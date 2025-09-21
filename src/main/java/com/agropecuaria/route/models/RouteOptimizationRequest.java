package com.agropecuaria.route.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Requisição para otimização de rota
 */
public class RouteOptimizationRequest {
    
    @NotNull
    @Valid
    @JsonProperty("startLocation")
    private Location startLocation; // Localização inicial/final (fazenda/casa do produtor)
    
    @NotEmpty
    @Valid
    @JsonProperty("deliveries")
    private List<Delivery> deliveries; // Lista de entregas a serem realizadas
    
    @JsonProperty("vehicleSpeedKmH")
    private Double vehicleSpeedKmH = 60.0; // Velocidade média do veículo em km/h
    
    @JsonProperty("loadingTimeMinutes")
    private Integer loadingTimeMinutes = 0; // Tempo de carregamento/descarregamento por entrega
    
    @JsonProperty("useGoogleMaps")
    private Boolean useGoogleMaps = true; // Se deve usar Google Maps API para distâncias reais
    
    public RouteOptimizationRequest() {}
    
    public RouteOptimizationRequest(Location startLocation, List<Delivery> deliveries) {
        this.startLocation = startLocation;
        this.deliveries = deliveries;
    }
    
    // Getters and Setters
    public Location getStartLocation() {
        return startLocation;
    }
    
    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }
    
    public List<Delivery> getDeliveries() {
        return deliveries;
    }
    
    public void setDeliveries(List<Delivery> deliveries) {
        this.deliveries = deliveries;
    }
    
    public Double getVehicleSpeedKmH() {
        return vehicleSpeedKmH;
    }
    
    public void setVehicleSpeedKmH(Double vehicleSpeedKmH) {
        this.vehicleSpeedKmH = vehicleSpeedKmH;
    }
    
    public Integer getLoadingTimeMinutes() {
        return loadingTimeMinutes;
    }
    
    public void setLoadingTimeMinutes(Integer loadingTimeMinutes) {
        this.loadingTimeMinutes = loadingTimeMinutes;
    }
    
    public Boolean getUseGoogleMaps() {
        return useGoogleMaps;
    }
    
    public void setUseGoogleMaps(Boolean useGoogleMaps) {
        this.useGoogleMaps = useGoogleMaps;
    }
    
    @Override
    public String toString() {
        return "RouteOptimizationRequest{" +
                "startLocation=" + startLocation +
                ", deliveries=" + deliveries +
                ", vehicleSpeedKmH=" + vehicleSpeedKmH +
                ", loadingTimeMinutes=" + loadingTimeMinutes +
                ", useGoogleMaps=" + useGoogleMaps +
                '}';
    }
}