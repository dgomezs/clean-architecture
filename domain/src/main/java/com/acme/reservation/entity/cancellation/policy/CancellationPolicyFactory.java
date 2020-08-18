package com.acme.reservation.entity.cancellation.policy;

import com.acme.reservation.entity.Reservation;

public interface CancellationPolicyFactory {

  CancellationPolicy getApplicableCancellationPolicy(Reservation reservation);
}
