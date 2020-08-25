package com.acme.reservation.controller;

import com.acme.reservation.application.request.SearchReservationFilter;
import com.acme.reservation.application.response.ReservationListingAcmeTeam;
import com.acme.reservation.application.response.ReservationListingCustomer;
import com.acme.reservation.application.usecases.search.acme_team.SearchReservationAcmeTeamUseCase;
import com.acme.reservation.application.usecases.search.customer.SearchReservationCustomerUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("reservation")
public class SearchReservationsController {

  private final SearchReservationAcmeTeamUseCase searchReservationAcmeTeamUseCase;
  private final SearchReservationCustomerUseCase searchReservationCustomerUseCase;

  @Autowired
  public SearchReservationsController(
      SearchReservationAcmeTeamUseCase searchReservationAcmeTeamUseCase,
      SearchReservationCustomerUseCase searchReservationCustomerUseCase) {
    this.searchReservationAcmeTeamUseCase = searchReservationAcmeTeamUseCase;
    this.searchReservationCustomerUseCase = searchReservationCustomerUseCase;
  }

  @GetMapping(value = "/customer/{customerId:[\\d]+}")
  public Flux<ReservationListingCustomer> getReservationsByCustomer(@PathVariable Long customerId) {
    // TODO add security so customers can only retrieve their own reservations
    return searchReservationCustomerUseCase.searchReservationsForCustomers(customerId);
  }

  @GetMapping(value = "/search")
  public Flux<ReservationListingAcmeTeam> getReservationsByCustomer(
      SearchReservationFilter searchReservationFilter) {
    // TODO add security
    return searchReservationAcmeTeamUseCase.searchReservations(searchReservationFilter);
  }
}
