package com.acme.reservation.email;

import com.acme.reservation.application.response.RefundBreakdown;
import com.acme.reservation.entity.Reservation;

public interface ReservationEmailService {

  void sendReservationCreatedEmail(Reservation reservation);

  void sendReservationCancelledEmail(Reservation reservation, RefundBreakdown refundBreakdown);
}
