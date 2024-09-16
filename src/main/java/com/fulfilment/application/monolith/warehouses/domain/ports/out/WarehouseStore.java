package com.fulfilment.application.monolith.warehouses.domain.ports.out;

import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;

import java.util.List;
import java.util.Optional;

public interface WarehouseStore {

  List<Warehouse> getAllActive();

  Warehouse create(Warehouse warehouse);

  Warehouse update(Warehouse warehouse);

  void remove(Warehouse warehouse);

  Optional<Warehouse> findByBusinessUnitCode(String buCode);

  Optional<Warehouse> getById(Long id);

  List<Warehouse> findByLocation(Location location);
}
