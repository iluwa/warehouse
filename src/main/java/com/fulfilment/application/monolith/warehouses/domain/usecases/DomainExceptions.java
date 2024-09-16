package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;

public class DomainExceptions {
  public static class BusinessCodeAlreadyExistsException extends RuntimeException {
    public BusinessCodeAlreadyExistsException(String buCode) {
      super("The business code " + buCode + " already exists");
    }
  }

  public static class LocationNotExistException extends RuntimeException {
    public LocationNotExistException(String location) {
      super("The location '" + location + "' does not exist");
    }
  }

  public static class NoMoreWarehousesAtLocationAllowedException extends RuntimeException {
    public NoMoreWarehousesAtLocationAllowedException(Location location, String reason) {
      super("The location '" + location.identification + "' does not allow any more warehouses: " + reason);
    }
  }

  public static class DomainIsNotFoundException extends RuntimeException {
    DomainIsNotFoundException(String domainName, String searchString) {
      super("Object of the type [" + domainName + "] with " + searchString + " is not found");
    }
  }

  public static class WarehouseAlreadyArchivedException extends RuntimeException {
    public WarehouseAlreadyArchivedException(Warehouse warehouse) {
      super("Warehouse '" + warehouse.id() + "' is already archived");
    }
  }

  public static class WarehouseStockIsDifferentException extends RuntimeException {
    public WarehouseStockIsDifferentException(Warehouse warehouse) {
      super("New warehouse must have the same stock as the old one (id = " + warehouse.id() + ")");
    }
  }

  public static class WarehouseLocationIsDifferentException extends RuntimeException {
    public WarehouseLocationIsDifferentException(Warehouse warehouse) {
      super("A new warehouse must be placed at the same area as the old one (id = " + warehouse.id() + ")");
    }
  }
}
