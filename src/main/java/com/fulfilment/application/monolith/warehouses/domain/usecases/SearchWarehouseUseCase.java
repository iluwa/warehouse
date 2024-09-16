package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.in.SearchWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.out.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class SearchWarehouseUseCase implements SearchWarehouseOperation {
  @Inject private WarehouseStore warehouseStore;

  @Override
  public Warehouse findByBusinessUnitCode(String buCode) {
    return warehouseStore.findByBusinessUnitCode(buCode)
            .orElseThrow(() -> new DomainExceptions.DomainIsNotFoundException(
                    "Warehouse", "[businessUnitCode = " + buCode + "]"));
  }

  @Override
  public Warehouse getById(Long id) {
    return warehouseStore.getById(id)
            .orElseThrow(() -> new DomainExceptions.DomainIsNotFoundException(
                    "Warehouse", "[id = " + id + "]"));
  }

  @Override
  public List<Warehouse> getAllActive() {
    return warehouseStore.getAllActive();
  }
}
