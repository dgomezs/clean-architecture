package com.acme.reservation.cancellation;

import com.acme.reservation.application.event.ReservationEventPublisher;
import com.acme.reservation.application.repository.ReservationRepository;
import com.acme.reservation.application.usecases.cancellation.acme_team.CancelReservationAsAcmeTeamImpl;
import com.acme.reservation.application.usecases.cancellation.acme_team.CancelReservationAsAcmeTeamUseCase;
import com.acme.reservation.application.usecases.cancellation.customer.CancelReservationAsCustomerImpl;
import com.acme.reservation.application.usecases.cancellation.customer.CancelReservationAsCustomerUseCase;
import com.acme.reservation.cancellation.helpers.ReservationMockData;
import com.acme.reservation.cancellation.helpers.ReservationVerificationRules;
import com.acme.reservation.entity.cancellation.policy.CancellationPolicyFactory;
import com.acme.reservation.entity.cancellation.policy.CancellationPolicyFactoryImpl;
import com.acme.reservation.gateway.FinanceGateway;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.ReactiveTransaction;
import org.springframework.transaction.ReactiveTransactionManager;

@Configuration
public class ReservationTestConfig {

  @Bean
  public ReservationRepository reservationRepository() {
    return Mockito.mock(ReservationRepository.class);
  }

  @Bean
  public FinanceGateway financeGateway() {
    return Mockito.mock(FinanceGateway.class);
  }

  @Bean
  public ReactiveTransactionManager reactiveTransactionManager() {
    return Mockito.mock(ReactiveTransactionManager.class);
  }

  @Bean
  public CancellationPolicyFactory cancellationPolicyFactory() {
    return new CancellationPolicyFactoryImpl();
  }

  @Bean
  ReservationEventPublisher eventBus() {
    return Mockito.mock(ReservationEventPublisher.class);
  }

  @Bean
  ReactiveTransaction reactiveTransaction() {
    return Mockito.mock(ReactiveTransaction.class);
  }

  @Bean
  public CancelReservationAsCustomerUseCase cancelReservationAsCustomerUseCase(
      ReservationRepository reservationRepository,
      CancellationPolicyFactory cancellationPolicyFactory,
      ReservationEventPublisher eventBus,
      FinanceGateway financeGateway,
      ReactiveTransactionManager reactiveTransactionManager) {
    return new CancelReservationAsCustomerImpl(
        reservationRepository,
        cancellationPolicyFactory,
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

  @Bean
  public ReservationMockData reservationMockData(
      ReservationRepository reservationRepository,
      ReactiveTransactionManager reactiveTransactionManager,
      FinanceGateway financeGateway,
      ReservationEventPublisher eventPublisher) {
    return new ReservationMockData(
        reservationRepository, reactiveTransactionManager, financeGateway, eventPublisher);
  }

  @Bean
  public ReservationVerificationRules reservationVerificationRules(
      ReservationRepository reservationRepository,
      ReactiveTransactionManager reactiveTransactionManager,
      FinanceGateway financeGateway,
      ReservationEventPublisher eventPublisher) {
    return new ReservationVerificationRules(
        reservationRepository, reactiveTransactionManager, financeGateway, eventPublisher);
  }
}
