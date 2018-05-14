package com.example.reservationservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@SpringBootApplication
public class ReservationServiceApplication {

		@Bean
		RouterFunction<ServerResponse> routes(ReservationRepository rr, Environment env) {
				return route(GET("/reservations"), serverRequest -> ServerResponse.ok().body(rr.findAll(), Reservation.class))
					.andRoute(GET("/message"), r -> ServerResponse.ok().body(Flux.just(env.getProperty("message")), String.class));
		}

		public static void main(String[] args) {
				SpringApplication.run(ReservationServiceApplication.class, args);
		}
}

@Component
class DataRunner implements ApplicationRunner {

		private final ReservationRepository reservationRepository;

		DataRunner(ReservationRepository reservationRepository) {
				this.reservationRepository = reservationRepository;
		}

		@Override
		public void run(ApplicationArguments args) throws Exception {

				this.reservationRepository.deleteAll()
					.thenMany(Flux.just("Josh", "Matt", "Sandra", "Mila", "Prasad", "Darin", "Leon", "Khushbu")
						.map(x -> new Reservation(null, x))
						.flatMap(reservationRepository::save))
					.thenMany(reservationRepository.findAll())
					.subscribe(System.out::println);

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
