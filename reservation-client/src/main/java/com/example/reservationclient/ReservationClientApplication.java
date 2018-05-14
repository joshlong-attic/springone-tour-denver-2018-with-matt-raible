package com.example.reservationclient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancerExchangeFilterFunction;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.hystrix.HystrixCommands;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class ReservationClientApplication {

		@Bean
		WebClient client(LoadBalancerExchangeFilterFunction eff) {
				return WebClient.builder().filter(eff).build();
		}

		@Bean
		RouterFunction<ServerResponse> adapter(WebClient client) {
				return RouterFunctions.route(RequestPredicates.GET("/reservations/names"), new HandlerFunction<ServerResponse>() {
						@Override
						public Mono<ServerResponse> handle(ServerRequest serverRequest) {

								Flux<String> flux = client
									.get()
									.uri("http://reservation-service/reservations")
									.retrieve()
									.bodyToFlux(Reservation.class)
									.map(Reservation::getReservationName);

								Publisher<String> fallback = HystrixCommands
									.from(flux)
									.fallback(Flux.just("EEK!"))
									.commandName("names")
									.eager()
									.build();

								return ServerResponse.ok().body(fallback, String.class);
						}
				});
		}

		@Bean
		RedisRateLimiter redisRateLimiter() {
				return new RedisRateLimiter(5, 6);
		}

	/*
	@Bean
		MapReactiveUserDetailsService authentication() {
				return new MapReactiveUserDetailsService(User.withDefaultPasswordEncoder().username("user").password("password").roles("USER").build());   //@rob_winch is sad :-(
		}

		@Bean
		SecurityWebFilterChain authorization(ServerHttpSecurity security) {
				//@formatter:off
				return
					security
						.csrf().disable()
						.httpBasic()
						.and()
						.authorizeExchange()
								.pathMatchers("/proxy").authenticated()
								.anyExchange().permitAll()
						.and()
						.build();
				//@formatter:on
		}
*/
		@Bean
		RouteLocator routeLocator(RouteLocatorBuilder rlb) {
				return rlb
					.routes()
					.route(rspec -> rspec.path("/proxy")
						.filters(fspec -> fspec
								.setPath("/reservations")
								.requestRateLimiter(c ->
										c.setRateLimiter(redisRateLimiter())
//								.setKeyResolver(new PrincipalNameKeyResolver())
								)
						)
						.uri("lb://reservation-service"))
					.build();
		}

		public static void main(String[] args) {
				SpringApplication.run(ReservationClientApplication.class, args);
		}
}


@Data
@AllArgsConstructor
@NoArgsConstructor
class Reservation {

		@Id
		private String id;

		private String reservationName;
}
