package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.in.ArchiveWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.out.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;

@ApplicationScoped
public class ArchiveWarehouseUseCase implements ArchiveWarehouseOperation {

  @Inject private WarehouseStore warehouseStore;
  @Inject private BaseValidation baseValidation;

  @Override
  @Transactional
  public void archive(Warehouse warehouse) {
    baseValidation.checkIfAlreadyArchived(warehouse);

    warehouse.archivedAt(LocalDateTime.now());
    warehouseStore.update(warehouse);
  }
}
