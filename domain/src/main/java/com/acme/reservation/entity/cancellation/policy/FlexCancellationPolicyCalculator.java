package com.acme.reservation.entity.cancellation.policy;

import com.acme.reservation.application.response.RefundBreakdown;
import com.acme.reservation.entity.Money;
import com.acme.reservation.entity.Reservation;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FlexCancellationPolicyCalculator implements CancellationPolicyCalculator {

  public static final int HOURS_CUT_OFF_FOR_FLEX = 24;
  private final Clock clock;

  @Override
  public RefundBreakdown calculateRefundBreakdown(Reservation reservation) {
    if (lessThan24hBeforeReservationStarts(reservation)) {
      return noRefund();
    }
    return fullRefund(reservation);
  }

  private RefundBreakdown fullRefund(Reservation reservation) {
    return new RefundBreakdown(reservation.getTotalRefundableMoneyToCustomer());
  }

  private boolean lessThan24hBeforeReservationStarts(Reservation reservation) {
    Instant startOfReservation =
        reservation.getStartDate().atZone(reservation.getDestination().getTimeZone()).toInstant();
    Instant currentTime = Instant.now(clock);
    long hoursToStartOfReservation = Duration.between(currentTime, startOfReservation).toHours();
    return hoursToStartOfReservation < HOURS_CUT_OFF_FOR_FLEX;
  }

  private RefundBreakdown noRefund() {
    return new RefundBreakdown(new Money(BigDecimal.ZERO));
  }
}
