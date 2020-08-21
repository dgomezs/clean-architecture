package com.acme.reservation.persistence;

import com.acme.reservation.application.repository.SearchReservationRepository;
import com.acme.reservation.application.request.SearchReservationFilter;
import com.acme.reservation.application.response.ReservationListingAcmeTeam;
import com.acme.reservation.application.response.ReservationListingCustomer;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;

@AllArgsConstructor
public class SearchReservationRepositoryImpl implements SearchReservationRepository {

  private ReservationRepository reservationRepository;

  public Flux<ReservationListingAcmeTeam> searchReservations(
      SearchReservationFilter searchReservationFilter) {
    return null;
  }

  public Flux<ReservationListingCustomer> searchReservationsForCustomers(Long customerId) {
    return null;
  }
}
