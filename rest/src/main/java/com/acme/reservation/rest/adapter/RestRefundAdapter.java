package com.acme.reservation.rest.adapter;

import com.acme.reservation.application.response.RefundBreakdown;
import com.acme.reservation.rest.response.RestRefundBreakdown;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RestRefundAdapter {

  @Mapping(target = "amountRefunded", source = "amountToRefund.price")
  RestRefundBreakdown toRestRefundBreakDown(RefundBreakdown refundBreakdown);
}
