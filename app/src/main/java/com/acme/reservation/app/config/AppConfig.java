package com.acme.reservation.app.config;

import com.acme.reservation.app.FinanceGatewayImpl;
import com.acme.reservation.application.event.ReservationEvent;
import com.acme.reservation.gateway.FinanceGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.ReplayProcessor;

@Configuration
@ComponentScan(basePackages = "com.acme.reservation")
public class AppConfig {

  @Bean
  ReplayProcessor<ReservationEvent> eventBus() {
    return ReplayProcessor.create();
  }

  @Bean
  FinanceGateway financeGateway() {
    return new FinanceGatewayImpl();
  }
}
