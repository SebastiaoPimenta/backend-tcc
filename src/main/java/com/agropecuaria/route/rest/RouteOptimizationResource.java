package com.agropecuaria.route.rest;

import com.agropecuaria.route.algorithms.GeneticRouteOptimizer;
import com.agropecuaria.route.models.RouteOptimizationRequest;
import com.agropecuaria.route.models.RouteOptimizationResponse;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

/**
 * Endpoint REST para otimização de rotas de entrega
 */
@Path("/api/route-optimization")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RouteOptimizationResource {
    
    private static final Logger LOG = Logger.getLogger(RouteOptimizationResource.class);
    
    @Inject
    GeneticRouteOptimizer routeOptimizer;
    
    /**
     * Endpoint principal para otimização de rotas
     * 
     * @param request Requisição contendo localizações e produtos para entrega
     * @return Resposta com rota otimizada
     */
    @POST
    @Path("/optimize")
    public Response optimizeRoute(@Valid RouteOptimizationRequest request) {
        LOG.info("Recebida requisição de otimização de rota com " + 
                request.getDeliveries().size() + " entregas");
        
        try {
            // Validações básicas
            if (request.getDeliveries().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(createErrorResponse("Lista de entregas não pode estar vazia"))
                        .build();
            }
            
            if (request.getStartLocation() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(createErrorResponse("Localização inicial é obrigatória"))
                        .build();
            }
            
            // Executa otimização
            RouteOptimizationResponse response = routeOptimizer.optimizeRoute(request);
            
            if (response.getFeasible()) {
                LOG.info("Rota otimizada com sucesso. Tempo total: " + 
                        response.getTotalTimeMinutes() + " minutos, Distância: " + 
                        String.format("%.2f", response.getTotalDistanceKm()) + " km");
                
                return Response.ok(response).build();
            } else {
                LOG.warn("Não foi possível otimizar a rota: " + response.getMessage());
                return Response.status(422)
                        .entity(response)
                        .build();
            }
            
        } catch (Exception e) {
            LOG.error("Erro interno durante otimização da rota", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(createErrorResponse("Erro interno do servidor: " + e.getMessage()))
                    .build();
        }
    }
    
    /**
     * Endpoint para verificar viabilidade de uma rota sem otimizar
     */
    @POST
    @Path("/check-feasibility")
    public Response checkFeasibility(@Valid RouteOptimizationRequest request) {
        LOG.info("Verificando viabilidade de " + request.getDeliveries().size() + " entregas");
        
        try {
            RouteOptimizationResponse response = routeOptimizer.optimizeRoute(request);
            
            // Retorna apenas informações de viabilidade
            RouteOptimizationResponse feasibilityResponse = new RouteOptimizationResponse();
            feasibilityResponse.setFeasible(response.getFeasible());
            feasibilityResponse.setMessage(response.getMessage());
            feasibilityResponse.setInfeasibleDeliveries(response.getInfeasibleDeliveries());
            feasibilityResponse.setAlgorithmExecutionTimeMs(response.getAlgorithmExecutionTimeMs());
            
            return Response.ok(feasibilityResponse).build();
            
        } catch (Exception e) {
            LOG.error("Erro durante verificação de viabilidade", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(createErrorResponse("Erro interno do servidor: " + e.getMessage()))
                    .build();
        }
    }
    
    /**
     * Endpoint para obter informações sobre o serviço
     */
    @GET
    @Path("/info")
    public Response getServiceInfo() {
        ServiceInfo info = new ServiceInfo();
        info.setServiceName("Route Optimization Service");
        info.setVersion("1.0.0");
        info.setDescription("Serviço de otimização de rotas para entregas agropecuárias usando algoritmos genéticos");
        info.setAlgorithm("Jenetics - Genetic Algorithm");
        info.setStatus("Active");
        
        return Response.ok(info).build();
    }
    
    /**
     * Cria resposta de erro padronizada
     */
    private ErrorResponse createErrorResponse(String message) {
        ErrorResponse error = new ErrorResponse();
        error.setError(true);
        error.setMessage(message);
        error.setTimestamp(System.currentTimeMillis());
        return error;
    }
    
    /**
     * Classe para informações do serviço
     */
    public static class ServiceInfo {
        private String serviceName;
        private String version;
        private String description;
        private String algorithm;
        private String status;
        
        // Getters and Setters
        public String getServiceName() { return serviceName; }
        public void setServiceName(String serviceName) { this.serviceName = serviceName; }
        
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public String getAlgorithm() { return algorithm; }
        public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
    
    /**
     * Classe para respostas de erro
     */
    public static class ErrorResponse {
        private boolean error;
        private String message;
        private long timestamp;
        
        // Getters and Setters
        public boolean isError() { return error; }
        public void setError(boolean error) { this.error = error; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    }
}