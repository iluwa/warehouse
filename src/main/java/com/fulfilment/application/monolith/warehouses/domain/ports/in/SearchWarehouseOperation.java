package com.fulfilment.application.monolith.warehouses.domain.ports.in;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;

import java.util.List;

public interface SearchWarehouseOperation {
  Warehouse findByBusinessUnitCode(String buCode);

  Warehouse getById(Long id);

  List<Warehouse> getAllActive();
}
