package com.acme.reservation.entity;

import com.acme.reservation.application.response.RefundBreakdown;
import lombok.Getter;
import lombok.Setter;

public class Reservation {

  @Getter @Setter private ReservationId reservationId;

  public void addRefund(RefundBreakdown refundBreakdown) {}

  public void cancel(RefundBreakdown refundBreakdown) {}
}
