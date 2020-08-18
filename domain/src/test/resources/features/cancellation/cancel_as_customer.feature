Feature: Cancellation of an existing reservation

  Scenario Outline: Cancellation by a customer
    Given that the customer has a reservation starting on <ReservationStartDate> and ending on <ReservationEndDate>
    And that the reservation is for <TotalReservationPrice>
    And that the reservation has a cancellation policy <CancellationPolicy>
    When the customer cancels the reservation on <ReservationCancellationTime>
    Then the customer should receive <TotalRefundedMoney>

    Examples:
      | ReservationStartDate | ReservationEndDate | TotalReservationPrice | CancellationPolicy | ReservationCancellationTime | TotalRefundedMoney |
      | 2020-08-01           | 2020-08-05       | 100                   | FLEXI              | 2020-07-01                  | 50                 |