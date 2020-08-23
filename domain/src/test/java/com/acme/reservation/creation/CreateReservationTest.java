package com.acme.reservation.creation;

import com.acme.reservation.ReservationTestConfig;
import com.acme.reservation.application.usecases.customer_reservation.CreateReservationUseCase;
import com.acme.reservation.helpers.ReservationMockData;
import com.acme.reservation.helpers.ReservationVerificationRules;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
  public void createReservation() {}
}
