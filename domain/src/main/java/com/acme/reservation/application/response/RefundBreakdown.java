package com.acme.reservation.application.response;

import com.acme.reservation.entity.Money;

public class RefundBreakdown {

  private Money amountToRefund;

  public boolean isThereMoneyToRefundToCustomer() {
    return true;
  }
}
