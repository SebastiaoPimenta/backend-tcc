package com.agropecuaria.route.algorithms;

import com.agropecuaria.route.models.Delivery;
import com.agropecuaria.route.models.Location;
import com.agropecuaria.route.models.RouteOptimizationRequest;
import com.agropecuaria.route.models.RouteOptimizationResponse;
import com.agropecuaria.route.services.GoogleMapsService;
import io.jenetics.*;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.util.ISeq;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementação do algoritmo genético para otimização de rotas usando Jenetics
 */
@ApplicationScoped
public class GeneticRouteOptimizer {
    
    private static final Logger LOG = Logger.getLogger(GeneticRouteOptimizer.class);
    
    @Inject
    GoogleMapsService googleMapsService;
    
    // Parâmetros do algoritmo genético
    private static final int POPULATION_SIZE = 100;
    private static final int MAX_GENERATIONS = 500;
    private static final double MUTATION_PROBABILITY = 0.15;
    private static final double CROSSOVER_PROBABILITY = 0.65;
    
    /**
     * Otimiza a rota usando algoritmo genético
     */
    public RouteOptimizationResponse optimizeRoute(RouteOptimizationRequest request) {
        long startTime = System.currentTimeMillis();
        
        try {
            // Prepara dados para o algoritmo
            List<Location> allLocations = prepareLocations(request);
            double[][] timeMatrix = googleMapsService.calculateDistanceMatrix(allLocations);
            
            // Verifica viabilidade antes de otimizar
            FeasibilityResult feasibilityResult = checkFeasibility(request, timeMatrix);
            if (!feasibilityResult.isFeasible()) {
                return createInfeasibleResponse(feasibilityResult, startTime);
            }
            
            // Executa algoritmo genético
            List<Integer> optimizedRoute = runGeneticAlgorithm(request, timeMatrix);
            
            // Calcula métricas da rota otimizada
            double totalTime = googleMapsService.calculateRouteTime(
                    optimizedRoute, timeMatrix, request.getLoadingTimeMinutes()
            );
            
            double totalDistance = googleMapsService.calculateRouteDistance(optimizedRoute, allLocations);
            
            // Constrói resposta
            RouteOptimizationResponse response = new RouteOptimizationResponse();
            response.setOptimizedRoute(optimizedRoute.subList(1, optimizedRoute.size() - 1)); // Remove início e fim
            response.setDeliveryOrder(buildDeliveryOrder(optimizedRoute, request.getDeliveries()));
            response.setTotalDistanceKm(totalDistance);
            response.setTotalTimeMinutes((int) Math.ceil(totalTime));
            response.setFeasible(true);
            response.setMessage("Rota otimizada com sucesso");
            response.setAlgorithmExecutionTimeMs(System.currentTimeMillis() - startTime);
            
            return response;
            
        } catch (Exception e) {
            LOG.error("Erro durante otimização da rota: " + e.getMessage(), e);
            
            RouteOptimizationResponse response = new RouteOptimizationResponse();
            response.setFeasible(false);
            response.setMessage("Erro durante otimização: " + e.getMessage());
            response.setAlgorithmExecutionTimeMs(System.currentTimeMillis() - startTime);
            
            return response;
        }
    }
    
    /**
     * Prepara lista de localizações incluindo ponto inicial/final
     */
    private List<Location> prepareLocations(RouteOptimizationRequest request) {
        List<Location> locations = new ArrayList<>();
        locations.add(request.getStartLocation()); // Índice 0
        
        for (Delivery delivery : request.getDeliveries()) {
            locations.add(delivery.getLocation());
        }
        
        locations.add(request.getStartLocation()); // Índice final (mesmo que 0)
        
        return locations;
    }
    
    /**
     * Executa o algoritmo genético
     */
    private List<Integer> runGeneticAlgorithm(RouteOptimizationRequest request, double[][] timeMatrix) {
        int numDeliveries = request.getDeliveries().size();
        
        // Cria engine do algoritmo genético
        Engine<EnumGene<Integer>, Double> engine = Engine
                .builder(
                    genotype -> fitness(genotype, request, timeMatrix),
                    PermutationChromosome.ofInteger(numDeliveries)
                )
                .populationSize(POPULATION_SIZE)
                .alterers(
                    new SwapMutator<>(MUTATION_PROBABILITY),
                    new PartiallyMatchedCrossover<>(CROSSOVER_PROBABILITY)
                )
                .build();
        
        // Executa evolução
        Phenotype<EnumGene<Integer>, Double> best = engine.stream()
                .limit(MAX_GENERATIONS)
                .collect(EvolutionResult.toBestPhenotype());
        
        // Constrói rota final
        List<Integer> route = new ArrayList<>();
        route.add(0); // Ponto inicial
        
        // Adiciona entregas na ordem otimizada
        PermutationChromosome<Integer> chromosome = (PermutationChromosome<Integer>) best.genotype().chromosome();
        for (int i = 0; i < chromosome.length(); i++) {
            int deliveryIndex = chromosome.get(i).allele() + 1; // +1 porque 0 é o ponto inicial
            route.add(deliveryIndex);
        }
        
        route.add(0); // Retorna ao ponto inicial
        
        return route;
    }
    
