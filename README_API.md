# API de Otimização de Rotas Agropecuárias

Esta é uma API REST para otimização de rotas de entrega de produtos agropecuários usando algoritmos genéticos.

## 🚀 Como executar

1. **Pré-requisitos:**
   - Java 17+
   - Maven 3.8.2+
   - API Key do Google Maps (configurada no arquivo `.env`)

2. **Executar em modo desenvolvimento:**
   ```bash
   ./mvnw quarkus:dev
   ```

3. **Acessar a documentação Swagger:**
   - URL: http://localhost:8080/q/swagger-ui
   - Permite testar todos os endpoints da API

## 📋 Endpoints Disponíveis

### 1. Otimizar Rota
- **POST** `/api/route-optimization/optimize`
- Utiliza algoritmo genético para encontrar a melhor sequência de entregas
- Considera tempo de deterioração dos produtos
- **Nova funcionalidade**: Verificação final de produtos estragados após otimização
  - Se produtos estragarem, retorna warning na resposta mas mantém a solução otimizada
  - Inclui lista detalhada dos produtos que podem estragar

### 2. Verificar Viabilidade
- **POST** `/api/route-optimization/check-feasibility`
- Verifica se é possível entregar todos os produtos dentro dos prazos
- Não otimiza, apenas verifica viabilidade

### 3. Informações do Serviço
- **GET** `/api/route-optimization/info`
- Retorna informações sobre o serviço e versão

## 🧬 Algoritmo Genético

O sistema utiliza a biblioteca **Jenetics** com os seguintes parâmetros:

- **População:** 250 indivíduos
- **Gerações:** 500 iterações
- **Mutação:** 1% de probabilidade
- **Crossover:** 60% de probabilidade

## 📊 Produtos Suportados

O sistema reconhece diferentes tipos de produtos com tempos de deterioração realistas:

- **Leite fresco:** 12 horas (720 minutos)
- **Frutas frescas:** 24 horas (1440 minutos)
- **Verduras:** 24 horas (1440 minutos)
- **Produtos processados:** 24 horas (1440 minutos)

## 🗺️ Integração Google Maps

- Utiliza a API Distance Matrix do Google Maps
- Calcula distâncias e tempos reais entre localizações
- Fallback para cálculo Haversine em caso de erro

## 📝 Exemplo de Uso

### Payload para otimização:

```json
{
  "startLocation": {
    "latitude": -15.7942,
    "longitude": -47.8822,
    "address": "Brasília, DF"
  },
  "deliveries": [
    {
      "id": "entrega-1",
      "location": {
        "latitude": -15.7801,
        "longitude": -47.9292,
        "address": "Taguatinga, DF"
      },
      "products": [
        {
          "name": "Leite fresco",
          "type": "DAIRY",
          "quantity": 50,
          "deteriorationTimeMinutes": 720
        }
      ]
    }
  ]
}
```

### Resposta esperada:

```json
{
  "feasible": true,
  "optimizedRoute": [
    {
      "deliveryId": "entrega-1",
      "sequence": 1,
      "estimatedArrivalTime": "09:30:00",
      "travelTimeFromPrevious": 30,
      "distanceFromPrevious": 25.5
    }
  ],
  "totalDistance": 25.5,
  "totalTime": 30,
  "algorithmExecutionTimeMs": 1250,
  "message": "Rota otimizada com sucesso"
}
```

## 🔧 Configuração

As configurações principais estão no arquivo `application.yml`:

- **Porta do servidor:** 8080
- **Configurações do algoritmo genético**
- **Configurações do Google Maps**
- **Níveis de logging**

## 📖 Documentação OpenAPI

A documentação completa da API está disponível no Swagger UI:
- **URL:** http://localhost:8080/q/swagger-ui
- **OpenAPI JSON:** http://localhost:8080/q/openapi

## 🧪 Testes

Execute os testes com:
```bash
./mvnw test
```

## 🔍 Monitoramento

- **Health Check:** http://localhost:8080/q/health
- **Métricas:** http://localhost:8080/q/metrics

---

## 📄 Licença

MIT License