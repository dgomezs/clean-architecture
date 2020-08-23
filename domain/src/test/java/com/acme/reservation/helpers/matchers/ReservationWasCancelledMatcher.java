package com.acme.reservation.helpers.matchers;

import com.acme.reservation.application.response.RefundBreakdown;
import com.acme.reservation.entity.Reservation;
import com.acme.reservation.entity.ReservationStatus;
import lombok.AllArgsConstructor;
import org.mockito.ArgumentMatcher;

@AllArgsConstructor
public class ReservationWasCancelledMatcher implements ArgumentMatcher<Reservation> {

  private final Reservation reservation;
  private final RefundBreakdown refundBreakdown;

  @Override
  public boolean matches(Reservation r) {
    return ReservationStatus.CANCELLED.equals(r.getStatus())
        && refundBreakdown.equals(r.getCurrentRefund().orElseThrow())
        && reservation.getReservationId().equals(r.getReservationId());
  }
}
