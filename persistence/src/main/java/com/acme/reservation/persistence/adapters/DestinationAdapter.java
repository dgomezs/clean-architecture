package com.acme.reservation.persistence.adapters;

import com.acme.reservation.entity.Destination;
import com.acme.reservation.persistence.model.DestinationPersistence;
import java.time.ZoneId;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DestinationAdapter {

  DestinationPersistence toDestinationPersistence(Destination destination);

  Destination toDestination(DestinationPersistence destinationPersistence);

  default ZoneId toZoneId(String zoneId) {
    return ZoneId.of(zoneId);
  }

  default String toZoneId(ZoneId zoneId) {
    return zoneId.getId();
  }
}
