package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.out.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.out.ProductChecker;
import com.fulfilment.application.monolith.warehouses.domain.ports.out.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class BaseValidation {
  private static final int MAX_PRODUCT_AMOUNT = 5;
  @Inject private WarehouseStore warehouseStore;
  @Inject private LocationResolver locationResolver;
  @Inject private ProductChecker productChecker;

  void checkExistsByBuCode(Warehouse warehouse) {
    Optional<Warehouse> existingByBuCode = warehouseStore.findByBusinessUnitCode(warehouse.businessUnitCode())
            .filter(w -> w.archivedAt() == null);
    if (existingByBuCode.isPresent()) {
      throw new DomainExceptions.BusinessCodeAlreadyExistsException(warehouse.businessUnitCode());
    }
  }

  void checkProductsExist(List<String> productNames) {
    if (productNames.isEmpty()) {
      return;
    }

    ProductChecker.ProductCheckResult res = productChecker.check(productNames);
    if (res == ProductChecker.ProductCheckResult.ERROR) {
      throw new DomainExceptions.ProductNotExistException();
    }
  }

  void checkProductLimit(Warehouse warehouse) {
    if (warehouse.products().size() > MAX_PRODUCT_AMOUNT) {
      throw new DomainExceptions.WarehouseReachedProductLimitException();
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
