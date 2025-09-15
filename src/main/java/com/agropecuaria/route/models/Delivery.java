package com.agropecuaria.route.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Representa uma entrega com localização e produtos
 */
public class Delivery {
    
    @NotNull
    @Valid
    @JsonProperty("location")
    private Location location;
    
    @NotEmpty
    @Valid
    @JsonProperty("products")
    private List<Product> products;
    
    @JsonProperty("customerName")
    private String customerName;
    
    @JsonProperty("customerPhone")
    private String customerPhone;
    
    @JsonProperty("deliveryNotes")
    private String deliveryNotes;
    
    @JsonProperty("priority")
    private Integer priority; // 1 = alta, 2 = média, 3 = baixa
    
    public Delivery() {}
    
    public Delivery(Location location, List<Product> products) {
        this.location = location;
        this.products = products;
    }
    
    public Delivery(Location location, List<Product> products, String customerName) {
        this.location = location;
        this.products = products;
        this.customerName = customerName;
    }
    
    /**
     * Calcula o tempo máximo de entrega baseado no produto que estraga mais rápido
     */
    public Integer getMinDeliveryTimeLimit() {
        return products.stream()
                .mapToInt(Product::getMaxDeliveryTimeMinutes)
                .min()
                .orElse(Integer.MAX_VALUE);
    }
    
    // Getters and Setters
    public Location getLocation() {
        return location;
    }
    
    public void setLocation(Location location) {
        this.location = location;
    }
    
    public List<Product> getProducts() {
        return products;
    }
    
    public void setProducts(List<Product> products) {
        this.products = products;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public String getCustomerPhone() {
        return customerPhone;
    }
    
    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }
    
    public String getDeliveryNotes() {
        return deliveryNotes;
    }
    
    public void setDeliveryNotes(String deliveryNotes) {
        this.deliveryNotes = deliveryNotes;
    }
    
    public Integer getPriority() {
        return priority;
    }
    
    public void setPriority(Integer priority) {
        this.priority = priority;
    }
    
    @Override
    public String toString() {
        return "Delivery{" +
                "location=" + location +
                ", products=" + products +
                ", customerName='" + customerName + '\'' +
                ", customerPhone='" + customerPhone + '\'' +
                ", deliveryNotes='" + deliveryNotes + '\'' +
                ", priority=" + priority +
                '}';
    }
}