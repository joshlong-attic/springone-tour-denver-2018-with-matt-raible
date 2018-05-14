package com.example.upppercase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;

import java.util.function.Function;

@SpringBootApplication
public class UpppercaseApplication {

		@Bean
		Function<Flux<String>, Flux<String>> uppercase() {
				return stringFlux -> stringFlux.map(String::toUpperCase);
		}

		public static void main(String[] args) {
				SpringApplication.run(UpppercaseApplication.class, args);
		}
}
