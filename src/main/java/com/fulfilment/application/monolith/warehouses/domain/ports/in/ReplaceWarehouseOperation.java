package com.fulfilment.application.monolith.warehouses.domain.ports.in;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;

public interface ReplaceWarehouseOperation {
  Warehouse replace(Warehouse warehouse);
}
