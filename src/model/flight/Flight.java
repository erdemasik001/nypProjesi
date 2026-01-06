package model.flight;

import java.time.*;

public class Flight {
    private String flightNum;
    private String departurePlace;
    private String arrivalPlace;
    private LocalDate date;
    private LocalTime hour;
    private int duration;
    private double price;

    public Flight(String flightNum, String departurePlace, String arrivalPlace, LocalDate date, LocalTime hour,
            int duration, double price) {
        this.flightNum = flightNum;
        this.departurePlace = departurePlace;
        this.arrivalPlace = arrivalPlace;
        this.date = date;
        this.hour = hour;
        this.duration = duration;
        this.price = price;
    }

    public String getFlightNum() {
        return flightNum;
    }

    public String getDeparturePlace() {
        return departurePlace;
    }

    public String getArrivalPlace() {
        return arrivalPlace;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getHour() {
        return hour;
    }

    public int getDuration() {
        return duration;
    }

    public double getPrice() {
        return price;
    }

    public void setFlightNum(String flightNum) {
        this.flightNum = flightNum;
    }

    public void setDeparturePlace(String departurePlace) {
        this.departurePlace = departurePlace;
    }

    public void setArrivalPlace(String arrivalPlace) {
        this.arrivalPlace = arrivalPlace;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setHour(LocalTime hour) {
        this.hour = hour;
    }
}
