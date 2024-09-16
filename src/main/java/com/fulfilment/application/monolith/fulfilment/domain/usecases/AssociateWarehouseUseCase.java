package com.fulfilment.application.monolith.fulfilment.domain.usecases;

import com.fulfilment.application.monolith.fulfilment.domain.models.Fulfilment;
import com.fulfilment.application.monolith.fulfilment.domain.models.FulfilmentRepository;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.usecases.SearchWarehouseUseCase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class AssociateWarehouseUseCase {
  private static final int WAREHOUSE_STORE_LIMIT = 3;
  private static final int PRODUCT_STORE_LIMIT = 2;

  @Inject private FulfilmentRepository fulfilmentRepository;
  @Inject private SearchWarehouseUseCase searchWarehouseUseCase;
  @Inject private StoreProvider storeProvider;

  @Transactional
  public Fulfilment associate(Long warehouseId, String storeName, String productName) {
    // In ideal situation domain should not be exposed, but it should be a different class.
    // For now, it's faster to do like this
    Warehouse warehouse = searchWarehouseUseCase.getById(warehouseId);
    if (!warehouse.products().contains(productName)) {
      throw new ProductNotExistInWarehouseException();
    }

    storeProvider.getStoreByName(storeName).orElseThrow(StoreNotFoundException::new);

    List<Fulfilment> fulfilments = fulfilmentRepository.find("storeName", storeName).stream().toList();

    Set<Long> currentAssociatedWarehouses = fulfilments.stream().map(Fulfilment::warehouseId)
            .collect(Collectors.toSet());
    if (currentAssociatedWarehouses.size() >= WAREHOUSE_STORE_LIMIT) {
      throw new StoreHasMaximumAmountOfWarehousesException();
    }

    List<Fulfilment> productToStore = fulfilments.stream()
            .filter(fulfilment -> productName.equals(fulfilment.productName())).toList();
    if (productToStore.size() >= PRODUCT_STORE_LIMIT) {
      throw new StoreHasMaximumAmountOfProductsException();
    }

    Fulfilment fulfilment = new Fulfilment(warehouseId, storeName, productName);
    fulfilmentRepository.persist(fulfilment);
    return fulfilment;
  }

  public static final class StoreHasMaximumAmountOfWarehousesException extends RuntimeException {}

  public static final class StoreHasMaximumAmountOfProductsException extends RuntimeException {}

  public static final class ProductNotExistInWarehouseException extends RuntimeException {}

  public static final class StoreNotFoundException extends RuntimeException {}
}
