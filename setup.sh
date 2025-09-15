#!/bin/bash

# Script de inicialização do projeto Route Optimization

echo "🚀 Inicializando Backend de Otimização de Rotas"
echo "============================================="

# Verificar se Java 17+ está instalado
if type -p java; then
    echo "✅ Java encontrado"
    _java=java
elif [[ -n "$JAVA_HOME" ]] && [[ -x "$JAVA_HOME/bin/java" ]];  then
    echo "✅ Java encontrado em JAVA_HOME"
    _java="$JAVA_HOME/bin/java"
else
    echo "❌ Java não encontrado. Por favor, instale Java 17 ou superior."
    exit 1
fi

# Verificar versão do Java
if [[ "$_java" ]]; then
    version=$("$_java" -version 2>&1 | awk -F '"' '/version/ {print $2}')
    echo "📋 Versão do Java: $version"
    
    # Verificar se é Java 17+
    if [[ "$version" < "17" ]]; then
        echo "❌ Java 17 ou superior é necessário. Versão atual: $version"
        exit 1
    fi
fi

# Verificar se Maven está instalado
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven não encontrado. Por favor, instale Maven 3.8+"
    exit 1
else
    echo "✅ Maven encontrado"
fi

# Verificar variável de ambiente da API do Google Maps
if [[ -z "${GOOGLE_MAPS_API_KEY}" ]]; then
    echo "⚠️  Variável GOOGLE_MAPS_API_KEY não configurada"
    echo "   A aplicação funcionará com cálculos de distância em linha reta"
    echo "   Para melhor precisão, configure sua chave da API do Google Maps:"
    echo "   export GOOGLE_MAPS_API_KEY=sua_chave_aqui"
else
    echo "✅ Chave da API Google Maps configurada"
fi

echo ""
echo "🔧 Compilando projeto..."
./mvnw clean compile

if [ $? -eq 0 ]; then
    echo "✅ Compilação concluída com sucesso"
else
    echo "❌ Erro na compilação"
    exit 1
fi

echo ""
echo "🧪 Executando testes..."
./mvnw test

if [ $? -eq 0 ]; then
    echo "✅ Testes executados com sucesso"
else
    echo "⚠️  Alguns testes falharam, mas a aplicação pode ainda assim ser executada"
fi

echo ""
echo "🏗️  Empacotando aplicação..."
./mvnw package

if [ $? -eq 0 ]; then
    echo "✅ Empacotamento concluído"
else
    echo "❌ Erro no empacotamento"
    exit 1
fi

echo ""
echo "🎉 Inicialização concluída!"
echo ""
echo "Para executar a aplicação:"
echo "  Modo desenvolvimento: ./mvnw quarkus:dev"
echo "  Modo produção:       java -jar target/quarkus-app/quarkus-run.jar"
echo ""
echo "A aplicação estará disponível em: http://localhost:8080"
echo "Documentação da API: http://localhost:8080/q/swagger-ui"
echo "Endpoint principal: http://localhost:8080/api/route-optimization/optimize"
echo ""
echo "Consulte o README.md para mais informações e exemplos de uso."