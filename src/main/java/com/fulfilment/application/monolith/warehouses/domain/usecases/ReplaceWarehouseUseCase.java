package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.in.ReplaceWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.out.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.out.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ReplaceWarehouseUseCase implements ReplaceWarehouseOperation {

  @Inject private WarehouseStore warehouseStore;
  @Inject private BaseValidation baseValidation;
  @Inject private LocationResolver locationResolver;

  @Override
  @Transactional
  public Warehouse replace(Warehouse newWarehouse) {
    Optional<Warehouse> existingByBuCodeOpt = warehouseStore.findByBusinessUnitCode(newWarehouse.businessUnitCode());
    if (existingByBuCodeOpt.isEmpty()) {
      throw new DomainExceptions.DomainIsNotFoundException(
              "Warehouse", "[businessUnitCode = " + newWarehouse.businessUnitCode() + "]");
    }

    Warehouse currentWarehouse = existingByBuCodeOpt.get();
    baseValidation.checkIfAlreadyArchived(currentWarehouse);

    if (!currentWarehouse.location().equals(newWarehouse.location())) {
      throw new DomainExceptions.WarehouseLocationIsDifferentException(currentWarehouse);
    }

    if (!currentWarehouse.stock().equals(newWarehouse.stock())) {
      throw new DomainExceptions.WarehouseStockIsDifferentException(currentWarehouse);
    }

    Location newLocation = baseValidation.checkLocationIdentifier(newWarehouse);
    validateLocationCapacity(currentWarehouse, newWarehouse, newLocation);

    baseValidation.checkProductLimit(newWarehouse);
    baseValidation.checkProductsExist(newWarehouse.products());

    currentWarehouse.archivedAt(LocalDateTime.now());
    warehouseStore.update(currentWarehouse);

    return warehouseStore.create(newWarehouse);
  }

  private void validateLocationCapacity(Warehouse currentWarehouse, Warehouse newWarehouse,
                                        Location location) {
    int capacityDiff = newWarehouse.capacity() - currentWarehouse.capacity();
    List<Warehouse> existingWarehouses = warehouseStore.findByLocation(location);
    int existingCapacity = existingWarehouses.stream().map(Warehouse::capacity)
            .mapToInt(Integer::intValue).sum();
    if (existingCapacity + capacityDiff > location.maxCapacity) {
      throw new DomainExceptions.NoMoreWarehousesAtLocationAllowedException(location, "it reached its capacity limit");
    }
  }
}
