package com.acme.reservation.persistence;

import com.acme.reservation.application.repository.ReservationRepository;
import com.acme.reservation.application.repository.SearchReservationRepository;
import com.acme.reservation.application.request.SearchReservationFilter;
import com.acme.reservation.application.response.ReservationListingAcmeTeam;
import com.acme.reservation.application.response.ReservationListingCustomer;
import com.acme.reservation.entity.Customer;
import com.acme.reservation.entity.Destination;
import com.acme.reservation.entity.Reservation;
import com.acme.reservation.entity.ReservationId;
import com.acme.reservation.entity.ReservationStatus;
import java.util.Collections;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
public class ReservationRepositoryTest {

  @Autowired private ReservationRepository reservationRepository;
  @Autowired private SearchReservationRepository searchReservationRepository;

  @Test
  public void storeReservation() {
    Reservation reservation = TestHelper.createReservation();
    Mono<Reservation> reservationMono = createReservation(reservation);
    StepVerifier.create(reservationMono)
        .assertNext(
            r -> {
              verifyCustomer(reservation.getCustomer(), r.getCustomer());
              verifyDestination(reservation.getDestination(), r.getDestination());
              verifyReservation(reservation, r);
            })
        .verifyComplete();
  }

  @Test
  public void createReservationForCustomerAndFindIt() {
    Reservation reservation = TestHelper.createReservation();
    Mono<Reservation> reservationMono = createReservation(reservation);
    Flux<ReservationListingCustomer> reservations =
        reservationMono.flatMapMany(
            r ->
                searchReservationRepository.searchReservationsForCustomers(
                    r.getCustomer().getId()));

    StepVerifier.create(reservations)
        .assertNext(r -> verifyCustomerListing(r, reservation))
        .verifyComplete();
  }

  @Test
  public void createReservationForAcmeTeamAndFindIt() {
    Reservation reservation = TestHelper.createCompletedReservation();
    Mono<Reservation> reservationMono = createReservation(reservation);
    SearchReservationFilter searchReservationFilter = new SearchReservationFilter();
    searchReservationFilter.setStatusList(Collections.singletonList(ReservationStatus.COMPLETED));
    Flux<ReservationListingAcmeTeam> reservations =
        reservationMono.flatMapMany(
            r -> searchReservationRepository.searchReservations(searchReservationFilter));

    StepVerifier.create(reservations)
        .assertNext(r -> verifyAcmeTeamListing(r, reservation))
        .verifyComplete();
  }

  private void verifyAcmeTeamListing(ReservationListingAcmeTeam r, Reservation reservation) {
    verifyCustomerListing(r.getReservationListingCustomer(), reservation);
  }

  private void verifyCustomerListing(ReservationListingCustomer r, Reservation reservation) {
    Assert.assertEquals(r.getStartDate(), reservation.getStartDate());
  }

  private Mono<Reservation> createReservation(Reservation reservation) {

    Mono<ReservationId> reservationIdMono =
        this.reservationRepository.createReservation(reservation);

    return reservationIdMono.flatMap(r -> this.reservationRepository.getReservationById(r));
  }

  private void verifyReservation(Reservation reservation, Reservation r) {
    Assert.assertEquals(reservation.getStartDate(), r.getStartDate());
    Assert.assertEquals(reservation.getEndDate(), r.getEndDate());
    Assert.assertEquals(reservation.getStatus(), r.getStatus());
  }

  private void verifyDestination(Destination d1, Destination d2) {
    Assert.assertEquals(d1.getName(), d2.getName());
    Assert.assertEquals(d1.getTimeZone().getId(), d2.getTimeZone().getId());
  }

  private void verifyCustomer(Customer c1, Customer c2) {
    Assert.assertEquals(c1.getFirstName(), c2.getFirstName());
  }
}
