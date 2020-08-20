package com.acme.reservation.entity;

import java.time.ZoneId;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

@Data
public class Destination extends SelfValidating<Destination> {

  @NotNull(message = "the destination must have an id")
  @Getter
  private Long id;

  @NotNull(message = "the destination must have a name")
  @Getter
  private String name;

  @NotNull(message = "the destination must have a time zone")
  @Getter
  private ZoneId timeZone;
}
