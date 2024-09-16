package com.fulfilment.application.monolith.warehouses.domain.models;

import jakarta.annotation.Nullable;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public final class Warehouse {
  @Nullable private final Long id;
  private final String businessUnitCode;
  private final String location;
  private final Integer capacity;
  private final Integer stock;
  @Nullable private final LocalDateTime createdAt;
  @Nullable private LocalDateTime archivedAt;
  private final List<String> products;

  public Warehouse(
          @Nullable Long id,
          String businessUnitCode,
          String location,
          Integer capacity,
          Integer stock,
          @Nullable LocalDateTime createdAt,
          @Nullable LocalDateTime archivedAt
  ) {
    this(id, businessUnitCode, location, capacity, stock, createdAt, archivedAt, Collections.emptyList());
  }

  public Warehouse(
          @Nullable Long id,
          String businessUnitCode,
          String location,
          Integer capacity,
          Integer stock,
          @Nullable LocalDateTime createdAt,
          @Nullable LocalDateTime archivedAt,
          List<String> products
  ) {
    this.id = id;
    this.businessUnitCode = businessUnitCode;
    this.location = location;
    this.capacity = capacity;
    this.stock = stock;
    this.createdAt = createdAt;
    this.archivedAt = archivedAt;
    this.products = products;
  }

  @Nullable
  public Long id() {
    return id;
  }

  public String businessUnitCode() {
    return businessUnitCode;
  }

  public String location() {
    return location;
  }

  public Integer capacity() {
    return capacity;
  }

  public Integer stock() {
    return stock;
  }

  @Nullable
  public LocalDateTime createdAt() {
    return createdAt;
  }

  @Nullable
  public LocalDateTime archivedAt() {
    return archivedAt;
  }

  public void archivedAt(LocalDateTime archivedAt) {
    this.archivedAt = archivedAt;
  }

  public List<String> products() {
    return products;
  }
}
