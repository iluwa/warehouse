package com.fulfilment.application.monolith.warehouses.adapters.database;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "warehouse")
@Cacheable
public class DbWarehouse {

  @Id @GeneratedValue private Long id;

  private String businessUnitCode;

  private String location;

  private Integer capacity;

  private Integer stock;

  private LocalDateTime createdAt;

  private LocalDateTime archivedAt;

  public String businessUnitCode() {
    return businessUnitCode;
  }

  public void businessUnitCode(String businessUnitCode) {
    this.businessUnitCode = businessUnitCode;
  }

  public String location() {
    return location;
  }

  public void location(String location) {
    this.location = location;
  }

  public Integer capacity() {
    return capacity;
  }

  public void capacity(Integer capacity) {
    this.capacity = capacity;
  }

  public Integer stock() {
    return stock;
  }

  public void stock(Integer stock) {
    this.stock = stock;
  }

  public LocalDateTime createdAt() {
    return createdAt;
  }

  public void createdAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime archivedAt() {
    return archivedAt;
  }

  public void archivedAt(LocalDateTime archivedAt) {
    this.archivedAt = archivedAt;
  }

  public Long id() {
    return id;
  }

  public void id(Long id) {
    this.id = id;
  }
}
