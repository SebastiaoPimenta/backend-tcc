package com.agropecuaria.route.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

class DeliveryTest {
    
    @Test
    void testGetMinDeliveryTimeLimit() {
        // Arrange
        Product product1 = new Product("Leite", "Lácteo", 100.0, "litros", 720);  // 12 horas
        Product product2 = new Product("Queijo", "Lácteo", 50.0, "kg", 1080);   // 18 horas
        Product product3 = new Product("Manteiga", "Lácteo", 25.0, "kg", 480);  // 8 horas
        
        List<Product> products = Arrays.asList(product1, product2, product3);
        
        Location location = new Location(-23.5505, -46.6333, "São Paulo, SP");
        Delivery delivery = new Delivery(location, products);
        
        // Act
        Integer minTime = delivery.getMinDeliveryTimeLimit();
        
        // Assert
        assertEquals(480, minTime); // Deve retornar o menor tempo (manteiga - 8 horas)
    }
    
    @Test
    void testGetMinDeliveryTimeLimitEmptyProducts() {
        // Arrange
        Location location = new Location(-23.5505, -46.6333, "São Paulo, SP");
        Delivery delivery = new Delivery(location, Arrays.asList());
        
        // Act
        Integer minTime = delivery.getMinDeliveryTimeLimit();
        
        // Assert
        assertEquals(Integer.MAX_VALUE, minTime);
    }
    
    @Test
    void testDeliveryConstructors() {
        // Arrange
        Location location = new Location(-23.5505, -46.6333, "São Paulo, SP");
        Product product = new Product("Leite", "Lácteo", 100.0, "litros", 720); // 12 horas
        List<Product> products = Arrays.asList(product);
        
        // Act
        Delivery delivery1 = new Delivery(location, products);
        Delivery delivery2 = new Delivery(location, products, "João Silva");
        
        // Assert
        assertNotNull(delivery1.getLocation());
        assertEquals(1, delivery1.getProducts().size());
        assertNull(delivery1.getCustomerName());
        
        assertNotNull(delivery2.getLocation());
        assertEquals(1, delivery2.getProducts().size());
        assertEquals("João Silva", delivery2.getCustomerName());
    }
}