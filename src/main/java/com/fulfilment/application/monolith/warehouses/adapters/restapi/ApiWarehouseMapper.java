package com.fulfilment.application.monolith.warehouses.adapters.restapi;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
class ApiWarehouseMapper {
  public com.warehouse.api.beans.Warehouse fromDomain(Warehouse warehouse) {
    var apiWarehouse = new com.warehouse.api.beans.Warehouse();
    apiWarehouse.setId(String.valueOf(warehouse.id()));
    apiWarehouse.setBusinessUnitCode(warehouse.businessUnitCode());
    apiWarehouse.setCapacity(warehouse.capacity());
    apiWarehouse.setLocation(warehouse.location());
    apiWarehouse.setStock(warehouse.stock());
    return apiWarehouse;
  }

  public Warehouse toDomain(com.warehouse.api.beans.Warehouse apiWarehouse) {
    return new Warehouse(
            apiWarehouse.getId() != null ? Long.parseLong(apiWarehouse.getId()) : null,
            apiWarehouse.getBusinessUnitCode(),
            apiWarehouse.getLocation(),
            apiWarehouse.getCapacity(),
            apiWarehouse.getStock(),
            null,
            null
    );
  }
}
