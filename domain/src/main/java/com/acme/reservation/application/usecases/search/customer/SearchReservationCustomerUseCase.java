package com.acme.reservation.application.usecases.search.customer;

import com.acme.reservation.application.response.ReservationListingCustomer;
import reactor.core.publisher.Flux;

public interface SearchReservationCustomerUseCase {
  Flux<ReservationListingCustomer> searchReservationsForCustomers(Long customerId);

}
