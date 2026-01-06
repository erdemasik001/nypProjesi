package model.reservation;

import java.util.Date;

import model.flight.*;

public class Reservation {
    private String reservationCode;
    private Flight flight;
    private Seat seat;
    private Passenger passenger;
    private Date dateOfReservation;

    public Reservation(String reservationCode, Flight flight, Seat seat, Passenger passenger, Date dateOfReservation) {
        this.reservationCode = reservationCode;
        this.flight = flight;
        this.seat = seat;
        this.passenger = passenger;
        this.dateOfReservation = dateOfReservation;
        this.status = "Active";
    }

    public Reservation(String reservationCode, Flight flight, Seat seat, Passenger passenger, Date dateOfReservation,
            String status) {
        this.reservationCode = reservationCode;
        this.flight = flight;
        this.seat = seat;
        this.passenger = passenger;
        this.dateOfReservation = dateOfReservation;
        this.status = status;
    }

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReservationCode() {
        return reservationCode;
    }

    public Flight getFlight() {
        return flight;
    }

    public Seat getSeat() {
        return seat;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public Date getDateOfReservation() {
        return dateOfReservation;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public void setDateOfReservation(Date dateOfReservation) {
        this.dateOfReservation = dateOfReservation;
    }
}
