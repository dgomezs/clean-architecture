package com.acme.reservation.rest.controller;

import com.acme.reservation.application.usecases.cancellation.acme_team.CancelReservationAsAcmeTeamUseCase;
import com.acme.reservation.application.usecases.cancellation.customer.CancelReservationAsCustomerUseCase;
import com.acme.reservation.entity.ReservationId;
import com.acme.reservation.rest.adapter.RestRefundAdapter;
import com.acme.reservation.rest.response.RestRefundBreakdown;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("reservation/cancel")
public class CancellReservationController {

  private final CancelReservationAsCustomerUseCase cancelReservationAsCustomerUseCase;
  private final CancelReservationAsAcmeTeamUseCase cancelReservationAsAcmeTeamUseCase;
  private final RestRefundAdapter restRefundAdapter;

  public CancellReservationController(
      CancelReservationAsCustomerUseCase cancelReservationAsCustomerUseCase,
      CancelReservationAsAcmeTeamUseCase cancelReservationAsAcmeTeamUseCase,
      RestRefundAdapter restRefundAdapter) {
    this.cancelReservationAsCustomerUseCase = cancelReservationAsCustomerUseCase;
    this.cancelReservationAsAcmeTeamUseCase = cancelReservationAsAcmeTeamUseCase;
    this.restRefundAdapter = restRefundAdapter;
  }

  @GetMapping(value = "/customer/{reservationId}")
  public Mono<RestRefundBreakdown> cancelAsCustomer(@PathVariable String reservationId) {
    // TODO add security so customers can only cancel their own reservations
    return cancelReservationAsCustomerUseCase
        .cancelAsCustomer(new ReservationId(reservationId))
        .map(this.restRefundAdapter::toRestRefundBreakDown);
  }

  @GetMapping(value = "/acme/{reservationId}")
  public Mono<RestRefundBreakdown> cancelAsAcmeTeam(@PathVariable String reservationId) {
    // TODO add security
    return cancelReservationAsAcmeTeamUseCase
        .cancelAsAcmeTeam(new ReservationId(reservationId))
        .map(this.restRefundAdapter::toRestRefundBreakDown);
  }
}
