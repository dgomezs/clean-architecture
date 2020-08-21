package com.acme.reservation.persistence;

import com.acme.reservation.application.repository.SearchReservationRepository;
import com.acme.reservation.application.request.SearchReservationFilter;
import com.acme.reservation.application.response.ReservationListingAcmeTeam;
import com.acme.reservation.application.response.ReservationListingCustomer;
import com.acme.reservation.persistence.adapters.ReservationAdapter;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;

@AllArgsConstructor
public class SearchReservationRepositoryImpl implements SearchReservationRepository {

  private final ReservationCrudRepository reservationCrudRepository;
  private final ReservationAdapter reservationAdapter;

  public Flux<ReservationListingAcmeTeam> searchReservations(
      SearchReservationFilter searchReservationFilter) {
    return null;
  }

  public Flux<ReservationListingCustomer> searchReservationsForCustomers(Long customerId) {
    return null;
  }
}
