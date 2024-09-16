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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
public class ArchiveWarehouseUseCaseTest {
    @Inject private ArchiveWarehouseUseCase archiveWarehouseUseCase;
    @Inject private WarehouseStore warehouseStore;
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
    void testWhenTheCurrentIsArchivedThenException() {
        // given
        Warehouse warehouse = warehouseStore.create(new Warehouse(
                null,
                "code",
                "ZWOLLE-001",
                1,
                1,
                null,
                LocalDateTime.now()
        ));

        // then
        assertThrows(DomainExceptions.WarehouseAlreadyArchivedException.class,
                () -> archiveWarehouseUseCase.archive(warehouse));
    }

    @Test
    void testArchiveSuccess() {
        // given
        Warehouse warehouse = warehouseStore.create(new Warehouse(
                null,
                "code",
                "ZWOLLE-001",
                1,
                1,
                null,
                null
        ));

        // when
        archiveWarehouseUseCase.archive(warehouse);

        // then
        Warehouse archived = warehouseStore.getById(warehouse.id()).get();
        assertNotNull(archived.archivedAt());
    }
}
