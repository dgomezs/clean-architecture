package com.acme.reservation.persistence;

import com.acme.reservation.application.repository.ReservationRepository;
import com.acme.reservation.entity.Customer;
import com.acme.reservation.entity.Destination;
import com.acme.reservation.entity.Reservation;
import com.acme.reservation.entity.ReservationId;
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
public class ReservationRepositoryTest {

  @Autowired private ReservationRepository reservationRepository;

  @Test
  public void storeReservation() {
    Reservation reservation = TestHelper.createReservation();
    Mono<ReservationId> reservationIdMono =
        this.reservationRepository.createReservation(reservation);

    Mono<Reservation> reservationMono =
        reservationIdMono.flatMap(r -> this.reservationRepository.getReservationById(r));

    StepVerifier.create(reservationMono)
        .assertNext(
            r -> {
              verifyCustomer(reservation.getCustomer(), r.getCustomer());
              verifyDestination(reservation.getDestination(), r.getDestination());
              verifyReservation(reservation, r);
            })
        .verifyComplete();
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
