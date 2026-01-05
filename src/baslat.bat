@echo off
title NYP Projesi Baslatici
echo ==========================================
echo NYP Projesi Derleniyor...
echo ==========================================
javac Main.java

if %errorlevel% neq 0 (
    echo Derleme Hatasi! Lutfen JDK'nin yuklu oldugundan emin olun.
    pause
    exit /b
)

echo.
echo ==========================================
echo Uygulama Baslatiliyor...
echo ==========================================
java Main
pause
