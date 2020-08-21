package com.acme.reservation.application.usecases.search.customer;

import com.acme.reservation.application.repository.SearchReservationRepository;
import com.acme.reservation.application.response.ReservationListingCustomer;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;

@AllArgsConstructor
public class SearchReservationCustomerUseCaseImpl implements SearchReservationCustomerUseCase {

  private final SearchReservationRepository searchReservationRepository;

  @Override
  public Flux<ReservationListingCustomer> searchReservationsForCustomers(Long customerId) {
    return searchReservationRepository.searchReservationsForCustomers(customerId);
  }
}
