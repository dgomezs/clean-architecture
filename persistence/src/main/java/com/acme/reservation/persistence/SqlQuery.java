package com.acme.reservation.persistence;

public class SqlQuery {
  private SqlQuery() {}

  public static String reservationQuery =
      "SELECT * FROM reservation  r "
          + "JOIN customer c ON c.id = r.customer_id  "
          + "JOIN destination d ON d.id = r.destination_id";
}
