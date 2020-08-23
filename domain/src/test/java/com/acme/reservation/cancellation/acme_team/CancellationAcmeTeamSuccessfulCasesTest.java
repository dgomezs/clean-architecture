package com.acme.reservation.cancellation.acme_team;

import com.acme.reservation.ReservationTestConfig;
import com.acme.reservation.application.response.RefundBreakdown;
import com.acme.reservation.application.usecases.cancellation.acme_team.CancelReservationAsAcmeTeamUseCase;
import com.acme.reservation.entity.Reservation;
import com.acme.reservation.helpers.MockTransaction;
import com.acme.reservation.helpers.ReservationMockData;
import com.acme.reservation.helpers.ReservationVerificationRules;
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
public class CancellationAcmeTeamSuccessfulCasesTest {

  @Autowired private ReservationMockData reservationMockData;
  @Autowired private ReservationVerificationRules reservationVerificationRules;
  @Autowired private CancelReservationAsAcmeTeamUseCase cancelReservationAsAcmeTeamUseCase;

  @Before
  public void cleanUpMocks() {
    reservationMockData.cleanUpMocks();
  }

  @Test
  public void ensureReservationIsCancelledAndAllMoneyIsRefunded() {
    MockTransaction mockTransaction = new MockTransaction();
    Reservation reservation = reservationMockData.getFlexReservation();
    reservationMockData.configureTransaction(mockTransaction);
    reservationMockData.simulateFinanceGatewaySucceeds(reservation);
    reservationMockData.updateStatusSucceeds(reservation);

    cancelReservationAndVerifyAssertions(mockTransaction, reservation);
  }

  private void cancelReservationAndVerifyAssertions(
      MockTransaction mockTransaction, Reservation reservation) {
    Mono<RefundBreakdown> refundBreakdownMono =
        cancelReservationAsAcmeTeamUseCase.cancelAsAcmeTeam(reservation.getReservationId());

    StepVerifier.create(refundBreakdownMono)
        .assertNext(
            refundBreakdown -> {
              reservationVerificationRules.verifyTransactionWasCommitted(mockTransaction);
              reservationVerificationRules.verifyReservationWasCancelled(
                  reservation, refundBreakdown);
              reservationVerificationRules.verifyAllMoneyWasRefunded(refundBreakdown, reservation);
              reservationVerificationRules.verifyCancellationEventWasPublished(reservation);
            })
        .verifyComplete();
  }
}
