package com.acme.reservation.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
public class ReservationServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(ReservationServiceApplication.class, args);
  }
}
