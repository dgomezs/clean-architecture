package com.acme.reservation.application.usecases.cancellation.customer;

import com.acme.reservation.application.event.ReservationEventPublisher;
import com.acme.reservation.application.repository.ReservationRepository;
import com.acme.reservation.application.response.RefundBreakdown;
import com.acme.reservation.application.usecases.cancellation.CancellationFlow;
import com.acme.reservation.entity.Reservation;
import com.acme.reservation.entity.ReservationId;
import com.acme.reservation.entity.cancellation.policy.CancellationPolicyCalculator;
import com.acme.reservation.entity.cancellation.policy.CancellationPolicyCalculatorFactory;
import com.acme.reservation.gateway.FinanceGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.ReactiveTransactionManager;
import reactor.core.publisher.Mono;

@Slf4j
public class CancelReservationAsCustomerImpl extends CancellationFlow
    implements CancelReservationAsCustomerUseCase {

  private final CancellationPolicyCalculatorFactory cancellationPolicyCalculatorFactory;

  public CancelReservationAsCustomerImpl(
      ReservationRepository reservationRepository,
      CancellationPolicyCalculatorFactory cancellationPolicyCalculatorFactory,
      ReservationEventPublisher eventBus,
      FinanceGateway financeGateway,
      ReactiveTransactionManager reactiveTransactionManager) {
    super(reservationRepository, eventBus, financeGateway, reactiveTransactionManager);
    this.cancellationPolicyCalculatorFactory = cancellationPolicyCalculatorFactory;
  }

  @Override
  public Mono<RefundBreakdown> cancelAsCustomer(ReservationId reservationId) {
    return cancellation(reservationId);
  }

  @Override
  protected RefundBreakdown calculateRefundBreakdown(Reservation reservation) {
    CancellationPolicyCalculator applicableCancellationPolicy =
        cancellationPolicyCalculatorFactory.getApplicableCancellationPolicy(reservation);
    return applicableCancellationPolicy.calculateRefundBreakdown(reservation);
  }
}
