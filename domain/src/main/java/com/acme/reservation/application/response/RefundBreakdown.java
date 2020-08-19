package com.acme.reservation.application.response;

import com.acme.reservation.entity.Money;
import java.math.BigDecimal;
import lombok.Value;

@Value
public class RefundBreakdown {

  private Money amountToRefund;

  public boolean isThereMoneyToRefundToCustomer() {
    return amountToRefund.getPrice().compareTo(BigDecimal.ZERO) > 0;
  }
}
