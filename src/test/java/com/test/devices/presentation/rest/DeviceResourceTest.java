package com.test.devices.presentation.rest;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

@QuarkusTest
class DeviceResourceTest {
    @Test
    void shouldCreateDeviceSuccessfully() {
        // GIVEN - A valid create request
        String requestBody = """
                {
                    "name": "iPhone 15",
                    "brand": "Apple",
                    "state": "AVAILABLE"
                }
                """;

        // WHEN/THEN - Creating device returns 201
        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/v1/devices")
                .then()
                .statusCode(201)
                .body("name", equalTo("iPhone 15"))
                .body("brand", equalTo("Apple"))
                .body("state", equalTo("AVAILABLE"))
                .body("id", notNullValue())
                .body("creationTime", notNullValue());
    }

    @Test
    void shouldReturnBadRequestWhenCreatingDeviceWithMissingFields() {
        // GIVEN - Invalid request (missing required fields)
        String requestBody = """
            {
                "name": ""
            }
            """;

        // WHEN/THEN - Returns 400
        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/v1/devices")
                .then()
                .statusCode(400);
    }

    @Test
    void shouldGetDeviceById() {
        // GIVEN - A created device
        String createBody = """
            {
                "name": "Galaxy S24",
                "brand": "Samsung",
                "state": "AVAILABLE"
            }
            """;

        Integer deviceId = given()
                .contentType(ContentType.JSON)
                .body(createBody)
                .when()
                .post("/api/v1/devices")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        // WHEN/THEN - Getting device by ID returns 200
        given()
                .pathParam("id", deviceId)
                .when()
                .get("/api/v1/devices/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(deviceId))
                .body("name", equalTo("Galaxy S24"))
                .body("brand", equalTo("Samsung"));
    }

    @Test
    void shouldReturnNotFoundForNonExistentDevice() {
        // GIVEN - Non-existent device ID

        // WHEN/THEN - Returns 404
        given()
                .pathParam("id", 99999)
                .when()
                .get("/api/v1/devices/{id}")
                .then()
                .statusCode(404)
                .body("message", containsString("Device not found"));
    }


    @Test
    void shouldUpdateDeviceState() {
        // GIVEN - A created device
        String createBody = """
            {
                "name": "iPad Pro",
                "brand": "Apple",
                "state": "AVAILABLE"
            }
            """;

        Integer deviceId = given()
                .contentType(ContentType.JSON)
                .body(createBody)
                .when()
                .post("/api/v1/devices")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        // WHEN - Updating device state
        String updateBody = """
            {
                "state": "IN_USE"
            }
            """;

        // THEN - Device is updated
        given()
                .contentType(ContentType.JSON)
                .pathParam("id", deviceId)
                .body(updateBody)
                .when()
                .put("/api/v1/devices/{id}")
                .then()
                .statusCode(200)
                .body("state", equalTo("IN_USE"));
    }

    @Test
    void shouldNotUpdateNameWhenDeviceInUse() {
        // GIVEN - A device in use
        String createBody = """
            {
                "name": "MacBook Pro",
                "brand": "Apple",
                "state": "IN_USE"
            }
            """;

        Integer deviceId = given()
                .contentType(ContentType.JSON)
                .body(createBody)
                .when()
                .post("/api/v1/devices")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        // WHEN - Trying to update name
        String updateBody = """
            {
                "name": "MacBook Air"
            }
            """;

        // THEN - Returns 400
        given()
                .contentType(ContentType.JSON)
                .pathParam("id", deviceId)
                .body(updateBody)
                .when()
                .put("/api/v1/devices/{id}")
                .then()
                .statusCode(400)
                .body("message", containsString("Cannot update"));
    }

    @Test
    void shouldListAllDevices() {
        // GIVEN - Multiple devices
        given().contentType(ContentType.JSON).body("""
            {"name": "Device 1", "brand": "Brand A", "state": "AVAILABLE"}
            """).post("/api/v1/devices");

        given().contentType(ContentType.JSON).body("""
            {"name": "Device 2", "brand": "Brand B", "state": "IN_USE"}
            """).post("/api/v1/devices");

        // WHEN/THEN - Listing devices returns array
        given()
                .when()
                .get("/api/v1/devices")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(2));
    }

    @Test
    void shouldFilterDevicesByBrand() {
        // GIVEN - Devices with different brands
        given().contentType(ContentType.JSON).body("""
            {"name": "iPhone", "brand": "Apple", "state": "AVAILABLE"}
            """).post("/api/v1/devices");

        given().contentType(ContentType.JSON).body("""
            {"name": "Galaxy", "brand": "Samsung", "state": "AVAILABLE"}
            """).post("/api/v1/devices");

        // WHEN/THEN - Filtering by brand works
        given()
                .queryParam("brand", "Apple")
                .when()
                .get("/api/v1/devices")
                .then()
                .statusCode(200)
                .body("findAll { it.brand == 'Apple' }.size()", greaterThanOrEqualTo(1));
    }

    @Test
    void shouldDeleteDevice() {
        // GIVEN - A device not in use
        String createBody = """
            {
                "name": "Test Device",
                "brand": "Test Brand",
                "state": "AVAILABLE"
            }
            """;

        Integer deviceId = given()
                .contentType(ContentType.JSON)
                .body(createBody)
                .when()
                .post("/api/v1/devices")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        // WHEN - Deleting device
        given()
                .pathParam("id", deviceId)
                .when()
                .delete("/api/v1/devices/{id}")
                .then()
                .statusCode(204);

        // THEN - Device no longer exists
        given()
                .pathParam("id", deviceId)
                .when()
                .get("/api/v1/devices/{id}")
                .then()
                .statusCode(404);
    }

    @Test
    void shouldNotDeleteDeviceInUse() {
        // GIVEN - A device in use
        String createBody = """
            {
                "name": "Active Device",
                "brand": "Brand",
                "state": "IN_USE"
            }
            """;

        Integer deviceId = given()
                .contentType(ContentType.JSON)
                .body(createBody)
                .when()
                .post("/api/v1/devices")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        // WHEN/THEN - Delete returns 400
        given()
                .pathParam("id", deviceId)
                .when()
                .delete("/api/v1/devices/{id}")
                .then()
                .statusCode(400)
                .body("message", containsString("Cannot delete"));
    }
}