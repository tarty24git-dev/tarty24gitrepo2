@echo off
REM CTH Enterprise Reporting System Start Script for Windows
echo Starting CTH Enterprise Reporting System...
start /b java -jar target/enterprise-reporting-app-1.0.0.jar > app.log 2>&1
echo Application started in background. Logs written to app.log.
