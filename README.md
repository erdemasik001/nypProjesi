# ✈️ Airline Reservation and Management System

BLM2012 Object Oriented Programming - 2025-2026 Fall Semester Project

## 📋 Proje Hakkında

Bu proje, havayolu rezervasyon ve yönetim sistemini Java programlama dili kullanarak geliştirmek için tasarlanmıştır. Nesne Yönelimli Programlama (OOP) prensipleri, Unit Testing ve Multithreading konularını kapsamaktadır.

## 🏗️ Proje Yapısı

```
src/
├── model/
│   ├── flight/          # Uçuş yönetimi modeli
│   │   ├── Plane.java
│   │   ├── Flight.java
│   │   ├── Seat.java
│   │   ├── Route.java
│   │   └── SeatClass.java
│   └── reservation/     # Rezervasyon modeli
│       ├── Passenger.java
│       ├── Reservation.java
│       ├── Ticket.java
│       └── Baggage.java
├── service/             # İş mantığı katmanı
│   ├── FlightManager.java
│   ├── SeatManager.java
│   ├── ReservationManager.java
│   ├── CalculatePrice.java
│   └── FlightSearchEngine.java
├── gui/                 # Kullanıcı arayüzü
│   ├── LoginScreen.java
│   ├── FlightSearchScreen.java
│   ├── ReservationManagementScreen.java
│   ├── AdminScreen.java
│   └── SeatReservationPanel.java
├── thread/              # Çoklu iş parçacığı işlemleri
│   ├── SeatReservationThread.java
│   └── ReportGeneratorThread.java
├── util/                # Yardımcı sınıflar
│   └── FileManager.java
└── Main.java            # Ana giriş noktası

test/
└── service/             # JUnit testleri
    ├── CalculatePriceTest.java
    ├── FlightSearchEngineTest.java
    └── SeatManagerTest.java
```

## 🎯 Ana Modüller

### 1. Flight Management Module
- Uçak, uçuş, koltuk ve rota yönetimi

### 2. Reservation and Ticketing Module
- Yolcu, rezervasyon, bilet ve bagaj yönetimi

### 3. Services and Managers
- Uçuş yöneticisi, koltuk yöneticisi, rezervasyon yöneticisi ve fiyat hesaplama

## 🔧 Gereksinimler

- Java JDK 11 veya üzeri
- JavaFX veya Swing (GUI için)
- JUnit 5 (Test için)

## 🚀 Çalıştırma

```bash
# Derleme
javac -d out src/**/*.java

# Çalıştırma
java -cp out Main
```

## 📝 Özellikler

- ✅ OOP prensipleri (Encapsulation, Inheritance, Polymorphism, Abstraction)
- ✅ Multithreading (Eşzamanlı koltuk rezervasyonu)
- ✅ Asenkron rapor oluşturma
- ✅ Unit Testing (JUnit 5)
- ✅ Dosya tabanlı veri saklama
    - `flights.txt`: Uçuş bilgileri (CSV formatı)
    - `staff.txt`: Personel bilgileri (CSV formatı)
    - `reservations.txt`: Rezervasyon bilgileri (CSV formatı)
- ✅ Grafik kullanıcı arayüzü (GUI)

## 👥 Geliştirici

[Grup Numaranız]

## 📅 Teslim Tarihi

09.01.2026 23:59 (Türkiye Saati)
