package com.acme.reservation.application.usecases.cancellation.customer;

import com.acme.reservation.application.event.ReservationCancelled;
import com.acme.reservation.application.event.ReservationEventPublisher;
import com.acme.reservation.application.exception.ReservationNotFoundException;
import com.acme.reservation.application.repository.ReservationRepository;
import com.acme.reservation.application.response.RefundBreakdown;
import com.acme.reservation.entity.Reservation;
import com.acme.reservation.entity.ReservationId;
import com.acme.reservation.entity.cancellation.policy.CancellationPolicyFactory;
import com.acme.reservation.gateway.FinanceGateway;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Slf4j
public class CancelReservationAsCustomerImpl implements CancelReservationAsCustomerUseCase {

  private final ReservationRepository reservationRepository;
  private final CancellationPolicyFactory cancellationPolicyFactory;
  private final ReservationEventPublisher eventBus;
  private final FinanceGateway financeGateway;
  private final ReactiveTransactionManager reactiveTransactionManager;

  @Override
  public Mono<RefundBreakdown> cancelAsCustomer(ReservationId reservationId) {

    return this.reservationRepository
        .getReservationById(reservationId)
        .flatMap(
            reservation -> {
              RefundBreakdown refundBreakdown = calculateRefundBreakdown(reservation);

              reservation.cancel(refundBreakdown);

              Mono<Void> persistedOperation = reservationRepository.updateStatus(reservation);

              Mono<Void> refundOperation = refundMoneyToCustomer(reservation, refundBreakdown);

              Mono<Void> saveThenRefund = persistedOperation.and(refundOperation);

              return executeInTransaction(saveThenRefund)
                  .thenReturn(refundBreakdown)
                  .doOnNext(r -> this.notifyReservationCancelled(reservation));
            })
        .switchIfEmpty(throwReservationNotFound(reservationId));
  }

  private Mono<Void> refundMoneyToCustomer(
      Reservation reservation, RefundBreakdown refundBreakdown) {
    return refundBreakdown.isThereMoneyToRefundToCustomer()
        ? financeGateway.refundReservation(refundBreakdown, reservation)
        : Mono.empty();
  }

  private Mono<Void> executeInTransaction(Mono<Void> toBeExecutedInTransaction) {
    TransactionalOperator transactionalOperator =
        TransactionalOperator.create(reactiveTransactionManager);
    return transactionalOperator.transactional(toBeExecutedInTransaction);
  }

  private void notifyReservationCancelled(Reservation reservation) {
    eventBus.publish(new ReservationCancelled(reservation));
  }

  private RefundBreakdown calculateRefundBreakdown(Reservation reservation) {
    return new RefundBreakdown();
  }

  private Mono<RefundBreakdown> throwReservationNotFound(ReservationId reservationId) {
    val errorMessage =
        String.format("Couldn't find reservation with id %s", reservationId.getKey());
    return Mono.defer(() -> Mono.error(new ReservationNotFoundException(errorMessage)));
  }
}
