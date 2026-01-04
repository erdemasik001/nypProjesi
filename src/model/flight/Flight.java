package model.flight;
import java.time.*;

public class Flight {
    private String flightNum;
    private String departurePlace;
    private String arrivalPlace;
    private LocalDate date;
    private LocalTime hour;
    private int duration;

    public Flight(String flightNum, String departurePlace, String arrivalPlace, LocalDate date, LocalTime hour, int duration){
        this.flightNum = flightNum;
        this.departurePlace = departurePlace;
        this.arrivalPlace = arrivalPlace;
        this.date = date;
        this.hour = hour;
        this.duration = duration;
    }
    public String getFlightNum(){
        return flightNum;
    }
    public String getDeparturePlace(){
        return departurePlace;
    }
    public String getArrivalPlace(){
        return arrivalPlace;
    }
    public LocalDate getDate(){
        return date;
    }
    public LocalTime getHour(){
        return hour;
    }
    public int getDuration(){
        return duration;
    }
    public void setFlightNum(String flightNum){
        this.flightNum = flightNum;
    }
    public void setDeparturePlace(String departurePlace){
        this.departurePlace = departurePlace;
    }
    public void setArrivalPlace(String arrivalPlace){
        this.arrivalPlace = arrivalPlace;
    }
    public void setDuration(int duration){
        this.duration = duration;
    }
}
