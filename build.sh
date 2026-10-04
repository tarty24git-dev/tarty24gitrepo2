#!/bin/bash
# CTH Enterprise Reporting System Build Script for Linux / Unix
echo "========================================================"
echo "Building CTH Enterprise Reporting System Package..."
echo "========================================================"
mvn clean package -DskipTests
if [ $? -ne 0 ]; then
    echo "[ERROR] Maven Build Failed!"
    exit 1
fi
echo "[SUCCESS] Application packaged successfully in target/enterprise-reporting-app-1.0.0.jar"
