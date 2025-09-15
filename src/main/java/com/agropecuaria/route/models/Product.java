package com.agropecuaria.route.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Representa um produto agropecuário com informações de deterioração
 */
public class Product {
    
    @NotBlank
    @JsonProperty("name")
    private String name;
    
    @NotBlank
    @JsonProperty("type")
    private String type;
    
    @NotNull
    @Positive
    @JsonProperty("quantity")
    private Double quantity;
    
    @NotBlank
    @JsonProperty("unit")
    private String unit; // kg, litros, unidades, etc.
    
    @NotNull
    @Positive
    @JsonProperty("maxDeliveryTimeMinutes")
    private Integer maxDeliveryTimeMinutes; // Tempo máximo em minutos antes de estragar
    
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("temperature")
    private String temperature; // refrigerado, ambiente, congelado
    
    public Product() {}
    
    public Product(String name, String type, Double quantity, String unit, Integer maxDeliveryTimeMinutes) {
        this.name = name;
        this.type = type;
        this.quantity = quantity;
        this.unit = unit;
        this.maxDeliveryTimeMinutes = maxDeliveryTimeMinutes;
    }
    
    // Getters and Setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public Double getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }
    
    public String getUnit() {
        return unit;
    }
    
    public void setUnit(String unit) {
        this.unit = unit;
    }
    
    public Integer getMaxDeliveryTimeMinutes() {
        return maxDeliveryTimeMinutes;
    }
    
    public void setMaxDeliveryTimeMinutes(Integer maxDeliveryTimeMinutes) {
        this.maxDeliveryTimeMinutes = maxDeliveryTimeMinutes;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getTemperature() {
        return temperature;
    }
    
    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
    
    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", quantity=" + quantity +
                ", unit='" + unit + '\'' +
                ", maxDeliveryTimeMinutes=" + maxDeliveryTimeMinutes +
                ", description='" + description + '\'' +
                ", temperature='" + temperature + '\'' +
                '}';
    }
}