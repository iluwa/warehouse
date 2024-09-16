package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.in.CreateWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.out.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class CreateWarehouseUseCase implements CreateWarehouseOperation {

  @Inject private WarehouseStore warehouseStore;
  @Inject private BaseValidation baseValidation;

  @Override
  @Transactional
  public Warehouse create(Warehouse warehouse) {
    baseValidation.checkExistsByBuCode(warehouse);
    Location location = baseValidation.checkLocationIdentifier(warehouse);
    checkLocationLimits(location, warehouse);
    return warehouseStore.create(warehouse);
  }

  private void checkLocationLimits(Location location, Warehouse warehouse) {
    List<Warehouse> existingWarehouses = warehouseStore.findByLocation(location);
    if (existingWarehouses.size() + 1 > location.maxNumberOfWarehouses) {
      throw new DomainExceptions.NoMoreWarehousesAtLocationAllowedException(location, "it reached its warehouse limit");
    }

    int existingCapacity = existingWarehouses.stream().map(Warehouse::capacity)
            .mapToInt(Integer::intValue).sum();
    if (existingCapacity + warehouse.capacity() > location.maxCapacity) {
      throw new DomainExceptions.NoMoreWarehousesAtLocationAllowedException(location, "it reached its capacity limit");
    }
  }
}
