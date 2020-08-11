package com.acme.reservation.entity.cancellation.policy;

import com.acme.reservation.application.response.RefundBreakdown;
import com.acme.reservation.entity.Reservation;
import reactor.core.publisher.Mono;

public interface CancellationPolicy {

  Mono<RefundBreakdown> calculateRefundBreakdown(Reservation reservation);
}
