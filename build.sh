#!/bin/bash
set -e

echo "Building Quarkus application..."
./mvnw clean package -DskipTests

echo "Build completed successfully!"
echo "Generated JAR: target/route-optimization-1.0.0-SNAPSHOT-runner.jar"