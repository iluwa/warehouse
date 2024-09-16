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

@QuarkusTest
public class SearchWarehouseUseCaseTest {
  @Inject private WarehouseStore warehouseStore;
  @Inject private SearchWarehouseUseCase searchWarehouseUseCase;
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
  void testGetAllActive() {
    // given
    Warehouse active = warehouseStore.create(new Warehouse(
            null,
            "code",
            "ZWOLLE-001",
            1,
            1,
            null,
            null
    ));

    Warehouse archived = warehouseStore.create(new Warehouse(
            null,
            "code",
            "ZWOLLE-001",
            1,
            1,
            null,
            LocalDateTime.now()
    ));

    // when
    List<Warehouse> allActive = searchWarehouseUseCase.getAllActive();

    // then
    assertEquals(1, allActive.size());
    assertEquals(active.id(), allActive.get(0).id());
  }
}
