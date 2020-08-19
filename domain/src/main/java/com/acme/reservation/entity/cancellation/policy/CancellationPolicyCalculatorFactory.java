package com.acme.reservation.entity.cancellation.policy;

import com.acme.reservation.entity.Reservation;

public interface CancellationPolicyCalculatorFactory {

  CancellationPolicyCalculator getApplicableCancellationPolicy(Reservation reservation);
}
