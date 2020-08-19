package com.acme.reservation.entity.cancellation.policy;

import com.acme.reservation.entity.Reservation;
import java.time.Clock;
import java.util.HashMap;
import java.util.Optional;

public class CancellationPolicyCalculatorFactoryImpl
    implements CancellationPolicyCalculatorFactory {

  private final HashMap<CancellationPolicy, CancellationPolicyCalculator> cancellationPolicyHashMap;

  public CancellationPolicyCalculatorFactoryImpl(Clock clock) {
    cancellationPolicyHashMap = new HashMap<>();
    cancellationPolicyHashMap.put(
        CancellationPolicy.FLEX, new FlexCancellationPolicyCalculator(clock));
  }

  @Override
  public CancellationPolicyCalculator getApplicableCancellationPolicy(Reservation reservation) {
    CancellationPolicy cancellationPolicy = reservation.getCancellationPolicy();
    return Optional.ofNullable(cancellationPolicyHashMap.get(cancellationPolicy))
        .orElseThrow(
            () ->
                new IllegalStateException(
                    "No cancellation policy defined for type " + cancellationPolicy));
  }
}
