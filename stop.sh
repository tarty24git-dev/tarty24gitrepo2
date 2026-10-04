#!/bin/bash
# CTH Enterprise Reporting System Stop Script for Linux / Unix
echo "Stopping CTH Enterprise Reporting System..."
if [ -f app.pid ]; then
    PID=$(cat app.pid)
    kill -9 $PID 2>/dev/null || true
    rm -f app.pid
    echo "Application process $PID stopped."
else
    pkill -f enterprise-reporting-app 2>/dev/null || true
    echo "Application stopped."
fi
