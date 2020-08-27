package com.acme.reservation.application.response;

import com.acme.reservation.entity.Customer;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Delegate;

@Builder
@Getter
public class ReservationListingAcmeTeam {
  @Delegate private final ReservationListingCustomer reservationListingCustomer;

  private final Customer customer;
}
