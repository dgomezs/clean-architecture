package com.acme.reservation.application.usecases.search.acme_team;

import com.acme.reservation.application.repository.SearchReservationRepository;
import com.acme.reservation.application.request.SearchReservationFilter;
import com.acme.reservation.application.response.ReservationListingAcmeTeam;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;

@AllArgsConstructor
public class SearchReservationAcmeTeamUseCaseImpl implements SearchReservationAcmeTeamUseCase {

  private final SearchReservationRepository searchReservationRepository;

  @Override
  public Flux<ReservationListingAcmeTeam> searchReservations(
      SearchReservationFilter searchReservationFilter) {
    return searchReservationRepository.searchReservations(searchReservationFilter);
  }
}
