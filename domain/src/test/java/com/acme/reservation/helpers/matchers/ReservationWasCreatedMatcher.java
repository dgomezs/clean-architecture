package com.acme.reservation.helpers.matchers;

import com.acme.reservation.entity.Reservation;
import com.acme.reservation.entity.ReservationStatus;
import lombok.AllArgsConstructor;
import org.mockito.ArgumentMatcher;

@AllArgsConstructor
public class ReservationWasCreatedMatcher implements ArgumentMatcher<Reservation> {

  private final Reservation reservation;

  @Override
  public boolean matches(Reservation r) {
    return ReservationStatus.CREATED.equals(r.getStatus())
        && reservation.getStartDate().equals(r.getStartDate());
    // TODO add more elements of comparison
  }
}
