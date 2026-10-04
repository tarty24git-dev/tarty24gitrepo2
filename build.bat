@echo off
REM CTH Enterprise Reporting System Build Script for Windows
echo ========================================================
echo Building CTH Enterprise Reporting System Package...
echo ========================================================
call mvn clean package -DskipTests
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Maven Build Failed!
    exit /b %ERRORLEVEL%
)
echo [SUCCESS] Application packaged successfully in target/enterprise-reporting-app-1.0.0.jar
