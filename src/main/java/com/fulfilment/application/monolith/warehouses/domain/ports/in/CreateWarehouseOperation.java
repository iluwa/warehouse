package com.fulfilment.application.monolith.warehouses.domain.ports.in;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;

public interface CreateWarehouseOperation {
  Warehouse create(Warehouse warehouse);
}
