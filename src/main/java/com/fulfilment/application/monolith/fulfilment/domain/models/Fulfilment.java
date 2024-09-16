package com.fulfilment.application.monolith.fulfilment.domain.models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;

@Entity
@Cacheable
public final class Fulfilment extends PanacheEntity {
  private Long warehouseId;

  // Since the field is unique it can be used as a key
  private String storeName;

  // Since the field is unique it can be used as a key
  private String productName;

  public Fulfilment() {
  }

  public Fulfilment(Long warehouseId, String storeName, String productName) {
    this.warehouseId = warehouseId;
    this.storeName = storeName;
    this.productName = productName;
  }

  public Long warehouseId() {
    return warehouseId;
  }

  public String storeName() {
    return storeName;
  }

  public String productName() {
    return productName;
  }
}
