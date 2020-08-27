package com.acme.reservation.persistence;

import com.acme.reservation.entity.Reservation;
import reactor.core.publisher.Mono;

public interface RefundRepository {

  Mono<Void> storeCurrentRefund(Reservation reservation);
}
