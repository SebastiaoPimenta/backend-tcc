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
    void testOptimizeRouteWithSpoilageValidation() {
        // Arrange - Usando dados do example-spoilage-test.json
        Location startLocation = new Location(-15.7942, -47.8822, "Setor Comercial Sul, Brasília, DF");

        // Produtos com tempos de deterioração críticos
        Product leiteFresco = new Product("Leite fresco", "DAIRY", 50.0, "litros", 30); // 30 minutos - muito crítico
        Product banana = new Product("Banana", "FRUIT", 40.0, "kg", 30); // 30 minutos - crítico
        Product alface = new Product("Alface", "VEGETABLE", 30.0, "kg", 30); // 30 minutos - crítico

        // Entregas em diferentes pontos de Brasília
        Delivery delivery1 = new Delivery(
            new Location(-15.7801, -47.9292, "Taguatinga Norte, DF"),
            Arrays.asList(leiteFresco),
            "Cliente Taguatinga"
        );

        Delivery delivery2 = new Delivery(
            new Location(-15.8331, -48.0368, "Ceilândia Norte, DF"),
            Arrays.asList(banana),
            "Cliente Ceilândia"
        );

        Delivery delivery3 = new Delivery(
            new Location(-15.7217, -47.8825, "Asa Norte, Brasília, DF"),
            Arrays.asList(alface),
            "Cliente Asa Norte"
        );

        RouteOptimizationRequest request = new RouteOptimizationRequest();
        request.setStartLocation(startLocation);
        request.setDeliveries(Arrays.asList(delivery1, delivery2, delivery3));

        // Act
        var response = given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/api/route-optimization/optimize")
        .then()
            .statusCode(200)
            .body("optimizedRoute", notNullValue())
            .body("totalDistanceKm", greaterThan(0.0f))
            .body("totalTimeMinutes", greaterThan(0)) // Corrigido para Integer
            .body("feasible", is(true))
            .body("optimizedRoute", hasSize(5)) // Deve retornar todas as 3 entregas
            .body("message", containsString("Alguns produtos podem estragar durante a entrega"));
        System.out.println("Resposta recebida:\n" + response.toString());
    }
    
    @Test
    void testOptimizeRouteWithCriticalSpoilageTime() {
        // Arrange - Teste específico para produtos com tempo de deterioração muito baixo
        Location startLocation = new Location(-15.7942, -47.8822, "Setor Comercial Sul, Brasília, DF");

        // Produto extremamente perecível - 10 minutos
        Product produtoMuitoCritico = new Product("Leite orgânico fresco", "DAIRY", 25.0, "litros", 10);
        
        Delivery delivery = new Delivery(
            new Location(-15.7801, -47.9292, "Taguatinga Norte, DF"),
            Arrays.asList(produtoMuitoCritico),
            "Cliente Crítico"
        );
        
        RouteOptimizationRequest request = new RouteOptimizationRequest();
        request.setStartLocation(startLocation);
        request.setDeliveries(Arrays.asList(delivery));
        
        // Act & Assert - Espera que falhe por ser inviável (422)
        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/api/route-optimization/optimize")
        .then()
            .statusCode(422) // Status correto para rota inviável
            .body("feasible", is(false))
            .body("message", containsString("Não é possível entregar"));
    }

    @Test
    void testOptimizeRouteWithViableSpoilageTime() {
        // Arrange - Teste com produto perecível mas com tempo viável
        Location startLocation = new Location(-15.7942, -47.8822, "Setor Comercial Sul, Brasília, DF");
        
        // Produto com tempo razoável - 2 horas
        Product produtoViavel = new Product("Banana madura", "FRUIT", 30.0, "kg", 120);
        
        Delivery delivery = new Delivery(
            new Location(-15.7801, -47.9292, "Taguatinga Norte, DF"),
            Arrays.asList(produtoViavel),
            "Cliente Próximo"
        );
        
        RouteOptimizationRequest request = new RouteOptimizationRequest();
        request.setStartLocation(startLocation);
        request.setDeliveries(Arrays.asList(delivery));
        
        // Act & Assert
        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/api/route-optimization/optimize")
        .then()
            .statusCode(200)
            .body("feasible", is(true))
            .body("optimizedRoute", contains(0, 1, 0))
            .body("totalTimeMinutes", greaterThan(0))
            .body("totalDistanceKm", greaterThan(0.0f));
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