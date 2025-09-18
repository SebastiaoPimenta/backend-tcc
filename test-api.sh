#!/bin/bash

# Script para testar a API de Otimização de Rotas

BASE_URL="http://localhost:8080/api/route-optimization"

echo "🚀 Testando API de Otimização de Rotas"
echo "======================================="

# Teste 1: Verificar se o serviço está funcionando
echo
echo "📋 1. Testando endpoint de informações..."
curl -s -X GET "$BASE_URL/info" | jq '.' || echo "❌ Erro ao acessar /info"

# Teste 2: Verificar viabilidade
echo
echo "🔍 2. Testando verificação de viabilidade..."
curl -s -X POST "$BASE_URL/check-feasibility" \
  -H "Content-Type: application/json" \
  -d @example-request.json | jq '.' || echo "❌ Erro ao verificar viabilidade"

# Teste 3: Otimizar rota
echo
echo "🧬 3. Testando otimização de rota..."
curl -s -X POST "$BASE_URL/optimize" \
  -H "Content-Type: application/json" \
  -d @example-request.json | jq '.' || echo "❌ Erro ao otimizar rota"

echo
echo "✅ Testes concluídos!"
echo "📖 Acesse a documentação em: http://localhost:8080/q/swagger-ui"