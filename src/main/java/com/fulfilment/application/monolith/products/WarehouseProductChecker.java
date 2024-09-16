package com.fulfilment.application.monolith.products;

import com.fulfilment.application.monolith.warehouses.domain.ports.out.ProductChecker;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class WarehouseProductChecker implements ProductChecker {
  @Inject private ProductRepository productRepository;

  @Override
  public ProductCheckResult check(List<String> productNames) {
    List<Product> res = productRepository.find("name in (?1)", productNames).stream().toList();
    return res.size() == productNames.size()
            ? ProductCheckResult.OK
            : ProductCheckResult.ERROR;
  }
}
