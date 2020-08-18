package com.acme.reservation.gateway;

import com.acme.reservation.application.response.RefundBreakdown;
import com.acme.reservation.entity.Reservation;
import reactor.core.publisher.Mono;

public interface FinanceGateway {

  public Mono<Void> refundReservation(RefundBreakdown refundBreakdown, Reservation reservation);
}
