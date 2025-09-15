# Exemplo de uso da API de Otimização de Rotas

Este arquivo contém exemplos de como usar a API para diferentes cenários.

## Exemplo 1: Rota Simples com 3 Entregas

```bash
curl -X POST http://localhost:8080/api/route-optimization/optimize \
  -H "Content-Type: application/json" \
  -d '{
    "startLocation": {
      "latitude": -23.5505,
      "longitude": -46.6333,
      "address": "Fazenda São João, São Paulo, SP",
      "city": "São Paulo",
      "state": "SP"
    },
    "deliveries": [
      {
        "location": {
          "latitude": -22.9068,
          "longitude": -43.1729,
          "address": "Rua das Flores, 123, Rio de Janeiro, RJ",
          "city": "Rio de Janeiro",
          "state": "RJ"
        },
        "products": [
          {
            "name": "Leite Fresco",
            "type": "Lácteo",
            "quantity": 100.0,
            "unit": "litros",
            "maxDeliveryTimeMinutes": 240,
            "description": "Leite fresco de vaca",
            "temperature": "refrigerado"
          }
        ],
        "customerName": "Padaria Central",
        "customerPhone": "(21) 99999-9999",
        "priority": 1
      },
      {
        "location": {
          "latitude": -15.7942,
          "longitude": -47.8822,
          "address": "Setor Comercial Sul, Brasília, DF",
          "city": "Brasília",
          "state": "DF"
        },
        "products": [
          {
            "name": "Queijo Minas",
            "type": "Lácteo",
            "quantity": 50.0,
            "unit": "kg",
            "maxDeliveryTimeMinutes": 360,
            "description": "Queijo minas artesanal",
            "temperature": "refrigerado"
          }
        ],
        "customerName": "Mercado do Cerrado",
        "customerPhone": "(61) 88888-8888",
        "priority": 2
      },
      {
        "location": {
          "latitude": -19.9167,
          "longitude": -43.9345,
          "address": "Avenida Central, 456, Belo Horizonte, MG",
          "city": "Belo Horizonte",
          "state": "MG"
        },
        "products": [
          {
            "name": "Manteiga",
            "type": "Lácteo",
            "quantity": 25.0,
            "unit": "kg",
            "maxDeliveryTimeMinutes": 180,
            "description": "Manteiga caseira",
            "temperature": "refrigerado"
          }
        ],
        "customerName": "Restaurante Mineiro",
        "customerPhone": "(31) 77777-7777",
        "priority": 1
      }
    ],
    "vehicleSpeedKmH": 60.0,
    "loadingTimeMinutes": 15,
    "useGoogleMaps": true
  }'
```

## Exemplo 2: Verificação de Viabilidade

```bash
curl -X POST http://localhost:8080/api/route-optimization/check-feasibility \
  -H "Content-Type: application/json" \
  -d '{
    "startLocation": {
      "latitude": -23.5505,
      "longitude": -46.6333,
      "address": "Fazenda São João, São Paulo, SP"
    },
    "deliveries": [
      {
        "location": {
          "latitude": -22.9068,
          "longitude": -43.1729,
          "address": "Rua das Flores, 123, Rio de Janeiro, RJ"
        },
        "products": [
          {
            "name": "Leite Ultra Fresco",
            "type": "Lácteo",
            "quantity": 100.0,
            "unit": "litros",
            "maxDeliveryTimeMinutes": 30,
            "description": "Leite que deve ser entregue em 30 minutos"
          }
        ],
        "customerName": "Cliente Urgente"
      }
    ]
  }'
```

## Exemplo 3: Múltiplos Produtos por Entrega

```bash
curl -X POST http://localhost:8080/api/route-optimization/optimize \
  -H "Content-Type: application/json" \
  -d '{
    "startLocation": {
      "latitude": -23.5505,
      "longitude": -46.6333,
      "address": "Fazenda São João, São Paulo, SP"
    },
    "deliveries": [
      {
        "location": {
          "latitude": -22.9068,
          "longitude": -43.1729,
          "address": "Mercado Central, Rio de Janeiro, RJ"
        },
        "products": [
          {
            "name": "Leite Integral",
            "type": "Lácteo",
            "quantity": 200.0,
            "unit": "litros",
            "maxDeliveryTimeMinutes": 300,
            "temperature": "refrigerado"
          },
          {
            "name": "Queijo Fresco",
            "type": "Lácteo",
            "quantity": 30.0,
            "unit": "kg",
            "maxDeliveryTimeMinutes": 240,
            "temperature": "refrigerado"
          },
          {
            "name": "Iogurte Natural",
            "type": "Lácteo",
            "quantity": 50.0,
            "unit": "unidades",
            "maxDeliveryTimeMinutes": 180,
            "temperature": "refrigerado"
          }
        ],
        "customerName": "Mercado Central",
        "customerPhone": "(21) 99999-9999",
        "deliveryNotes": "Entregar pela manhã",
        "priority": 1
      }
    ],
    "vehicleSpeedKmH": 50.0,
    "loadingTimeMinutes": 20,
    "useGoogleMaps": true
  }'
```

