package com.acme.reservation.application.usecases.cancellation.customer;

import com.acme.reservation.application.response.RefundBreakdown;
import com.acme.reservation.entity.ReservationId;
import reactor.core.publisher.Mono;

public interface CancelReservationAsCustomerUseCase {

  Mono<RefundBreakdown> cancelAsCustomer(ReservationId reservationId);
}
