package com.acme.reservation.email;

import com.acme.reservation.application.event.ReservationEvent;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.ReplayProcessor;
import reactor.test.scheduler.VirtualTimeScheduler;

@Configuration
public class EmailTestConfig {

  @Bean
  ReplayProcessor<ReservationEvent> eventBus() {
    return ReplayProcessor.create();
  }

  @Bean
  VirtualTimeScheduler virtualTimeScheduler() {
    return VirtualTimeScheduler.create();
  }

  @Bean
  ReservationEmailService reservationEmailService(
      EmailSender emailSender,
      ReplayProcessor<ReservationEvent> eventBus,
      VirtualTimeScheduler virtualTimeScheduler) {
    return new ReservationEmailServiceImpl(eventBus, emailSender, virtualTimeScheduler);
  }

  @Bean
  EmailSender emailSender() {
    return Mockito.mock(EmailSender.class);
  }
}