    /**
     * Função de fitness (quanto menor, melhor)
     */
    private double fitness(Genotype<EnumGene<Integer>> genotype, 
                          RouteOptimizationRequest request, 
                          double[][] timeMatrix) {
        
        // Constrói rota a partir do genótipo
        List<Integer> route = new ArrayList<>();
        route.add(0); // Ponto inicial
        
        PermutationChromosome<Integer> chromosome = (PermutationChromosome<Integer>) genotype.chromosome();
        for (int i = 0; i < chromosome.length(); i++) {
            int deliveryIndex = chromosome.get(i).allele() + 1;
            route.add(deliveryIndex);
        }
        
        route.add(0); // Retorna ao ponto inicial
        
        // Calcula fitness baseado no tempo total e violações de restrições
        double totalTime = googleMapsService.calculateRouteTime(
                route, timeMatrix, request.getLoadingTimeMinutes()
        );
        
        // Penaliza rotas que violam restrições de tempo
        double penalty = calculateTimePenalty(route, request, timeMatrix);
        
        return totalTime + penalty;
    }
    
    /**
     * Calcula penalidade por violação de restrições de tempo
     */
    private double calculateTimePenalty(List<Integer> route, 
                                      RouteOptimizationRequest request, 
                                      double[][] timeMatrix) {
        double penalty = 0;
        double currentTime = 0;
        
        for (int i = 1; i < route.size() - 1; i++) {
            int prevIndex = route.get(i - 1);
            int currentIndex = route.get(i);
            
            // Adiciona tempo de viagem
            currentTime += timeMatrix[prevIndex][currentIndex];
            
            // Adiciona tempo de carregamento
            currentTime += request.getLoadingTimeMinutes();
            
            // Verifica se viola restrição de tempo do produto
            Delivery delivery = request.getDeliveries().get(currentIndex - 1);
            int maxTimeLimit = delivery.getMinDeliveryTimeLimit();
            
            if (currentTime > maxTimeLimit) {
                // Penalidade exponencial para violações graves
                penalty += Math.pow(currentTime - maxTimeLimit, 2) * 1000;
            }
        }
        
        return penalty;
    }
    
    /**
     * Verifica viabilidade da solução
     */
    private FeasibilityResult checkFeasibility(RouteOptimizationRequest request, double[][] timeMatrix) {
        List<Integer> infeasibleDeliveries = new ArrayList<>();
        
        for (int i = 0; i < request.getDeliveries().size(); i++) {
            Delivery delivery = request.getDeliveries().get(i);
            int maxTimeLimit = delivery.getMinDeliveryTimeLimit();
            
            // Tempo mínimo direto do ponto inicial até esta entrega
            double minTimeToDelivery = timeMatrix[0][i + 1] + request.getLoadingTimeMinutes();
            
            if (minTimeToDelivery > maxTimeLimit) {
                infeasibleDeliveries.add(i);
            }
        }
        
        return new FeasibilityResult(infeasibleDeliveries.isEmpty(), infeasibleDeliveries);
    }
    
    /**
     * Constrói ordem de entrega a partir da rota otimizada
     */
    private List<Delivery> buildDeliveryOrder(List<Integer> route, List<Delivery> deliveries) {
        return route.subList(1, route.size() - 1).stream()
                .map(index -> deliveries.get(index - 1))
                .collect(Collectors.toList());
    }
    
    /**
     * Cria resposta para casos inviáveis
     */
    private RouteOptimizationResponse createInfeasibleResponse(FeasibilityResult feasibilityResult, long startTime) {
        RouteOptimizationResponse response = new RouteOptimizationResponse();
        response.setFeasible(false);
        response.setInfeasibleDeliveries(feasibilityResult.getInfeasibleDeliveries());
        response.setMessage("Não é possível entregar todos os produtos dentro do prazo limite");
        response.setAlgorithmExecutionTimeMs(System.currentTimeMillis() - startTime);
        
        return response;
    }
    
    /**
     * Classe interna para resultado de viabilidade
     */
    private static class FeasibilityResult {
        private final boolean feasible;
        private final List<Integer> infeasibleDeliveries;
        
        public FeasibilityResult(boolean feasible, List<Integer> infeasibleDeliveries) {
            this.feasible = feasible;
            this.infeasibleDeliveries = infeasibleDeliveries;
        }
        
        public boolean isFeasible() {
            return feasible;
        }
        
        public List<Integer> getInfeasibleDeliveries() {
            return infeasibleDeliveries;
        }
    }
}