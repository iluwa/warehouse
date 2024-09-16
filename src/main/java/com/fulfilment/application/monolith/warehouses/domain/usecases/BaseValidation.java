package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.out.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.out.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class BaseValidation {
  @Inject private WarehouseStore warehouseStore;
  @Inject private LocationResolver locationResolver;

  void checkExistsByBuCode(Warehouse warehouse) {
    Optional<Warehouse> existingByBuCode = warehouseStore.findByBusinessUnitCode(warehouse.businessUnitCode())
            .filter(w -> w.archivedAt() == null);
    if (existingByBuCode.isPresent()) {
      throw new DomainExceptions.BusinessCodeAlreadyExistsException(warehouse.businessUnitCode());
    }
  }

  Location checkLocationIdentifier(Warehouse warehouse) {
    return locationResolver.resolveByIdentifier(warehouse.location())
            .orElseThrow(() -> new DomainExceptions.LocationNotExistException(warehouse.location()));
  }

  void checkIfAlreadyArchived(Warehouse warehouse) {
    if (warehouse.archivedAt() != null) {
      throw new DomainExceptions.WarehouseAlreadyArchivedException(warehouse);
    }
  }
}
