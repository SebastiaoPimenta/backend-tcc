package com.agropecuaria.route.controllers;

import com.agropecuaria.route.algorithms.GeneticRouteOptimizer;
import com.agropecuaria.route.models.RouteOptimizationRequest;
import com.agropecuaria.route.models.RouteOptimizationResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

/**
 * Controller para lógica de negócio de otimização de rotas
 * Separação de responsabilidades entre REST endpoint e lógica de negócio
 */
@ApplicationScoped
public class RouteOptimizationController {
    
    private static final Logger LOG = Logger.getLogger(RouteOptimizationController.class);
    
    @Inject
    GeneticRouteOptimizer routeOptimizer;
    
    /**
     * Processa requisição de otimização de rota com validações de negócio
     */
    public RouteOptimizationResponse processOptimizationRequest(RouteOptimizationRequest request) {
        LOG.info("Processando requisição de otimização com " + 
                request.getDeliveries().size() + " entregas");
        
        // Validações de negócio específicas
        validateBusinessRules(request);
        
        // Logs para auditoria
        logRequestDetails(request);
        
        // Executa otimização
        RouteOptimizationResponse response = routeOptimizer.optimizeRoute(request);
        
        // Log do resultado
        logResponseDetails(response);
        
        return response;
    }
    
    /**
     * Valida regras de negócio específicas
     */
    private void validateBusinessRules(RouteOptimizationRequest request) {
        // Validação: máximo de entregas por rota
        if (request.getDeliveries().size() > 20) {
            throw new IllegalArgumentException(
                "Número máximo de entregas por rota excedido (máximo: 20)"
            );
        }
        
        // Validação: produtos devem ter tempo mínimo de 30 minutos
        request.getDeliveries().forEach(delivery -> {
            delivery.getProducts().forEach(product -> {
                if (product.getMaxDeliveryTimeMinutes() < 30) {
                    throw new IllegalArgumentException(
                        "Produto '" + product.getName() + 
                        "' tem tempo de entrega muito restritivo (mínimo: 30 minutos)"
                    );
                }
            });
        });
        
        // Validação: velocidade do veículo deve ser realista
        if (request.getVehicleSpeedKmH() > 120 || request.getVehicleSpeedKmH() < 20) {
            throw new IllegalArgumentException(
                "Velocidade do veículo deve estar entre 20 e 120 km/h"
            );
        }
    }
    
    /**
     * Log detalhes da requisição para auditoria
     */
    private void logRequestDetails(RouteOptimizationRequest request) {
        LOG.info("Detalhes da requisição:");
        LOG.info("- Localização inicial: " + request.getStartLocation().getCity());
        LOG.info("- Número de entregas: " + request.getDeliveries().size());
        LOG.info("- Velocidade do veículo: " + request.getVehicleSpeedKmH() + " km/h");
        LOG.info("- Tempo de carregamento: " + request.getLoadingTimeMinutes() + " min");
        LOG.info("- Usar Google Maps: " + request.getUseGoogleMaps());
        
        // Log produtos por tipo
        var productTypes = request.getDeliveries().stream()
                .flatMap(d -> d.getProducts().stream())
                .collect(java.util.stream.Collectors.groupingBy(
                    p -> p.getType(),
                    java.util.stream.Collectors.counting()
                ));
        
        LOG.info("- Tipos de produtos: " + productTypes);
    }
    
    /**
     * Log detalhes da resposta
     */
    private void logResponseDetails(RouteOptimizationResponse response) {
        if (response.getFeasible()) {
            LOG.info("Otimização bem-sucedida:");
            LOG.info("- Distância total: " + String.format("%.2f", response.getTotalDistanceKm()) + " km");
            LOG.info("- Tempo total: " + response.getTotalTimeMinutes() + " minutos");
            LOG.info("- Tempo de execução: " + response.getAlgorithmExecutionTimeMs() + " ms");
        } else {
            LOG.warn("Otimização inviável:");
            LOG.warn("- Motivo: " + response.getMessage());
            LOG.warn("- Entregas inviáveis: " + response.getInfeasibleDeliveries());
        }
    }
}