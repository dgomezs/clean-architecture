package com.acme.reservation.application.usecases.cancellation.acme_team;

import com.acme.reservation.application.response.RefundBreakdown;
import com.acme.reservation.entity.ReservationId;
import reactor.core.publisher.Mono;

public interface CancelReservationAsAcmeTeamUseCase {

  Mono<RefundBreakdown> cancelAsAcmeTeam(ReservationId reservationId);
}
