package com.acme.reservation.application.response;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Delegate;

@Builder
@Getter
public class ReservationListingAcmeTeam {
  @Delegate private ReservationListingCustomer reservationListingCustomer;
}
