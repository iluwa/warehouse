package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.ClearDatabase;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.out.WarehouseStore;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
public class CreateWarehouseUseCaseTest {
  @Inject private WarehouseStore warehouseStore;
  @Inject private CreateWarehouseUseCase createWarehouseUseCase;
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
  void testWhenActiveWarehouseExistsThenException() {
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
    assertThrows(DomainExceptions.BusinessCodeAlreadyExistsException.class,
            () -> createWarehouseUseCase.create(base));
  }

  @Test
  void testWhenLocationNotExistThenException() {
    // given
    Warehouse base = new Warehouse(
            null,
            "code",
            "non-existing-location",
            1,
            1,
            null,
            null
    );

    // then
    assertThrows(DomainExceptions.LocationNotExistException.class,
            () -> createWarehouseUseCase.create(base));
  }

  @Test
  void testWhenMaximumNumberOfWarehousesExceededThenException() {
    // given
    warehouseStore.create(new Warehouse(
            null,
            "code",
            "ZWOLLE-001",
            1,
            1,
            null,
            null
    ));

    // then - second warehouse at ZWOLLE-001 is not allowed
    Warehouse warehouse = new Warehouse(
            null,
            "code2",
            "ZWOLLE-001",
            1,
            1,
            null,
            null
    );
    assertThrows(DomainExceptions.NoMoreWarehousesAtLocationAllowedException.class,
            () -> createWarehouseUseCase.create(warehouse));
  }

  @Test
  void testWhenMaximumCapacityExceededThenException() {
    // then - maximum capacity at ZWOLLE-001 is 40
    Warehouse warehouse = new Warehouse(
            null,
            "code2",
            "ZWOLLE-001",
            41,
            1,
            null,
            null
    );
    assertThrows(DomainExceptions.NoMoreWarehousesAtLocationAllowedException.class,
            () -> createWarehouseUseCase.create(warehouse));
  }

  @Test
  void testCreationSuccess() {
    // when
    Warehouse warehouse = new Warehouse(
            null,
            "code2",
            "ZWOLLE-001",
            40,
            1,
            null,
            null
    );
    Warehouse savedWarehouse = createWarehouseUseCase.create(warehouse);

    // then
    assertEquals("code2", savedWarehouse.businessUnitCode());
    assertEquals("ZWOLLE-001", savedWarehouse.location());
    assertEquals(40, savedWarehouse.capacity());
    assertEquals(1, savedWarehouse.stock());
    assertNotNull(savedWarehouse.createdAt());
    assertNull(savedWarehouse.archivedAt());
  }
}
