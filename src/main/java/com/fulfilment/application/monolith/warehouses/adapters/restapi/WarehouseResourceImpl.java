package com.fulfilment.application.monolith.warehouses.adapters.restapi;

import com.fulfilment.application.monolith.warehouses.domain.ports.in.ArchiveWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.in.CreateWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.in.ReplaceWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.in.SearchWarehouseOperation;
import com.warehouse.api.WarehouseResource;
import com.warehouse.api.beans.Warehouse;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@RequestScoped
public class WarehouseResourceImpl implements WarehouseResource {

  @Inject private ApiWarehouseMapper apiWarehouseMapper;
  @Inject private SearchWarehouseOperation searchWarehouseOperation;
  @Inject private CreateWarehouseOperation createWarehouseOperation;
  @Inject private ArchiveWarehouseOperation archiveWarehouseOperation;
  @Inject private ReplaceWarehouseOperation replaceWarehouseOperation;

  @Override
  public List<Warehouse> listAllWarehousesUnits() {
    return searchWarehouseOperation.getAllActive().stream().map(apiWarehouseMapper::fromDomain).toList();
  }

  @Override
  public Warehouse createANewWarehouseUnit(@NotNull Warehouse data) {
    var newWarehouse = apiWarehouseMapper.toDomain(data);
    var savedWarehouse = createWarehouseOperation.create(newWarehouse);
    return apiWarehouseMapper.fromDomain(savedWarehouse);
  }

  @Override
  public Warehouse getAWarehouseUnitByID(String id) {
    var warehouse = searchWarehouseOperation.findByBusinessUnitCode(id);
    return apiWarehouseMapper.fromDomain(warehouse);
  }

  @Override
  public void archiveAWarehouseUnitByID(String id) {
    var warehouse = searchWarehouseOperation.getById(Long.parseLong(id));
    archiveWarehouseOperation.archive(warehouse);
  }

  @Override
  public Warehouse replaceTheCurrentActiveWarehouse(
          String businessUnitCode, @NotNull Warehouse data) {

    var newWarehouse = apiWarehouseMapper.toDomain(data);
    var savedWarehouse = replaceWarehouseOperation.replace(newWarehouse);
    return apiWarehouseMapper.fromDomain(savedWarehouse);
  }
}
