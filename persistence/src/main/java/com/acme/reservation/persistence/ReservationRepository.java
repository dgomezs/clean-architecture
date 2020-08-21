package com.acme.reservation.persistence;

import com.acme.reservation.persistence.model.ReservationPersistence;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ReservationRepository
    extends ReactiveCrudRepository<ReservationPersistence, String> {}
