package com.acme.reservation.application.response;

import com.acme.reservation.entity.Money;
import lombok.Value;

@Value
public class RefundBreakdown {

  private Money amountToRefund;

  public boolean isThereMoneyToRefundToCustomer() {
    return true;
  }
}
