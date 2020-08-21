package com.acme.reservation.application.response;

import java.time.Instant;
import java.util.Optional;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Delegate;

@Builder
@Getter
public class ReservationListingAcmeTeam {
  @Delegate private ReservationListingCustomer reservationListingCustomer;

}
