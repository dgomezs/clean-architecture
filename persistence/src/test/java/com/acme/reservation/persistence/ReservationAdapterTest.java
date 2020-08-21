package com.acme.reservation.persistence;

import com.acme.reservation.entity.Reservation;
import com.acme.reservation.persistence.adapters.ReservationAdapter;
import com.acme.reservation.persistence.model.ReservationPersistence;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
public class ReservationAdapterTest {

  @Autowired ReservationAdapter reservationAdapter;

  @Test
  public void testReservationToPersistence() {

    Reservation reservation = TestHelper.createReservation();

    ReservationPersistence reservationPersistence =
        reservationAdapter.toReservationPersistence(reservation);

    Assert.assertEquals(
        reservationPersistence.getReservationId(), reservation.getReservationId().getKey());
  }
}
