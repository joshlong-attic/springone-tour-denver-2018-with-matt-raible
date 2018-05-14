package com.example.reservationservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class ReservationServiceApplication {

		@Bean
		ApplicationRunner runner(ReservationRepository rr) {
				return args ->
					rr.deleteAll()
						.thenMany(
							Flux.just("A", "B", "C")
								.map(x -> new Reservation(null, x))
								.flatMap(rr::save))
						.thenMany(rr.findAll())
						.subscribe(System.out::println);
		}

		@Bean
		RouterFunction<ServerResponse> routes(ReservationRepository rr) {
				return RouterFunctions.route(RequestPredicates.GET("/reservations"),
					serverRequest -> ServerResponse.ok().body(rr.findAll(), Reservation.class)) ;
		}

		public static void main(String[] args) {
				SpringApplication.run(ReservationServiceApplication.class, args);
		}
}


interface ReservationRepository extends ReactiveMongoRepository<Reservation, String> {
}

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
class Reservation {
		@Id
		private String id;
		private String reservationName;
}
