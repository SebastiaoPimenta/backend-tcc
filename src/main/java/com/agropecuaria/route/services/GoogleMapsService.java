package com.agropecuaria.route.services;

import com.agropecuaria.route.models.Location;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixRow;
import com.google.maps.model.LatLng;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.Arrays;

/**
 * Serviço para integração com Google Maps Distance Matrix API
 */
@ApplicationScoped
public class GoogleMapsService {
    
    private static final Logger LOG = Logger.getLogger(GoogleMapsService.class);
    
    @ConfigProperty(name = "google.maps.api.key")
    String googleMapsApiKey;
    
    private GeoApiContext context;
    
    public void init() {
        if (context == null && googleMapsApiKey != null && !googleMapsApiKey.isEmpty()) {
            context = new GeoApiContext.Builder()
                    .apiKey(googleMapsApiKey)
                    .build();
        }
    }
    
    /**
     * Calcula a matriz de distâncias e tempos entre todas as localizações
     * @param locations Lista de localizações
     * @return Matriz de tempos em minutos (locations x locations)
     */
    public double[][] calculateDistanceMatrix(List<Location> locations) {
        init();
        
        if (context == null) {
            LOG.warn("Google Maps API key not configured, using straight-line distances");
            return calculateStraightLineDistances(locations);
        }
        
        try {
            LatLng[] origins = locations.stream()
                    .map(loc -> new LatLng(loc.getLatitude(), loc.getLongitude()))
                    .toArray(LatLng[]::new);
            
            LatLng[] destinations = origins;
            
            DistanceMatrix matrix = DistanceMatrixApi.newRequest(context)
                    .origins(origins)
                    .destinations(destinations)
                    .await();
            
            return parseDistanceMatrix(matrix);
            
        } catch (Exception e) {
            LOG.error("Error calling Google Maps API: " + e.getMessage(), e);
            // Fallback para cálculo de distância em linha reta
            return calculateStraightLineDistances(locations);
        }
    }
    
    /**
     * Calcula distâncias em linha reta quando Google Maps não está disponível
     */
    private double[][] calculateStraightLineDistances(List<Location> locations) {
        int size = locations.size();
        double[][] distances = new double[size][size];
        
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == j) {
                    distances[i][j] = 0;
                } else {
                    Location loc1 = locations.get(i);
                    Location loc2 = locations.get(j);
                    double distanceKm = calculateHaversineDistance(
                            loc1.getLatitude(), loc1.getLongitude(),
                            loc2.getLatitude(), loc2.getLongitude()
                    );
                    // Converte para tempo assumindo velocidade média de 60 km/h
                    distances[i][j] = (distanceKm / 60.0) * 60; // tempo em minutos
                }
            }
        }
        
        return distances;
    }
    
    /**
     * Calcula distância usando fórmula de Haversine
     */
    private double calculateHaversineDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Raio da Terra em km
        
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return R * c;
    }
    
    /**
     * Converte a resposta da API do Google Maps para matriz de tempos
     */
    private double[][] parseDistanceMatrix(DistanceMatrix matrix) {
        DistanceMatrixRow[] rows = matrix.rows;
        int size = rows.length;
        double[][] timeMatrix = new double[size][size];
        
        for (int i = 0; i < size; i++) {
            DistanceMatrixElement[] elements = rows[i].elements;
            for (int j = 0; j < elements.length; j++) {
                if (elements[j].duration != null) {
                    // Converte de segundos para minutos
                    timeMatrix[i][j] = elements[j].duration.inSeconds / 60.0;
                } else {
                    // Se não conseguir obter o tempo, usa distância em linha reta
                    timeMatrix[i][j] = Double.MAX_VALUE;
                }
            }
        }
        LOG.info("Time matrix: " + Arrays.deepToString(timeMatrix));
        return timeMatrix;
    }
    
    /**
     * Calcula o tempo total de uma rota específica
     */
    public double calculateRouteTime(List<Integer> route, double[][] timeMatrix, int loadingTimeMinutes) {
        double totalTime = 0;
        
        for (int i = 0; i < route.size() - 1; i++) {
            int from = route.get(i);
            int to = route.get(i + 1);
            totalTime += timeMatrix[from][to];
            
            // Adiciona tempo de carregamento/descarregamento (exceto no último ponto)
            if (i < route.size() - 2) {
                totalTime += loadingTimeMinutes;
            }
        }
        
        return totalTime;
    }
    
    /**
     * Calcula a distância total de uma rota específica
     */
    public double calculateRouteDistance(List<Integer> route, List<Location> locations) {
        double totalDistance = 0;
        
        for (int i = 0; i < route.size() - 1; i++) {
            Location from = locations.get(route.get(i));
            Location to = locations.get(route.get(i + 1));
            totalDistance += calculateHaversineDistance(
                    from.getLatitude(), from.getLongitude(),
                    to.getLatitude(), to.getLongitude()
            );
        }
        
        return totalDistance;
    }
}