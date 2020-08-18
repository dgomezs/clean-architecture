package com.acme.reservation.entity;

import com.acme.reservation.application.response.RefundBreakdown;
import lombok.Getter;
import lombok.Setter;

public class Reservation {

  @Getter @Setter private ReservationId reservationId;

  @Getter private ReservationStatus reservationStatus;
  @Getter private RefundBreakdown currentRefund;
  @Getter private Money reservationPrice;

  public void cancel(RefundBreakdown refundBreakdown) {
    this.reservationStatus = ReservationStatus.CANCELLED;
    this.currentRefund = refundBreakdown;
  }

  public Money getTotalRefundableMoneyToCustomer() {
    // for now we assume that the total price of the reservation can be refunded
    return reservationPrice;
  }
}
