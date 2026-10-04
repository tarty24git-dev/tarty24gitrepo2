@echo off
REM CTH Enterprise Reporting System Stop Script for Windows
echo Stopping CTH Enterprise Reporting System...
for /f "tokens=5" %%a in ('netstat -aon ^| findstr :8080') do taskkill /f /pid %%a 2>nul
echo Application stopped.
