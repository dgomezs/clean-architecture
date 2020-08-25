package com.acme.reservation.creation;

import com.acme.reservation.ReservationTestConfig;
import com.acme.reservation.application.request.CreateReservationDto;
import com.acme.reservation.application.usecases.creation.CreateReservationUseCase;
import com.acme.reservation.entity.Reservation;
import com.acme.reservation.helpers.ReservationMockData;
import com.acme.reservation.helpers.ReservationVerificationRules;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReservationTestConfig.class)
public class CreateReservationTest {

  @Autowired private ReservationMockData reservationMockData;
  @Autowired private ReservationVerificationRules reservationVerificationRules;
  @Autowired private CreateReservationUseCase createReservationUseCase;

  @Before
  public void cleanUpMocks() {
    reservationMockData.cleanUpMocks();
  }

  @Test
  public void reservationIsCreatedAndEventIsPublished() {

    CreateReservationDto randomFlexReservation = reservationMockData.buildFlexReservationDto();

    Reservation reservation = reservationMockData.createReservationFromDto(randomFlexReservation);

    reservationMockData.simulateCreationSuccess(reservation);
    reservationMockData.mockGetReservation(reservation);

    Mono<Reservation> reservationCreated =
        createReservationUseCase.createReservation(randomFlexReservation);

    StepVerifier.create(reservationCreated)
        .assertNext(
            r -> {
              Assert.assertNotNull(r.getReservationId());
              reservationVerificationRules.verifyReservationWasCreated(r);
              reservationVerificationRules.verifyCreationEventWasPublished(r);
            })
        .verifyComplete();
  }

  @Test
  public void testThatIfCreationFailsNoEventIsPublished() {

    CreateReservationDto randomFlexReservation = reservationMockData.buildFlexReservationDto();

    reservationMockData.simulateCreateReservationFails();

    Mono<Reservation> reservationCreated =
        createReservationUseCase.createReservation(randomFlexReservation);

    StepVerifier.create(reservationCreated)
        .expectErrorSatisfies(
            e -> {
              // TODO investigate why test does not work with IllegalAccessError
              Assert.assertEquals(e.getClass(), IllegalStateException.class);
              reservationVerificationRules.verifyNoEventsWerePublished();
            })
        .verify();
  }
}
