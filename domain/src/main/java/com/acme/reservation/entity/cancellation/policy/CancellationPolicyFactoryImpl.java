package com.acme.reservation.entity.cancellation.policy;

import com.acme.reservation.entity.Reservation;

public class CancellationPolicyFactoryImpl implements CancellationPolicyFactory {

  @Override
  public CancellationPolicy getApplicableCancellationPolicy(Reservation reservation) {
    return null;
  }
}
