package com.acme.reservation.application.usecases.search.acme_team;

import com.acme.reservation.application.request.SearchReservationFilter;
import com.acme.reservation.application.response.ReservationListingAcmeTeam;
import reactor.core.publisher.Flux;

public interface SearchReservationAcmeTeamUseCase {
  Flux<ReservationListingAcmeTeam> searchReservations(
      SearchReservationFilter searchReservationFilter);
}
