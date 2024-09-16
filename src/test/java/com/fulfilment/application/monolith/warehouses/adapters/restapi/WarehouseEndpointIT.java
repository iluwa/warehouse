package com.fulfilment.application.monolith.warehouses.adapters.restapi;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@QuarkusIntegrationTest
public class WarehouseEndpointIT {

  @Test
  void testWhenNotFoundThen404() {
    final String path = "warehouse";

    given()
            .when()
            .get(path + "/8888")
            .then()
            .statusCode(404);
  }

  @Test
  @Disabled("Fix api-spec generator to return correct code")
  void testWhenCreatedThen201() {
    final String path = "warehouse";

    given()
            .contentType("application/json")
            .body("""
                    {
                      "businessUnitCode": "5454",
                      "location": "AMSTERDAM-001",
                      "capacity": 10,
                      "stock": 4
                    }
                    """)
            .when()
            .post(path)
            .then()
            .statusCode(201);
  }

  @Test
  public void testSimpleCheckingArchivingWarehouses() {
    final String path = "warehouse";

    // List all, should have all 6 warehouses in the database:
    given()
            .when()
            .get(path)
            .then()
            .statusCode(200)
            .body(
                    containsString("MWH.001"),
                    containsString("MWH.012"),
                    containsString("MWH.023"),
                    containsString("ZWOLLE-001"),
                    containsString("AMSTERDAM-001"),
                    containsString("TILBURG-001"));

    // Archive the ZWOLLE-001:
    given().when().delete(path + "/1").then().statusCode(204);

    // List all, ZWOLLE-001 should be missing now:
    given()
            .when()
            .get(path)
            .then()
            .statusCode(200)
            .body(
                    not(containsString("ZWOLLE-001")),
                    containsString("AMSTERDAM-001"),
                    containsString("TILBURG-001"));
  }
}
