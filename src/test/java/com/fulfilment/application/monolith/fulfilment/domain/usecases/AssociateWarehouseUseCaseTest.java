package com.fulfilment.application.monolith.fulfilment.domain.usecases;

import com.fulfilment.application.monolith.ClearDatabase;
import com.fulfilment.application.monolith.fulfilment.domain.models.Fulfilment;
import com.fulfilment.application.monolith.fulfilment.domain.models.FulfilmentRepository;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.out.WarehouseStore;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
class AssociateWarehouseUseCaseTest {
  @Inject private AssociateWarehouseUseCase associateWarehouseUseCase;
  @Inject private FulfilmentRepository fulfilmentRepository;
  @Inject private WarehouseStore warehouseStore;
  @Inject private ClearDatabase clearDatabase;

  @BeforeEach
  void setUp() {
    clearDatabase.run(List.of("fulfilment", "warehouse"));
  }

  @AfterEach
  void tearDown() {
    clearDatabase.run(List.of("fulfilment", "warehouse"));
  }

  @Test
  void testAssociateProductThatIsNotInWarehouseThenException() {
    // given
    Warehouse warehouse = warehouseStore.create(new Warehouse(
            null,
            "code2",
            "ZWOLLE-001",
            1,
            1,
            null,
            null
    ));

    assertThrows(AssociateWarehouseUseCase.ProductNotExistInWarehouseException.class,
            () -> associateWarehouseUseCase.associate(warehouse.id(), "TONSTAD", "TONSTAD"));
  }

  @Test
  void testWhenStoreAlreadyHasWarehouseLimitThenException() {
    // given
    List<Warehouse> warehouses = Stream.of(1, 2, 3)
            .map(i -> warehouseStore.create(new Warehouse(
                    null,
                    "code" + i,
                    "ZWOLLE-001",
                    1,
                    1,
                    null,
                    null,
                    List.of("TONSTAD", "KALLAX", "BESTÅ")
            )))
            .toList();

    // Link three different warehouses to TONSTAD
    warehouses.forEach(warehouse -> fulfilmentRepository.persist(
            new Fulfilment(warehouse.id(), "TONSTAD", "TONSTAD"))
    );

    // when
    var warehouse = warehouseStore.create(new Warehouse(
            null,
            "code44",
            "ZWOLLE-001",
            1,
            1,
            null,
            null,
            List.of("TONSTAD", "KALLAX", "BESTÅ")
    ));

    assertThrows(AssociateWarehouseUseCase.StoreHasMaximumAmountOfWarehousesException.class,
            () -> associateWarehouseUseCase.associate(warehouse.id(), "TONSTAD", "TONSTAD"));
  }

  @Test
  void testWhenProductToStoreAlreadyHasLimitThenException() {
    // given
    List<Warehouse> warehouses = Stream.of(1, 2)
            .map(i -> warehouseStore.create(new Warehouse(
                    null,
                    "code" + i,
                    "ZWOLLE-001",
                    1,
                    1,
                    null,
                    null,
                    List.of("TONSTAD")
            )))
            .toList();

    // Link TONSTAD twice from different warehouses
    warehouses.forEach(warehouse -> fulfilmentRepository.persist(
            new Fulfilment(warehouse.id(), "TONSTAD", "TONSTAD"))
    );

    // when
    var warehouse = warehouseStore.create(new Warehouse(
            null,
            "code44",
            "ZWOLLE-001",
            1,
            1,
            null,
            null,
            List.of("TONSTAD")
    ));
    assertThrows(AssociateWarehouseUseCase.StoreHasMaximumAmountOfProductsException.class,
            () -> associateWarehouseUseCase.associate(warehouse.id(), "TONSTAD", "TONSTAD"));
  }

  @Test
  void testAssociationSuccess() {
    // given
    Warehouse warehouse = warehouseStore.create(new Warehouse(
            null,
            "code2",
            "ZWOLLE-001",
            1,
            1,
            null,
            null,
            List.of("KALLAX")
    ));

    // when
    Fulfilment fulfilment = associateWarehouseUseCase.associate(
            warehouse.id(), "TONSTAD", "KALLAX");

    // then
    assertEquals(warehouse.id(), fulfilment.warehouseId());
    assertEquals("TONSTAD", fulfilment.storeName());
    assertEquals("KALLAX", fulfilment.productName());
  }
}