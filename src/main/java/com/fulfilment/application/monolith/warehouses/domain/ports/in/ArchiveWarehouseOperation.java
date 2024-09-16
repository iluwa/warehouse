package com.fulfilment.application.monolith.warehouses.domain.ports.in;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;

public interface ArchiveWarehouseOperation {
  void archive(Warehouse warehouse);
}
