package com.test.presentation.rest;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

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
}