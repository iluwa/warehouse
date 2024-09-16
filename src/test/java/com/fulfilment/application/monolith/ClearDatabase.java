package com.fulfilment.application.monolith;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class ClearDatabase {
  private static final List<String> TABLE_NAMES = List.of(
          "warehouse",
          "product",
          "store"
  );
  @Inject private EntityManager entityManager;

  @Transactional
  public void run() {
    TABLE_NAMES.forEach(tableName ->
            entityManager.createNativeQuery("DELETE FROM " + tableName)
                    .executeUpdate());
  }

  @Transactional
  public void run(List<String> tableNames) {
    tableNames.forEach(tableName ->
            entityManager.createNativeQuery("DELETE FROM " + tableName)
                    .executeUpdate());
  }
}
