package com.acme.reservation.persistence;

import com.acme.reservation.entity.Reservation;
import com.acme.reservation.persistence.adapters.RefundAdapter;
import com.acme.reservation.persistence.crud.RefundCrudRepository;
import com.acme.reservation.persistence.model.RefundPersistence;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class RefundRepositoryImpl implements RefundRepository {

  private final RefundCrudRepository refundCrudRepository;
  private final RefundAdapter refundAdapter;

  public Mono<Void> storeCurrentRefund(Reservation reservation) {
    RefundPersistence refundPersistence = refundAdapter.toRefundPersistence(reservation);
    return refundCrudRepository.save(refundPersistence).then();
  }
}
