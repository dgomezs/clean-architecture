package com.acme.reservation.persistence;

import com.acme.reservation.application.repository.SearchReservationRepository;
import com.acme.reservation.application.request.SearchReservationFilter;
import com.acme.reservation.application.response.ReservationListingAcmeTeam;
import com.acme.reservation.application.response.ReservationListingCustomer;
import com.acme.reservation.entity.ReservationStatus;
import com.acme.reservation.persistence.adapters.ReservationAdapter;
import com.acme.reservation.persistence.model.ReservationRow;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.r2dbc.core.DatabaseClient.GenericExecuteSpec;
import reactor.core.publisher.Flux;

@AllArgsConstructor
public class SearchReservationRepositoryImpl implements SearchReservationRepository {

  private final DatabaseClient databaseClient;
  private final ReservationAdapter reservationAdapter;

  public Flux<ReservationListingAcmeTeam> searchReservations(
      SearchReservationFilter searchReservationFilter) {

    Flux<ReservationRow> reservations =
        buildQuery(searchReservationFilter).as(ReservationRow.class).fetch().all();
    return reservations.map(reservationAdapter::toReservationListingAcmeTeam);
  }

  private GenericExecuteSpec buildQuery(SearchReservationFilter searchReservationFilter) {

    MultiQuery query = new MultiQuery(SqlQuery.reservationQuery);

    searchReservationFilter
        .getCustomerId()
        .ifPresent(customerId -> addCustomerQuery(customerId).accept((query)));
    if (searchReservationFilter.hasStatuses()) {
      addStatusQuery(searchReservationFilter.getStatusList()).accept(query);
    }
    return query.bindParams(databaseClient);
  }

  public Flux<ReservationListingCustomer> searchReservationsForCustomers(Long customerId) {

    val searchReservationFilter = new SearchReservationFilter();
    searchReservationFilter.setCustomerId(customerId);
    GenericExecuteSpec genericExecuteSpec = buildQuery(searchReservationFilter);

    Flux<ReservationRow> reservations = genericExecuteSpec.as(ReservationRow.class).fetch().all();
    return reservations.map(reservationAdapter::toReservationListingCustomer);
  }

  private Consumer<MultiQuery> addCustomerQuery(Long customerId) {
    return q -> {
      q.addParamMapping("$1", customerId);
      q.addSqlCondition(" c.id = $1 ");
    };
  }

  private Consumer<MultiQuery> addStatusQuery(List<ReservationStatus> statusList) {
    return q -> {
      q.addParamMapping("$1", statusList.stream().map(Enum::name).collect(Collectors.joining(",")));
      q.addSqlCondition(" r.status IN ($1) ");
    };
  }
}
