package com.fulfilment.application.monolith.warehouses.adapters.database;

class EntityIsNotFoundException extends RuntimeException {
  EntityIsNotFoundException(String entityName, String searchString) {
    super("Object of the type [" + entityName + "] with " + searchString + " is not found");
  }
}
