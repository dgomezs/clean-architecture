package com.acme.reservation.rest.controller;

import com.acme.reservation.application.request.SearchReservationFilter;
import com.acme.reservation.application.usecases.search.acme_team.SearchReservationAcmeTeamUseCase;
import com.acme.reservation.application.usecases.search.customer.SearchReservationCustomerUseCase;
import com.acme.reservation.rest.adapter.RestReservationAdapter;
import com.acme.reservation.rest.response.RestReservationListingAcmeTeam;
import com.acme.reservation.rest.response.RestReservationListingCustomer;
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
  private final RestReservationAdapter restReservationAdapter;

  @Autowired
  public SearchReservationsController(
      SearchReservationAcmeTeamUseCase searchReservationAcmeTeamUseCase,
      SearchReservationCustomerUseCase searchReservationCustomerUseCase,
      RestReservationAdapter restReservationAdapter) {
    this.searchReservationAcmeTeamUseCase = searchReservationAcmeTeamUseCase;
    this.searchReservationCustomerUseCase = searchReservationCustomerUseCase;
    this.restReservationAdapter = restReservationAdapter;
  }

  @GetMapping(value = "/customer/{customerId:[\\d]+}")
  public Flux<RestReservationListingCustomer> getReservationsByCustomer(
      @PathVariable Long customerId) {
    // TODO add security so customers can only retrieve their own reservations
    return searchReservationCustomerUseCase
        .searchReservationsForCustomers(customerId)
        .map(this.restReservationAdapter::toReservationListingCustomer);
  }

  @GetMapping(value = "/search")
  public Flux<RestReservationListingAcmeTeam> getReservationsByCustomer(
      SearchReservationFilter searchReservationFilter) {
    // TODO add security
    return searchReservationAcmeTeamUseCase
        .searchReservations(searchReservationFilter)
        .map(this.restReservationAdapter::toReservationListingAcmeTeam);
  }
}
