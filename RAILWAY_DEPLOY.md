# Deploy no Railway

Este guia explica como fazer deploy da aplicação Route Optimization no Railway.

## Configuração

### 1. Arquivo Procfile
O arquivo `Procfile` contém o comando para iniciar a aplicação:
```
web: java -Dquarkus.http.host=0.0.0.0 -Dquarkus.http.port=$PORT -jar target/route-optimization-1.0.0-SNAPSHOT-runner.jar
```

### 2. Configurações de Ambiente
Configure as seguintes variáveis de ambiente no Railway:

- `GOOGLE_MAPS_API_KEY`: Sua chave da API do Google Maps
- `PORT`: Porta configurada automaticamente pelo Railway
- `QUARKUS_HTTP_PORT`: Usa a variável $PORT do Railway

### 3. Build
O projeto está configurado para gerar um uber-jar, que contém todas as dependências necessárias em um único arquivo JAR executável.

## Comandos de Deploy

### Build Local
```bash
./mvnw clean package -DskipTests
```

### Executar Localmente
```bash
java -Dquarkus.http.host=0.0.0.0 -Dquarkus.http.port=8080 -jar target/route-optimization-1.0.0-SNAPSHOT-runner.jar
```

## Estrutura de Arquivos para Deploy

- `Procfile`: Comando de inicialização
- `railway.toml`: Configurações específicas do Railway
- `build.sh`: Script de build (opcional)
- `target/route-optimization-1.0.0-SNAPSHOT-runner.jar`: JAR executável gerado

## Verificação

Após o deploy, a aplicação estará disponível em:
- Health check: `https://seu-app.railway.app/q/health`
- API docs: `https://seu-app.railway.app/q/swagger-ui`
- Endpoint principal: `https://seu-app.railway.app/api/route-optimization`

## Logs

Para ver os logs no Railway:
```bash
railway logs
```

## Troubleshooting

### Erro "no main manifest attribute"
✅ **Resolvido**: O projeto agora gera um uber-jar com Main-Class definido

### Port binding
✅ **Configurado**: A aplicação usa a variável $PORT do Railway

### Environment variables
✅ **Configurado**: Variáveis de ambiente são lidas automaticamente