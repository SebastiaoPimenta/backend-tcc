#!/bin/bash

# Script para testar a nova funcionalidade de verificação de produtos estragados

BASE_URL="http://localhost:8080/api/route-optimization"

echo "🧪 Testando Verificação de Produtos Estragados"
echo "==============================================="

echo
echo "📋 Testando cenário com produtos que podem estragar..."
echo "Este teste usa produtos com tempos de deterioração muito curtos"
echo "para demonstrar a validação final após otimização."

curl -s -X POST "$BASE_URL/optimize" \
  -H "Content-Type: application/json" \
  -d @example-spoilage-test.json | jq '.'

echo
echo "✅ Observe que:"
echo "  - Se produtos estragarem, haverá um warning na mensagem de resposta"
echo "  - A lista de produtos estragados será detalhada"
echo "  - A solução otimizada ainda é retornada para que o usuário possa decidir"

echo
echo "📖 Esta funcionalidade permite ao usuário:"
echo "  - Saber exatamente quais produtos podem estragar"
echo "  - Tomar decisões informadas sobre a rota"
echo "  - Ajustar tempos de deterioração se necessário"