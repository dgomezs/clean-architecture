package com.acme.reservation;

import com.acme.reservation.application.event.ReservationEventPublisher;
import com.acme.reservation.application.repository.ReservationRepository;
import com.acme.reservation.application.usecases.cancellation.acme_team.CancelReservationAsAcmeTeamImpl;
import com.acme.reservation.application.usecases.cancellation.acme_team.CancelReservationAsAcmeTeamUseCase;
import com.acme.reservation.application.usecases.cancellation.customer.CancelReservationAsCustomerImpl;
import com.acme.reservation.application.usecases.cancellation.customer.CancelReservationAsCustomerUseCase;
import com.acme.reservation.application.usecases.customer_reservation.CreateReservationUseCase;
import com.acme.reservation.application.usecases.customer_reservation.CreateReservationUseCaseImpl;
import com.acme.reservation.entity.cancellation.policy.CancellationPolicyCalculatorFactory;
import com.acme.reservation.gateway.FinanceGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.ReactiveTransactionManager;

@Configuration
@Import(ReservationMockConfig.class)
public class ReservationTestConfig {

  @Bean
  CreateReservationUseCase createReservationUseCase(
      ReservationRepository reservationRepository, ReservationEventPublisher eventBus) {
    return new CreateReservationUseCaseImpl(reservationRepository, eventBus);
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
