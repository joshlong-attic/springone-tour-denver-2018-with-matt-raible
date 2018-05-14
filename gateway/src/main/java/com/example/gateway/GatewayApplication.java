package com.example.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.ratelimit.PrincipalNameKeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.server.SecurityWebFilterChain;

@SpringBootApplication
public class GatewayApplication {

		@Bean
		RouteLocator routes(RouteLocatorBuilder rlb) {
				return rlb
					.routes()
					.route(s -> s
						.path("/proxy")
						.filters(f -> f
							.requestRateLimiter(c ->
								c.setRateLimiter(redisRateLimiter())
							)
						)
						.uri("http://spring.io:80/guides"))
					.build();
		}

		@Bean
		RedisRateLimiter redisRateLimiter() {
				return new RedisRateLimiter(2, 3);
		}

		public static void main(String[] args) {
				SpringApplication.run(GatewayApplication.class, args);
		}
}
