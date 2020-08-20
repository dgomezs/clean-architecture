Feature: Cancellation of an existing reservation

  Scenario Outline: Cancellation by a customer
    Given that the customer has the following reservation
      | ReservationStartDate | ReservationEndDate | TotalReservationPrice | CancellationPolicy | DestinationTimeZone |
      | 2020-08-01 09:00     | 2020-08-05 15:00   | 100                   | FLEX               | Europe/Zurich       |

    When the customer cancels the reservation on <ReservationCancellationTime> in <ReservationCancellationTimeZone>
    Then the customer should receive <TotalRefundedMoney>

    Examples:
      | ReservationCancellationTime | ReservationCancellationTimeZone | TotalRefundedMoney |
      | 2020-07-01 08:00            | Europe/Zurich                   | 100                |
