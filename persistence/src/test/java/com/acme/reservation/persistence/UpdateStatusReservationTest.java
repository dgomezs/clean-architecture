package com.acme.reservation.persistence;

import com.acme.reservation.application.repository.ReservationRepository;
import com.acme.reservation.application.repository.SearchReservationRepository;
import com.acme.reservation.application.request.SearchReservationFilter;
import com.acme.reservation.application.response.ReservationListingAcmeTeam;
import com.acme.reservation.entity.Reservation;
import com.acme.reservation.entity.ReservationStatus;
import com.acme.reservation.persistence.crud.RefundCrudRepository;
import com.acme.reservation.persistence.model.RefundPersistence;
import java.util.Collections;
import java.util.List;
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
public class UpdateStatusReservationTest {

  @Autowired private RefundCrudRepository refundRepository;
  @Autowired private ReservationRepository reservationRepository;
  @Autowired private SearchReservationRepository searchReservationRepository;
  @Autowired private TestHelper testHelper;

  @Test
  public void storeReservationCancelled() {
    Mono<Reservation> reservationMono = testHelper.createReservationAndCancelledFullRefund();
    StepVerifier.create(reservationMono)
        .assertNext(
            r -> {
              Assert.assertEquals(ReservationStatus.CANCELLED, r.getStatus());
              Assert.assertNotNull(r.getCancellationTimestamp());
            })
        .verifyComplete();
  }

  @Test
  public void refundHasBeenCreated() {
    Mono<Reservation> reservationMono = testHelper.createReservationAndCancelledFullRefund();
    Flux<RefundPersistence> refundPersistenceFlux =
        reservationMono.flatMapMany(
            r -> refundRepository.findByReservationId(r.getReservationId().getKey()));
    StepVerifier.create(refundPersistenceFlux)
        .assertNext(VerifyHelper::verifyIsAFullRefund)
        .verifyComplete();
  }

  @Test
  public void findCancelledReservation() {
    Reservation reservation = TestHelper.createReservation();
    Mono<Reservation> reservationMono =
        testHelper.createReservationAndCancelledFullRefund(reservation);

    SearchReservationFilter reservationFilter = new SearchReservationFilter();
    reservationFilter.setStatusList(Collections.singletonList(ReservationStatus.CANCELLED));
    Mono<List<ReservationListingAcmeTeam>> reservationListingAcmeTeamFlux =
        searchReservationRepository.searchReservations(reservationFilter).collectList();

    StepVerifier.create(reservationMono.then(reservationListingAcmeTeamFlux))
        .assertNext(
            reservationList -> {
              Assert.assertTrue(
                  reservationList.stream()
                      .allMatch(r -> ReservationStatus.CANCELLED.equals(r.getStatus())));
              Assert.assertTrue(reservationList.size() > 0);
              Assert.assertTrue(
                  reservationList.stream()
                      .anyMatch(r -> VerifyHelper.verifyAcmeTeamListing(r, reservation)));
            })
        .verifyComplete();
  }
}
