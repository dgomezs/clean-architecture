package com.acme.reservation.app;

import com.acme.reservation.application.response.RefundBreakdown;
import com.acme.reservation.entity.Reservation;
import com.acme.reservation.gateway.FinanceGateway;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class FinanceGatewayImpl implements FinanceGateway {

  @Override
  public Mono<Void> refundReservation(RefundBreakdown refundBreakdown, Reservation reservation) {
    log.info(
        "Refunding {} to customer {}",
        refundBreakdown.getAmountToRefund().getPrice(),
        reservation.getCustomer().getEmail());
    return Mono.empty();
  }
}
