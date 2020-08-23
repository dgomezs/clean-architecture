package com.acme.reservation.persistence.crud;

import com.acme.reservation.persistence.model.ReservationPersistence;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationCrudRepository
    extends ReactiveCrudRepository<ReservationPersistence, String> {}
