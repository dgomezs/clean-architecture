package com.acme.reservation.entity;

import java.time.ZoneId;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Destination extends SelfValidating<Destination> {

  @NotNull(message = "the destination must have an id")
  @Getter
  private final Long id;

  @NotNull(message = "the destination must have a name")
  @Getter
  private final String name;

  @NotNull(message = "the destination must have a time zone")
  @Getter
  private final ZoneId timeZone;
}
