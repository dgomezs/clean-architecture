package com.acme.reservation.persistence.crud;

import com.acme.reservation.persistence.model.RefundPersistence;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface RefundCrudRepository extends ReactiveCrudRepository<RefundPersistence, Long> {

  @Query("SELECT * from refund where reservation_id = ?1")
  Flux<RefundPersistence> findByReservationId(String reservationId);
}
