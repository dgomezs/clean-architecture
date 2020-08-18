package com.acme.reservation.application.usecases.cancellation.acme_team;

import com.acme.reservation.application.event.ReservationEventPublisher;
import com.acme.reservation.application.repository.ReservationRepository;
import com.acme.reservation.application.response.RefundBreakdown;
import com.acme.reservation.application.usecases.cancellation.CancellationFlow;
import com.acme.reservation.entity.Reservation;
import com.acme.reservation.entity.ReservationId;
import com.acme.reservation.gateway.FinanceGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.ReactiveTransactionManager;
import reactor.core.publisher.Mono;

@Slf4j
public class CancelReservationAsAcmeTeamImpl extends CancellationFlow
    implements CancelReservationAsAcmeTeamUseCase {

  public CancelReservationAsAcmeTeamImpl(
      ReservationRepository reservationRepository,
      ReservationEventPublisher eventBus,
      FinanceGateway financeGateway,
      ReactiveTransactionManager reactiveTransactionManager) {
    super(reservationRepository, eventBus, financeGateway, reactiveTransactionManager);
  }

  @Override
  public Mono<RefundBreakdown> cancelAsAcmeTeam(ReservationId reservationId) {
    return cancellation(reservationId);
  }

  @Override
  protected RefundBreakdown calculateRefundBreakdown(Reservation reservation) {
    // if our team cancels then we refund the total possible refunded amount
    return new RefundBreakdown(reservation.getTotalRefundableMoneyToCustomer());
  }
}
