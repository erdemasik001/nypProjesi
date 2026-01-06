@echo off
title NYP Projesi JAR Olusturucu
echo ==========================================
echo NYP Projesi Derleniyor...
echo ==========================================

cd /d "%~dp0"

REM Tum Java dosyalarini derle
dir /s /B *.java > sources.txt
javac -d out @sources.txt

if %errorlevel% neq 0 (
    echo Derleme Hatasi! Lutfen JDK'nin yuklu oldugundan emin olun.
    pause
    exit /b
)

REM Manifest dosyasi olustur
echo Main-Class: Main> manifest.txt

REM JAR dosyasi olustur
cd out
jar cfm ..\51.jar ..\manifest.txt *

cd ..
del sources.txt
del manifest.txt

echo.
echo ==========================================
echo JAR dosyasi basariyla olusturuldu: 51.jar
echo Calistirmak icin: java -jar 51.jar
echo ==========================================
pause
