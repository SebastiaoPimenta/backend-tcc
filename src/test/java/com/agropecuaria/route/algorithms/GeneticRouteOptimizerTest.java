package com.agropecuaria.route.algorithms;

import com.agropecuaria.route.models.*;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class GeneticRouteOptimizerTest {
    
    @Inject
    GeneticRouteOptimizer routeOptimizer;
    
    @Test
    void testOptimizeRouteWithTwoDeliveries() {
        // Arrange
        Location startLocation = new Location(-23.5505, -46.6333, "São Paulo, SP");
        
        Product product1 = new Product("Leite", "Lácteo", 100.0, "litros", 720); // 12 horas
        Delivery delivery1 = new Delivery(
            new Location(-22.9068, -43.1729, "Rio de Janeiro, RJ"),
            Arrays.asList(product1),
            "Cliente 1"
        );
        
        Product product2 = new Product("Queijo", "Lácteo", 50.0, "kg", 1440); // 24 horas
        Delivery delivery2 = new Delivery(
            new Location(-19.9191, -43.9386, "Belo Horizonte, MG"),
            Arrays.asList(product2),
            "Cliente 2"
        );
        
        RouteOptimizationRequest request = new RouteOptimizationRequest();
        request.setStartLocation(startLocation);
        request.setDeliveries(Arrays.asList(delivery1, delivery2));
        
        // Act
        RouteOptimizationResponse response = routeOptimizer.optimizeRoute(request);
        
        // Assert
        assertNotNull(response);
        assertNotNull(response.getOptimizedRoute());
        assertEquals(2, response.getOptimizedRoute().size());
        assertTrue(response.getTotalDistanceKm() > 0);
        assertTrue(response.getTotalTimeMinutes() > 0);
        assertTrue(response.getAlgorithmExecutionTimeMs() > 0);
    }
    
    @Test
    void testOptimizeRouteWithInfeasibleDelivery() {
        // Arrange
        Location startLocation = new Location(-23.5505, -46.6333, "São Paulo, SP");
        
        Product product1 = new Product("Leite Fresco", "Lácteo", 100.0, "litros", 120); // 2 horas apenas
        Delivery delivery1 = new Delivery(
            new Location(-22.9068, -43.1729, "Rio de Janeiro, RJ"), // ~6 horas de viagem
            Arrays.asList(product1),
            "Cliente 1"
        );
        
        RouteOptimizationRequest request = new RouteOptimizationRequest();
        request.setStartLocation(startLocation);
        request.setDeliveries(Arrays.asList(delivery1));
        
        // Act
        RouteOptimizationResponse response = routeOptimizer.optimizeRoute(request);
        
        // Assert
        assertNotNull(response);
        assertFalse(response.getFeasible());
        assertNotNull(response.getInfeasibleDeliveries());
        assertTrue(response.getInfeasibleDeliveries().contains(0));
    }
    
    @Test
    void testOptimizeRouteWithSingleDelivery() {
        // Arrange
        Location startLocation = new Location(-23.5505, -46.6333, "São Paulo, SP");
        
        Product product1 = new Product("Queijo", "Lácteo", 50.0, "kg", 720); // 12 horas
        Delivery delivery1 = new Delivery(
            new Location(-23.5000, -46.6000, "Perto de São Paulo, SP"),
            Arrays.asList(product1),
            "Cliente 1"
        );
        
        RouteOptimizationRequest request = new RouteOptimizationRequest();
        request.setStartLocation(startLocation);
        request.setDeliveries(Arrays.asList(delivery1));
        
        // Act
        RouteOptimizationResponse response = routeOptimizer.optimizeRoute(request);
        
        // Assert
        assertNotNull(response);
        assertEquals(1, response.getOptimizedRoute().size());
        // O algoritmo pode retornar 0 ou 1 dependendo da implementação
        assertTrue(response.getOptimizedRoute().get(0) >= 0);
        assertTrue(response.getFeasible());
        assertTrue(response.getTotalDistanceKm() > 0);
        assertTrue(response.getTotalTimeMinutes() > 0);
    }
}