## Exemplo 4: Informações do Serviço

```bash
curl -X GET http://localhost:8080/api/route-optimization/info
```

## Exemplo 5: Rota com Produtos de Diferentes Tipos

```bash
curl -X POST http://localhost:8080/api/route-optimization/optimize \
  -H "Content-Type: application/json" \
  -d '{
    "startLocation": {
      "latitude": -20.2976,
      "longitude": -40.2958,
      "address": "Fazenda Esperança, Vitória, ES"
    },
    "deliveries": [
      {
        "location": {
          "latitude": -20.3155,
          "longitude": -40.3128,
          "address": "Centro de Vitória, ES"
        },
        "products": [
          {
            "name": "Tomates Orgânicos",
            "type": "Hortaliça",
            "quantity": 100.0,
            "unit": "kg",
            "maxDeliveryTimeMinutes": 720,
            "temperature": "ambiente"
          }
        ],
        "customerName": "Feira Orgânica"
      },
      {
        "location": {
          "latitude": -20.2621,
          "longitude": -40.3037,
          "address": "Vila Velha, ES"
        },
        "products": [
          {
            "name": "Leite de Cabra",
            "type": "Lácteo",
            "quantity": 50.0,
            "unit": "litros",
            "maxDeliveryTimeMinutes": 120,
            "temperature": "refrigerado"
          }
        ],
        "customerName": "Laticínios Especiais"
      },
      {
        "location": {
          "latitude": -20.2433,
          "longitude": -40.3189,
          "address": "Cariacica, ES"
        },
        "products": [
          {
            "name": "Ovos Caipira",
            "type": "Avícola",
            "quantity": 200.0,
            "unit": "unidades",
            "maxDeliveryTimeMinutes": 480,
            "temperature": "ambiente"
          }
        ],
        "customerName": "Distribuidora de Ovos"
      }
    ],
    "vehicleSpeedKmH": 40.0,
    "loadingTimeMinutes": 10
  }'
```

## Resposta de Sucesso Típica

```json
{
  "optimizedRoute": [2, 0, 1],
  "deliveryOrder": [
    {
      "location": {
        "latitude": -20.2433,
        "longitude": -40.3189,
        "address": "Cariacica, ES"
      },
      "products": [
        {
          "name": "Ovos Caipira",
          "type": "Avícola",
          "quantity": 200.0,
          "unit": "unidades",
          "maxDeliveryTimeMinutes": 480
        }
      ],
      "customerName": "Distribuidora de Ovos"
    },
    {
      "location": {
        "latitude": -20.3155,
        "longitude": -40.3128,
        "address": "Centro de Vitória, ES"
      },
      "products": [
        {
          "name": "Tomates Orgânicos",
          "type": "Hortaliça",
          "quantity": 100.0,
          "unit": "kg",
          "maxDeliveryTimeMinutes": 720
        }
      ],
      "customerName": "Feira Orgânica"
    },
    {
      "location": {
        "latitude": -20.2621,
        "longitude": -40.3037,
        "address": "Vila Velha, ES"
      },
      "products": [
        {
          "name": "Leite de Cabra",
          "type": "Lácteo",
          "quantity": 50.0,
          "unit": "litros",
          "maxDeliveryTimeMinutes": 120
        }
      ],
      "customerName": "Laticínios Especiais"
    }
  ],
  "totalDistanceKm": 45.7,
  "totalTimeMinutes": 98,
  "feasible": true,
  "message": "Rota otimizada com sucesso",
  "algorithmExecutionTimeMs": 856
}
```

## Resposta de Erro (Inviável)

```json
{
  "optimizedRoute": null,
  "deliveryOrder": null,
  "totalDistanceKm": null,
  "totalTimeMinutes": null,
  "feasible": false,
  "infeasibleDeliveries": [1],
  "message": "Não é possível entregar todos os produtos dentro do prazo limite",
  "algorithmExecutionTimeMs": 234
}
```

## Dicas de Uso

1. **Tempos Realistas**: Configure `maxDeliveryTimeMinutes` com valores realistas para produtos perecíveis
2. **Velocidade do Veículo**: Ajuste `vehicleSpeedKmH` considerando trânsito e tipo de estrada
3. **Tempo de Carregamento**: `loadingTimeMinutes` deve incluir tempo de preparação e carregamento
4. **Prioridades**: Use o campo `priority` para dar preferência a certas entregas
5. **Google Maps**: Ative `useGoogleMaps: true` para maior precisão (requer API key configurada)