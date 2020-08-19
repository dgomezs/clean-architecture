package com.acme.reservation.entity.cancellation.policy;

import com.acme.reservation.application.response.RefundBreakdown;
import com.acme.reservation.entity.Reservation;

public interface CancellationPolicyCalculator {

  RefundBreakdown calculateRefundBreakdown(Reservation reservation);
}
