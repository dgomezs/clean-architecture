package com.acme.reservation.application.event;

import com.acme.reservation.application.response.RefundBreakdown;
import com.acme.reservation.entity.Reservation;
import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class ReservationCancelledEvent extends ReservationEvent {

  private final RefundBreakdown refundBreakdown;

  public ReservationCancelledEvent(Reservation reservation, RefundBreakdown refundBreakdown) {
    super(reservation);
    this.refundBreakdown = refundBreakdown;
  }
}
