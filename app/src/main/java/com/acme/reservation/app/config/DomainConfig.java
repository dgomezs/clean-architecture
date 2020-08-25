package com.acme.reservation.app.config;

import com.acme.reservation.app.EventBusImpl;
import com.acme.reservation.application.event.ReservationEvent;
import com.acme.reservation.application.event.ReservationEventPublisher;
import com.acme.reservation.application.repository.ReservationRepository;
import com.acme.reservation.application.repository.SearchReservationRepository;
import com.acme.reservation.application.usecases.cancellation.acme_team.CancelReservationAsAcmeTeamImpl;
import com.acme.reservation.application.usecases.cancellation.acme_team.CancelReservationAsAcmeTeamUseCase;
import com.acme.reservation.application.usecases.cancellation.customer.CancelReservationAsCustomerImpl;
import com.acme.reservation.application.usecases.cancellation.customer.CancelReservationAsCustomerUseCase;
import com.acme.reservation.application.usecases.creation.CreateReservationUseCase;
import com.acme.reservation.application.usecases.creation.CreateReservationUseCaseImpl;
import com.acme.reservation.application.usecases.search.acme_team.SearchReservationAcmeTeamUseCase;
import com.acme.reservation.application.usecases.search.acme_team.SearchReservationAcmeTeamUseCaseImpl;
import com.acme.reservation.application.usecases.search.customer.SearchReservationCustomerUseCase;
import com.acme.reservation.application.usecases.search.customer.SearchReservationCustomerUseCaseImpl;
import com.acme.reservation.entity.cancellation.policy.CancellationPolicyCalculatorFactory;
import com.acme.reservation.entity.cancellation.policy.CancellationPolicyCalculatorFactoryImpl;
import com.acme.reservation.gateway.FinanceGateway;
import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.ReactiveTransactionManager;
import reactor.core.publisher.ReplayProcessor;

@Configuration
public class DomainConfig {

  @Bean
  public ReservationEventPublisher reservationEventPublisher(
      ReplayProcessor<ReservationEvent> eventBus) {
    return new EventBusImpl(eventBus);
  }

  @Bean
  public CancellationPolicyCalculatorFactory cancellationPolicyFactory() {
    return new CancellationPolicyCalculatorFactoryImpl(Clock.systemUTC());
  }

  @Bean
  public SearchReservationAcmeTeamUseCase searchReservationAcmeTeamUseCase(
      SearchReservationRepository searchReservationRepository) {
    return new SearchReservationAcmeTeamUseCaseImpl(searchReservationRepository);
  }

  @Bean
  public CreateReservationUseCase createReservationUseCase(
      ReservationRepository reservationRepository, ReservationEventPublisher eventBus) {
    return new CreateReservationUseCaseImpl(reservationRepository, eventBus);
  }

  @Bean
  public SearchReservationCustomerUseCase searchReservationCustomerUseCase(
      SearchReservationRepository searchReservationRepository) {
    return new SearchReservationCustomerUseCaseImpl(searchReservationRepository);
  }

  @Bean
  public CancelReservationAsCustomerUseCase cancelReservationAsCustomerUseCase(
      ReservationRepository reservationRepository,
      CancellationPolicyCalculatorFactory cancellationPolicyCalculatorFactory,
      ReservationEventPublisher eventBus,
      FinanceGateway financeGateway,
      ReactiveTransactionManager reactiveTransactionManager) {
    return new CancelReservationAsCustomerImpl(
        reservationRepository,
        cancellationPolicyCalculatorFactory,
        eventBus,
        financeGateway,
        reactiveTransactionManager);
  }

  @Bean
  public CancelReservationAsAcmeTeamUseCase cancelReservationAsAcmeTeamUseCase(
      ReservationRepository reservationRepository,
      ReservationEventPublisher eventBus,
      FinanceGateway financeGateway,
      ReactiveTransactionManager reactiveTransactionManager) {
    return new CancelReservationAsAcmeTeamImpl(
        reservationRepository, eventBus, financeGateway, reactiveTransactionManager);
  }
}
