#!/bin/bash
# CTH Enterprise Reporting System Start Script for Linux / Unix
echo "Starting CTH Enterprise Reporting System..."
nohup java -jar target/enterprise-reporting-app-1.0.0.jar > app.log 2>&1 &
echo $! > app.pid
echo "Application started in background with PID $(cat app.pid). Logs written to app.log."
