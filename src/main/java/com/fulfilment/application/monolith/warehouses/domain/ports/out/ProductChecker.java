package com.fulfilment.application.monolith.warehouses.domain.ports.out;

import java.util.List;

public interface ProductChecker {
  ProductCheckResult check(List<String> productNames);
  enum ProductCheckResult {
    OK,
    ERROR
  }
}
