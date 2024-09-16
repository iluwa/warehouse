package com.fulfilment.application.monolith.warehouses.adapters.database;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DbWarehouseMapper {
  public Warehouse toDomain(DbWarehouse dbWarehouse) {
    return new Warehouse(
            dbWarehouse.id(),
            dbWarehouse.businessUnitCode(),
            dbWarehouse.location(),
            dbWarehouse.capacity(),
            dbWarehouse.stock(),
            dbWarehouse.createdAt(),
            dbWarehouse.archivedAt(),
            dbWarehouse.products()
    );
  }

  public DbWarehouse fromDomain(Warehouse warehouse) {
    DbWarehouse dbWarehouse = new DbWarehouse();
    dbWarehouse.id(warehouse.id());
    dbWarehouse.businessUnitCode(warehouse.businessUnitCode());
    dbWarehouse.archivedAt(warehouse.archivedAt());
    dbWarehouse.createdAt(warehouse.createdAt());
    dbWarehouse.stock(warehouse.stock());
    dbWarehouse.capacity(warehouse.capacity());
    dbWarehouse.location(warehouse.location());
    dbWarehouse.products(warehouse.products());
    return dbWarehouse;
  }

  public void fromDomainToExistingInstance(Warehouse warehouse, DbWarehouse dbWarehouse) {
    dbWarehouse.id(warehouse.id());
    dbWarehouse.businessUnitCode(warehouse.businessUnitCode());
    dbWarehouse.archivedAt(warehouse.archivedAt());
    dbWarehouse.createdAt(warehouse.createdAt());
    dbWarehouse.stock(warehouse.stock());
    dbWarehouse.capacity(warehouse.capacity());
    dbWarehouse.location(warehouse.location());
  }
}
