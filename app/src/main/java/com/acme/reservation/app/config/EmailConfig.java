package com.acme.reservation.app.config;

import com.acme.reservation.application.event.ReservationEvent;
import com.acme.reservation.email.EmailSender;
import com.acme.reservation.email.EmailSenderImpl;
import com.acme.reservation.email.ReservationEmailService;
import com.acme.reservation.email.ReservationEmailServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.ReplayProcessor;
import reactor.core.scheduler.Schedulers;

@Configuration
public class EmailConfig {

  @Bean
  ReservationEmailService reservationEmailService(
      EmailSender emailSender, ReplayProcessor<ReservationEvent> eventBus) {
    return new ReservationEmailServiceImpl(eventBus, emailSender, Schedulers.boundedElastic());
  }

  @Bean
  EmailSender emailSender() {
    return new EmailSenderImpl();
  }
}
