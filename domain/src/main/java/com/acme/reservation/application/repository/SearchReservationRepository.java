package com.acme.reservation.application.repository;

import com.acme.reservation.application.request.SearchReservationFilter;
import com.acme.reservation.application.response.ReservationListingAcmeTeam;
import com.acme.reservation.application.response.ReservationListingCustomer;
import reactor.core.publisher.Flux;

public interface SearchReservationRepository {

  Flux<ReservationListingAcmeTeam> searchReservations(
      SearchReservationFilter searchReservationFilter);

  Flux<ReservationListingCustomer> searchReservationsForCustomers(Long customerId);
}
