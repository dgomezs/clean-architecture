package com.acme.reservation.persistence;

import com.acme.reservation.application.repository.ReservationRepository;
import com.acme.reservation.application.repository.SearchReservationRepository;
import com.acme.reservation.application.request.SearchReservationFilter;
import com.acme.reservation.application.response.ReservationListingAcmeTeam;
import com.acme.reservation.application.response.ReservationListingCustomer;
import com.acme.reservation.entity.Reservation;
import com.acme.reservation.entity.ReservationStatus;
import java.util.Collections;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
public class CreateReservationTest {

  @Autowired private ReservationRepository reservationRepository;
  @Autowired private SearchReservationRepository searchReservationRepository;
  @Autowired private TestHelper testHelper;

  @Test
  public void storeReservation() {
    Reservation reservation = TestHelper.createReservation();
    Mono<Reservation> reservationMono = testHelper.createReservation(reservation);
    StepVerifier.create(reservationMono)
        .assertNext(
            r -> {
              Assert.assertTrue(
                  VerifyHelper.equalCustomer(reservation.getCustomer(), r.getCustomer()));
              Assert.assertTrue(
                  VerifyHelper.equalDestination(reservation.getDestination(), r.getDestination()));
              Assert.assertTrue(VerifyHelper.equalReservation(reservation, r));
            })
        .verifyComplete();
  }

  @Test
  public void createReservationForCustomerAndFindIt() {
    Reservation reservation = TestHelper.createReservation();
    Mono<Reservation> reservationMono = testHelper.createReservation(reservation);
    Mono<List<ReservationListingCustomer>> reservations =
        reservationMono
            .flatMapMany(
                r ->
                    searchReservationRepository.searchReservationsForCustomers(
                        r.getCustomer().getId()))
            .collectList();

    StepVerifier.create(reservations)
        .assertNext(
            reservationsList ->
                Assert.assertTrue(
                    reservationsList.stream()
                        .anyMatch(r -> VerifyHelper.verifyCustomerListing(r, reservation))))
        .verifyComplete();
  }

  @Test
  public void createReservationForAcmeTeamAndFindIt() {
    Reservation reservation = TestHelper.createCompletedReservation();
    Mono<Reservation> reservationMono = testHelper.createReservation(reservation);
    SearchReservationFilter searchReservationFilter = new SearchReservationFilter();
    searchReservationFilter.setStatusList(Collections.singletonList(ReservationStatus.COMPLETED));
    Mono<List<ReservationListingAcmeTeam>> reservations =
        reservationMono
            .flatMapMany(
                r -> searchReservationRepository.searchReservations(searchReservationFilter))
            .collectList();

    StepVerifier.create(reservations)
        .assertNext(
            reservationsList ->
                Assert.assertTrue(
                    reservationsList.stream()
                        .anyMatch(r -> VerifyHelper.verifyAcmeTeamListing(r, reservation))))
        .verifyComplete();
  }
}
