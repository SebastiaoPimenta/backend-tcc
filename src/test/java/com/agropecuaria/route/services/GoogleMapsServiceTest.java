package com.agropecuaria.route.services;

import com.agropecuaria.route.models.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GoogleMapsServiceTest {
    
    @InjectMocks
    GoogleMapsService googleMapsService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    void testCalculateHaversineDistance() {
        // Arrange
        Location saoPaulo = new Location(-23.5505, -46.6333, "São Paulo, SP");
        Location rioDeJaneiro = new Location(-22.9068, -43.1729, "Rio de Janeiro, RJ");
        Location brasilia = new Location(-15.7942, -47.8822, "Brasília, DF");
        
        List<Location> locations = Arrays.asList(saoPaulo, rioDeJaneiro, brasilia);
        
        // Act
        double[][] distanceMatrix = googleMapsService.calculateDistanceMatrix(locations);
        
        // Assert
        assertNotNull(distanceMatrix);
        assertEquals(3, distanceMatrix.length);
        assertEquals(3, distanceMatrix[0].length);
        
        // Distância de uma cidade para ela mesma deve ser 0
        assertEquals(0.0, distanceMatrix[0][0]);
        assertEquals(0.0, distanceMatrix[1][1]);
        assertEquals(0.0, distanceMatrix[2][2]);
        
        // Distâncias devem ser simétricas (aproximadamente)
        assertTrue(Math.abs(distanceMatrix[0][1] - distanceMatrix[1][0]) < 1.0);
        assertTrue(Math.abs(distanceMatrix[0][2] - distanceMatrix[2][0]) < 1.0);
        assertTrue(Math.abs(distanceMatrix[1][2] - distanceMatrix[2][1]) < 1.0);
        
        // Distâncias devem ser positivas para cidades diferentes
        assertTrue(distanceMatrix[0][1] > 0);
        assertTrue(distanceMatrix[0][2] > 0);
        assertTrue(distanceMatrix[1][2] > 0);
    }
    
    @Test
    void testCalculateRouteTime() {
        // Arrange
        double[][] timeMatrix = {
            {0, 60, 90},  // São Paulo -> Rio: 60min, SP -> Brasília: 90min
            {60, 0, 75},  // Rio -> SP: 60min, Rio -> Brasília: 75min
            {90, 75, 0}   // Brasília -> SP: 90min, Brasília -> Rio: 75min
        };
        
        List<Integer> route = Arrays.asList(0, 1, 2, 0); // SP -> Rio -> Brasília -> SP
        int loadingTime = 15;
        
        // Act
        double totalTime = googleMapsService.calculateRouteTime(route, timeMatrix, loadingTime);
        
        // Assert
        // Tempo esperado: 60 + 15 + 75 + 15 + 90 = 255 minutos
        assertEquals(255.0, totalTime, 0.1);
    }
    
    @Test
    void testCalculateRouteDistance() {
        // Arrange
        Location saoPaulo = new Location(-23.5505, -46.6333, "São Paulo, SP");
        Location rioDeJaneiro = new Location(-22.9068, -43.1729, "Rio de Janeiro, RJ");
        Location brasilia = new Location(-15.7942, -47.8822, "Brasília, DF");
        
        List<Location> locations = Arrays.asList(saoPaulo, rioDeJaneiro, brasilia);
        List<Integer> route = Arrays.asList(0, 1, 2, 0);
        
        // Act
        double totalDistance = googleMapsService.calculateRouteDistance(route, locations);
        
        // Assert
        assertTrue(totalDistance > 0);
        assertTrue(totalDistance > 1000); // Deve ser mais de 1000 km para essa rota
        assertTrue(totalDistance < 3000); // Mas menos de 3000 km
    }
    
    @Test
    void testCalculateRouteTimeEmptyRoute() {
        // Arrange
        double[][] timeMatrix = {{0}};
        List<Integer> route = Arrays.asList(0);
        
        // Act
        double totalTime = googleMapsService.calculateRouteTime(route, timeMatrix, 15);
        
        // Assert
        assertEquals(0.0, totalTime);
    }
}