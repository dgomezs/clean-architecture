package com.acme.reservation.persistence;

import com.acme.reservation.application.repository.ReservationRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(PersistenceConfiguration.class)
public class TestConfig {

  @Bean
  public TestHelper testHelper(ReservationRepository reservationRepository) {
    return new TestHelper(reservationRepository);
  }
}
