package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.ClearDatabase;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.out.WarehouseStore;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
public class ReplaceWarehouseUseCaseTest {
  @Inject private WarehouseStore warehouseStore;
  @Inject private ReplaceWarehouseUseCase replaceWarehouseUseCase;
  @Inject private ClearDatabase clearDatabase;

  @BeforeEach
  void setUp() {
    clearDatabase.run(List.of("warehouse"));
  }

  @AfterEach
  void tearDown() {
    clearDatabase.run(List.of("warehouse"));
  }

  @Test
  void testWhenReplacingNonExistingWarehouseThenException() {
    Warehouse base = new Warehouse(
            null,
            "code",
            "ZWOLLE-001",
            1,
            1,
            null,
            null
    );
    assertThrows(DomainExceptions.DomainIsNotFoundException.class,
            () -> replaceWarehouseUseCase.replace(base));
  }

  @Test
  void testWhenReplacingArchivedWarehouseThenException() {
    // given
    Warehouse archived = new Warehouse(
            null,
            "code",
            "ZWOLLE-001",
            1,
            1,
            null,
            LocalDateTime.now()
    );
    warehouseStore.create(archived);

    // then
    Warehouse warehouse = new Warehouse(
            null,
            "code",
            "ZWOLLE-001",
            1,
            1,
            null,
            null
    );
    assertThrows(DomainExceptions.WarehouseAlreadyArchivedException.class,
            () -> replaceWarehouseUseCase.replace(warehouse));
  }

  @Test
  void testWhenReplacingWithAnotherLocationThenException() {
    // given
    Warehouse base = new Warehouse(
            null,
            "code",
            "ZWOLLE-001",
            1,
            1,
            null,
            null
    );
    warehouseStore.create(base);

    // then
    Warehouse warehouse = new Warehouse(
            null,
            "code",
            "ZWOLLE-002",
            1,
            1,
            null,
            null
    );
    assertThrows(DomainExceptions.WarehouseLocationIsDifferentException.class,
            () -> replaceWarehouseUseCase.replace(warehouse));
  }

  @Test
  void testWhenReplacingWithAnotherStockThenException() {
    // given
    Warehouse base = new Warehouse(
            null,
            "code",
            "ZWOLLE-001",
            1,
            1,
            null,
            null
    );
    warehouseStore.create(base);

    // then
    Warehouse warehouse = new Warehouse(
            null,
            "code",
            "ZWOLLE-001",
            1,
            2,
            null,
            null
    );
    assertThrows(DomainExceptions.WarehouseStockIsDifferentException.class,
            () -> replaceWarehouseUseCase.replace(warehouse));
  }

  @Test
  void testWhenReplacingWithHigherCapacityAboveTheLimitThenException() {
    // given
    Warehouse base = new Warehouse(
            null,
            "code",
            "ZWOLLE-001",
            1,
            1,
            null,
            null
    );
    warehouseStore.create(base);

    // then
    Warehouse warehouse = new Warehouse(
            null,
            "code",
            "ZWOLLE-001",
            41,
            1,
            null,
            null
    );
    assertThrows(DomainExceptions.NoMoreWarehousesAtLocationAllowedException.class,
            () -> replaceWarehouseUseCase.replace(warehouse));
  }

  @Test
  void testReplacementSuccess() {
    // given
    Warehouse base = new Warehouse(
            null,
            "code",
            "ZWOLLE-001",
            1,
            1,
            null,
            null
    );
    base = warehouseStore.create(base);

    // when
    Warehouse warehouse = new Warehouse(
            null,
            "code",
            "ZWOLLE-001",
            40,
            1,
            null,
            null
    );
    Warehouse savedWarehouse = replaceWarehouseUseCase.replace(warehouse);

    // then
    assertEquals("code", savedWarehouse.businessUnitCode());
    assertEquals("ZWOLLE-001", savedWarehouse.location());
    assertEquals(40, savedWarehouse.capacity());
    assertEquals(1, savedWarehouse.stock());
    assertNotNull(savedWarehouse.createdAt());
    assertNull(savedWarehouse.archivedAt());

    Warehouse archived = warehouseStore.getById(base.id()).get();
    assertNotNull(archived.archivedAt());
  }
}
