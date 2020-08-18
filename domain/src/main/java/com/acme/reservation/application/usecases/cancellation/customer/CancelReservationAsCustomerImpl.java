package com.acme.reservation.application.usecases.cancellation.customer;

import com.acme.reservation.application.event.ReservationEventPublisher;
import com.acme.reservation.application.repository.ReservationRepository;
import com.acme.reservation.application.response.RefundBreakdown;
import com.acme.reservation.application.usecases.cancellation.CancellationFlow;
import com.acme.reservation.entity.Money;
import com.acme.reservation.entity.Reservation;
import com.acme.reservation.entity.ReservationId;
import com.acme.reservation.entity.cancellation.policy.CancellationPolicyFactory;
import com.acme.reservation.gateway.FinanceGateway;
import java.math.BigDecimal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.ReactiveTransactionManager;
import reactor.core.publisher.Mono;

@Slf4j
public class CancelReservationAsCustomerImpl extends CancellationFlow
    implements CancelReservationAsCustomerUseCase {

  private final CancellationPolicyFactory cancellationPolicyFactory;

  public CancelReservationAsCustomerImpl(
      ReservationRepository reservationRepository,
      CancellationPolicyFactory cancellationPolicyFactory,
      ReservationEventPublisher eventBus,
      FinanceGateway financeGateway,
      ReactiveTransactionManager reactiveTransactionManager) {
    super(reservationRepository, eventBus, financeGateway, reactiveTransactionManager);
    this.cancellationPolicyFactory = cancellationPolicyFactory;
  }

  @Override
  public Mono<RefundBreakdown> cancelAsCustomer(ReservationId reservationId) {
    return cancellation(reservationId);
  }

  @Override
  protected RefundBreakdown calculateRefundBreakdown(Reservation reservation) {
    return new RefundBreakdown(new Money(BigDecimal.ONE));
  }
}
