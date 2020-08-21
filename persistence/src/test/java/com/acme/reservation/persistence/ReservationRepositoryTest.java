package com.acme.reservation.persistence;

import com.acme.reservation.application.repository.ReservationRepository;
import com.acme.reservation.entity.Reservation;
import com.acme.reservation.entity.ReservationId;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
public class ReservationRepositoryTest {

  @Autowired private ReservationRepository reservationRepository;

  @Test
  public void storeReservation() {
    Reservation reservation = TestHelper.createReservation();
    Mono<ReservationId> reservationIdMono =
        this.reservationRepository.createReservation(reservation);

    StepVerifier.create(reservationIdMono)
        .assertNext(r -> Assert.assertFalse(StringUtils.isEmpty(r.getKey())))
        .verifyComplete();
  }
}
