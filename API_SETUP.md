# 🗝️ Configuração da API do Google Maps

## Sua Chave de API
Sua chave de API do Google Maps foi configurada no arquivo `.env`:
```
GOOGLE_MAPS_API_KEY=AIzaSyCf_WeR5extE-qTqdQG4c2md5XoXO68h3c
```

## ✅ Status da Configuração
- ✅ Arquivo `.env` criado e configurado
- ✅ Chave da API adicionada
- ✅ Arquivo `.env` incluído no `.gitignore` (segurança)
- ✅ Arquivo `.env.example` criado para outros desenvolvedores

## 🔧 Como Funciona

### 1. Carregamento da Configuração
O Quarkus carrega automaticamente as variáveis do arquivo `.env` e as disponibiliza no `application.properties`:

```properties
google.maps.api.key=${GOOGLE_MAPS_API_KEY:}
```

### 2. Uso no GoogleMapsService
O serviço `GoogleMapsService` usa a chave para fazer chamadas para a Distance Matrix API:

```java
@ConfigProperty(name = "google.maps.api.key", defaultValue = "")
Optional<String> apiKey;
```

### 3. Fallback Automático
Se a chave não estiver configurada ou for inválida, o sistema automaticamente:
- ❌ Falha na chamada da API do Google Maps
- ✅ Usa cálculo de distância Haversine (linha reta)
- ⚠️ Mostra warning no log: "Google Maps API key not configured"

## 🚀 Testando a Configuração

### 1. Iniciar o servidor:
```bash
./mvnw quarkus:dev
```

### 2. Testar endpoint básico:
```bash
curl http://localhost:8080/api/route-optimization/info
```

### 3. Testar otimização com sua API:
```bash
curl -X POST http://localhost:8080/api/route-optimization/optimize \
  -H "Content-Type: application/json" \
  -d '{
    "startLocation": {
      "latitude": -23.5505,
      "longitude": -46.6333,
      "address": "São Paulo, SP"
    },
    "deliveries": [
      {
        "location": {
          "latitude": -22.9068,
          "longitude": -43.1729,
          "address": "Rio de Janeiro, RJ"
        },
        "products": [
          {
            "name": "Leite",
            "category": "Lácteo",
            "quantity": 100.0,
            "unit": "litros",
            "deteriorationTimeMinutes": 240
          }
        ],
        "customerName": "Cliente RJ"
      }
    ]
  }'
```

## 🔒 Segurança

### ✅ Boas Práticas Implementadas:
- Chave armazenada em arquivo `.env` (não no código)
- Arquivo `.env` no `.gitignore` (não vai para o repositório)
- Arquivo `.env.example` para outros desenvolvedores
- Validação e fallback automático se a chave for inválida

### 🚨 Importante:
- **NUNCA** commite o arquivo `.env` no git
- **NUNCA** compartilhe sua chave de API publicamente
- **SEMPRE** use variáveis de ambiente em produção

## 🌐 Configuração da API no Google Cloud

### Serviços Necessários:
1. **Distance Matrix API** - Para calcular distâncias reais entre pontos
2. **Maps JavaScript API** - Se for integrar com frontend web
3. **Geocoding API** - Se precisar converter endereços em coordenadas

### Restrições Recomendadas:
- Restrinja por domínio/IP em produção
- Configure cotas de uso apropriadas
- Monitor o uso através do console

## 🐛 Troubleshooting

### Erro: "Invalid API key"
- ✅ Verifique se a chave está correta no `.env`
- ✅ Verifique se a Distance Matrix API está habilitada
- ✅ Verifique se há restrições de IP/domínio configuradas

### Aviso: "Google Maps API key not configured"
- ✅ Sistema funcionando com distâncias Haversine (menos precisas)
- ✅ Configure a chave para usar distâncias reais da Google

### Erro de compilação/execução:
- ✅ Verifique se o Maven está na versão 3.8.2+
- ✅ Verifique se o Java está na versão 17+
- ✅ Execute `./mvnw clean compile` para recompilar