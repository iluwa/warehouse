package com.fulfilment.application.monolith.location;

import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class LocationGatewayTest {

  @Test
  public void testWhenResolveExistingLocationShouldReturn() {
    // given
    LocationGateway locationGateway = new LocationGateway();

    // when
    Location location = locationGateway.resolveByIdentifier("ZWOLLE-001").get();

    // then
    assertEquals("ZWOLLE-001", location.identification);
  }

  @Test
  public void testWhenResolveNotExistingLocationShouldReturnNull() {
    // given
    LocationGateway locationGateway = new LocationGateway();

    // when
    Optional<Location> location = locationGateway.resolveByIdentifier("non-existing-location");

    // then
    assertTrue(location.isEmpty());
  }
}
