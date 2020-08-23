package com.acme.reservation.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.r2dbc.core.DatabaseClient.GenericExecuteSpec;

public class MultiQuery {

  private final String initialSql;
  private final List<String> conditions = new ArrayList<>();
  private final Map<String, Object> params = new HashMap<>();

  public MultiQuery(String initialSql) {
    this.initialSql = initialSql;
  }

  public GenericExecuteSpec bindParams(DatabaseClient databaseClient) {
    String extraSql = conditions.size() > 0 ? " WHERE " + String.join(" AND ", conditions) : "";
    GenericExecuteSpec result = databaseClient.execute(initialSql + extraSql);
    // TODO find a nicer way to compose the binding
    for (Map.Entry<String, Object> e : params.entrySet()) {
      result = result.bind(e.getKey(), e.getValue());
    }
    return result;
  }

  public void addSqlCondition(String sql) {
    this.conditions.add(sql);
  }

  public void addParamMapping(String param, Object value) {
    this.params.put(param, value);
  }
}
