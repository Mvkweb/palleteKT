@echo off
REM PalleteKT Quick Push Script
REM Simple wrapper for push.ps1

powershell -ExecutionPolicy Bypass -File "%~dp0push.ps1" %*
