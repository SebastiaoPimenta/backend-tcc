package com.agropecuaria.route.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Resposta da otimização de rota
 */
public class RouteOptimizationResponse {
    
    @JsonProperty("optimizedRoute")
    private List<Integer> optimizedRoute; // Índices das entregas na ordem otimizada
    
    @JsonProperty("deliveryOrder")
    private List<Delivery> deliveryOrder; // Entregas na ordem otimizada
    
    @JsonProperty("totalDistanceKm")
    private Double totalDistanceKm;
    
    @JsonProperty("totalTimeMinutes")
    private Integer totalTimeMinutes;
    
    @JsonProperty("feasible")
    private Boolean feasible; // Se é possível entregar todos os produtos sem estragar
    
    @JsonProperty("infeasibleDeliveries")
    private List<Integer> infeasibleDeliveries; // Índices das entregas que não podem ser realizadas
    
    @JsonProperty("message")
    private String message;
    
    @JsonProperty("algorithmExecutionTimeMs")
    private Long algorithmExecutionTimeMs;
    
    public RouteOptimizationResponse() {}
    
    public RouteOptimizationResponse(List<Integer> optimizedRoute, List<Delivery> deliveryOrder, 
                                   Double totalDistanceKm, Integer totalTimeMinutes, Boolean feasible) {
        this.optimizedRoute = optimizedRoute;
        this.deliveryOrder = deliveryOrder;
        this.totalDistanceKm = totalDistanceKm;
        this.totalTimeMinutes = totalTimeMinutes;
        this.feasible = feasible;
    }
    
    // Getters and Setters
    public List<Integer> getOptimizedRoute() {
        return optimizedRoute;
    }
    
    public void setOptimizedRoute(List<Integer> optimizedRoute) {
        this.optimizedRoute = optimizedRoute;
    }
    
    public List<Delivery> getDeliveryOrder() {
        return deliveryOrder;
    }
    
    public void setDeliveryOrder(List<Delivery> deliveryOrder) {
        this.deliveryOrder = deliveryOrder;
    }
    
    public Double getTotalDistanceKm() {
        return totalDistanceKm;
    }
    
    public void setTotalDistanceKm(Double totalDistanceKm) {
        this.totalDistanceKm = totalDistanceKm;
    }
    
    public Integer getTotalTimeMinutes() {
        return totalTimeMinutes;
    }
    
    public void setTotalTimeMinutes(Integer totalTimeMinutes) {
        this.totalTimeMinutes = totalTimeMinutes;
    }
    
    public Boolean getFeasible() {
        return feasible;
    }
    
    public void setFeasible(Boolean feasible) {
        this.feasible = feasible;
    }
    
    public List<Integer> getInfeasibleDeliveries() {
        return infeasibleDeliveries;
    }
    
    public void setInfeasibleDeliveries(List<Integer> infeasibleDeliveries) {
        this.infeasibleDeliveries = infeasibleDeliveries;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public Long getAlgorithmExecutionTimeMs() {
        return algorithmExecutionTimeMs;
    }
    
    public void setAlgorithmExecutionTimeMs(Long algorithmExecutionTimeMs) {
        this.algorithmExecutionTimeMs = algorithmExecutionTimeMs;
    }
    
    @Override
    public String toString() {
        return "RouteOptimizationResponse{" +
                "optimizedRoute=" + optimizedRoute +
                ", deliveryOrder=" + deliveryOrder +
                ", totalDistanceKm=" + totalDistanceKm +
                ", totalTimeMinutes=" + totalTimeMinutes +
                ", feasible=" + feasible +
                ", infeasibleDeliveries=" + infeasibleDeliveries +
                ", message='" + message + '\'' +
                ", algorithmExecutionTimeMs=" + algorithmExecutionTimeMs +
                '}';
    }
}