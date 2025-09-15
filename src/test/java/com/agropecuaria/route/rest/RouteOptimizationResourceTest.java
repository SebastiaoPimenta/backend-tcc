package com.agropecuaria.route.rest;

import com.agropecuaria.route.models.*;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class RouteOptimizationResourceTest {
    
    @Test
    void testOptimizeRouteEmptyDeliveries() {
        // Arrange
        Location startLocation = new Location(-23.5505, -46.6333, "São Paulo, SP");
        
        RouteOptimizationRequest request = new RouteOptimizationRequest();
        request.setStartLocation(startLocation);
        request.setDeliveries(Arrays.asList());
        
        // Act & Assert
        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/api/route-optimization/optimize")
        .then()
            .statusCode(400)
            .body("title", equalTo("Constraint Violation"))
            .body("violations[0].field", equalTo("optimizeRoute.request.deliveries"))
            .body("violations[0].message", equalTo("must not be empty"));
    }
    
    @Test
    void testOptimizeRouteMissingStartLocation() {
        // Arrange
        Product product1 = new Product("Leite", "Lácteo", 100.0, "litros", 720); // 12 horas
        Delivery delivery1 = new Delivery(
            new Location(-22.9068, -43.1729, "Rio de Janeiro, RJ"),
            Arrays.asList(product1),
            "Cliente 1"
        );
        
        RouteOptimizationRequest request = new RouteOptimizationRequest();
        request.setStartLocation(null);
        request.setDeliveries(Arrays.asList(delivery1));
        
        // Act & Assert
        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/api/route-optimization/optimize")
        .then()
            .statusCode(400)
            .body("title", equalTo("Constraint Violation"))
            .body("violations[0].field", equalTo("optimizeRoute.request.startLocation"))
            .body("violations[0].message", equalTo("must not be null"));
    }
    
    @Test
    void testGetServiceInfo() {
        given()
        .when()
            .get("/api/route-optimization/info")
        .then()
            .statusCode(200)
            .body("serviceName", equalTo("Route Optimization Service"))
            .body("version", equalTo("1.0.0"))
            .body("algorithm", equalTo("Jenetics - Genetic Algorithm"))
            .body("status", equalTo("Active"));
    }
}