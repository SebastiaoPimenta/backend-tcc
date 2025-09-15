# ⏰ Tempos de Deterioração de Produtos Agrícolas

## Tempos Realistas Configurados nos Testes

### 🥛 Produtos Lácteos
- **Leite Fresco**: 120 minutos (2 horas) - Produto extremamente perecível
- **Leite**: 720 minutos (12 horas) - Leite pasteurizado refrigerado
- **Queijo**: 1080-1440 minutos (18-24 horas) - Queijos frescos
- **Manteiga**: 480 minutos (8 horas) - Produto refrigerado

### 🥬 Verduras e Vegetais
- **Folhosos (alface, espinafre)**: 240-360 minutos (4-6 horas)
- **Vegetais delicados (tomate, pepino)**: 480-720 minutos (8-12 horas)
- **Vegetais resistentes (batata, cenoura)**: 1440-2880 minutos (24-48 horas)

### 🍎 Frutas
- **Frutas muito perecíveis (banana madura, morango)**: 240-480 minutos (4-8 horas)
- **Frutas médias (maçã, laranja)**: 720-1440 minutos (12-24 horas)
- **Frutas resistentes (limão, abacaxi)**: 1440-4320 minutos (24-72 horas)

### 🥩 Carnes e Aves
- **Carne fresca sem refrigeração**: 120-240 minutos (2-4 horas)
- **Carne refrigerada**: 1440-2880 minutos (24-48 horas)
- **Aves frescas**: 480-720 minutos (8-12 horas)

### 🐟 Peixes e Frutos do Mar
- **Peixe fresco**: 180-360 minutos (3-6 horas)
- **Peixe no gelo**: 720-1440 minutos (12-24 horas)
- **Frutos do mar**: 120-240 minutos (2-4 horas)

## 🚛 Tempos de Viagem Considerados

### Distâncias Típicas no Brasil:
- **São Paulo ↔ Rio de Janeiro**: ~360 minutos (6 horas)
- **São Paulo ↔ Belo Horizonte**: ~420 minutos (7 horas)
- **São Paulo ↔ Curitiba**: ~240 minutos (4 horas)
- **São Paulo ↔ Brasília**: ~600 minutos (10 horas)

### Fatores que Afetam o Tempo:
- **Trânsito urbano**: +30-60 minutos por cidade grande
- **Condições da estrada**: +10-20% do tempo base
- **Paradas obrigatórias**: +15-30 minutos por parada
- **Condições climáticas**: +20-50% em condições adversas

## ⚡ Algoritmo Genético e Restrições

### Como o Sistema Funciona:
1. **Cálculo de Viabilidade**: Verifica se é possível entregar cada produto dentro do tempo limite
2. **Função Fitness**: Penaliza rotas que violam restrições de tempo
3. **Otimização**: Encontra a melhor sequência respeitando todos os limites
4. **Retorno**: Identifica entregas inviáveis e sugere alternativas

### Exemplo Prático:
```
Produto: Leite Fresco (120 min limite)
Rota: São Paulo → Rio de Janeiro (360 min viagem)
Resultado: INVIÁVEL ❌

Produto: Queijo (1440 min limite)  
Rota: São Paulo → Rio de Janeiro (360 min viagem)
Resultado: VIÁVEL ✅ (sobram 1080 min)
```

## 🎯 Benefícios dos Tempos Realistas

- **Precisão**: Reflete condições reais do agronegócio brasileiro
- **Flexibilidade**: Diferentes produtos têm diferentes restrições
- **Otimização**: Algoritmo pode sugerir rotas mais eficientes
- **Planejamento**: Ajuda no planejamento logístico real

Os testes agora usam tempos que refletem a realidade do transporte de produtos agrícolas no Brasil! 🚚🥛🥬