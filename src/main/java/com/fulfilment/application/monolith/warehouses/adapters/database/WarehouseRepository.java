package com.fulfilment.application.monolith.warehouses.adapters.database;

import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.out.WarehouseStore;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class WarehouseRepository implements WarehouseStore, PanacheRepository<DbWarehouse> {

  @Inject private DbWarehouseMapper dbWarehouseMapper;

  @Override
  @Transactional
  public List<Warehouse> getAllActive() {
    return this.find("archivedAt is null").stream().map(dbWarehouseMapper::toDomain).toList();
  }

  @Override
  @Transactional
  public Warehouse create(Warehouse warehouse) {
    DbWarehouse dbWarehouse = dbWarehouseMapper.fromDomain(warehouse);
    dbWarehouse.createdAt(LocalDateTime.now());
    this.persist(dbWarehouse);
    return dbWarehouseMapper.toDomain(dbWarehouse);
  }

  @Override
  @Transactional
  public Warehouse update(Warehouse warehouse) {
    // Check that the warehouse exists
    DbWarehouse dbWarehouse = findDbWarehouseOrThrow(warehouse.id());
    dbWarehouseMapper.fromDomainToExistingInstance(warehouse, dbWarehouse);
    this.persist(dbWarehouse);
    return dbWarehouseMapper.toDomain(dbWarehouse);
  }

  @Override
  @Transactional
  public void remove(Warehouse warehouse) {
    DbWarehouse dbWarehouse = findDbWarehouseOrThrow(warehouse.id());
    this.delete(dbWarehouse);
  }

  @Override
  @Transactional
  public Optional<Warehouse> findByBusinessUnitCode(String buCode) {
    return this.find("businessUnitCode", buCode).firstResultOptional()
            .map(dbWarehouseMapper::toDomain);
  }

  @Override
  @Transactional
  public Optional<Warehouse> getById(Long id) {
    return this.findByIdOptional(id).map(dbWarehouseMapper::toDomain);
  }

  @Override
  public List<Warehouse> findByLocation(Location location) {
    return this.find("location", location.identification).stream()
            .map(dbWarehouseMapper::toDomain)
            .toList();
  }

  private DbWarehouse findDbWarehouseOrThrow(Long id) {
    return this.findByIdOptional(id)
            .orElseThrow(() -> new EntityIsNotFoundException("Warehouse", "[id = " + id + "]"));
  }
}
