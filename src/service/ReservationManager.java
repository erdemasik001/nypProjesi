package service;

import model.flight.*;
import model.reservation.*;
import java.util.*;
import java.util.concurrent.*;

public class ReservationManager {

    private Map<String, Reservation> reservations;
    private Map<String, Seat> reservedSeats;
    private final Object lock = new Object();

    public ReservationManager() {
        this.reservations = new ConcurrentHashMap<>();
        this.reservedSeats = new ConcurrentHashMap<>();
    }

    public Reservation makeReservation(String reservationCode, Flight flight, Seat seat,
            Passenger passenger, Date dateOfReservation) {
        synchronized (lock) {
            if (reservationCode == null || reservationCode.trim().isEmpty()) {
                throw new IllegalArgumentException("Reservation code cannot be null or empty");
            }

            if (reservations.containsKey(reservationCode)) {
                throw new IllegalArgumentException("Reservation with code " + reservationCode + " already exists");
            }

            if (flight == null) {
                throw new IllegalArgumentException("Flight cannot be null");
            }

            if (seat == null) {
                throw new IllegalArgumentException("Seat cannot be null");
            }

            if (passenger == null) {
                throw new IllegalArgumentException("Passenger cannot be null");
            }

            String seatKey = generateSeatKey(flight.getFlightNum(), seat.getSeatNum());

            if (reservedSeats.containsKey(seatKey)) {
                throw new IllegalStateException(
                        "Seat " + seat.getSeatNum() + " is already reserved for flight " + flight.getFlightNum());
            }

            if (seat.isReserved()) {
                throw new IllegalStateException("Seat " + seat.getSeatNum() + " is already marked as reserved");
            }

            Reservation reservation = new Reservation(reservationCode, flight, seat, passenger, dateOfReservation);
            reservations.put(reservationCode, reservation);
            reservedSeats.put(seatKey, seat);
            seat.setReserved(true);

            return reservation;
        }
    }

    public Reservation makeReservation(Reservation reservation) {
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation cannot be null");
        }

        return makeReservation(
                reservation.getReservationCode(),
                reservation.getFlight(),
                reservation.getSeat(),
                reservation.getPassenger(),
                reservation.getDateOfReservation());
    }

    public void cancelReservation(String reservationCode) {
        synchronized (lock) {
            if (reservationCode == null || reservationCode.trim().isEmpty()) {
                throw new IllegalArgumentException("Reservation code cannot be null or empty");
            }

            Reservation reservation = reservations.get(reservationCode);

            if (reservation == null) {
                throw new IllegalArgumentException("Reservation with code " + reservationCode + " does not exist");
            }

            Flight flight = reservation.getFlight();
            Seat seat = reservation.getSeat();

            String seatKey = generateSeatKey(flight.getFlightNum(), seat.getSeatNum());

            reservations.remove(reservationCode);
            reservedSeats.remove(seatKey);
            seat.setReserved(false);
        }
    }

    public Reservation getReservation(String reservationCode) {
        return reservations.get(reservationCode);
    }

    public List<Reservation> getAllReservations() {
        return new ArrayList<>(reservations.values());
    }

    public boolean reservationExists(String reservationCode) {
        return reservations.containsKey(reservationCode);
    }

    public int getReservationCount() {
        return reservations.size();
    }

    public List<Reservation> getReservationsByFlight(String flightNum) {
        List<Reservation> result = new ArrayList<>();

        for (Reservation reservation : reservations.values()) {
            if (reservation.getFlight() != null &&
                    reservation.getFlight().getFlightNum().equals(flightNum)) {
                result.add(reservation);
            }
        }

        return result;
    }

    public List<Reservation> getReservationsByPassenger(String passengerId) {
        List<Reservation> result = new ArrayList<>();

        for (Reservation reservation : reservations.values()) {
            if (reservation.getPassenger() != null &&
                    reservation.getPassenger().getPassangerId().equals(passengerId)) {
                result.add(reservation);
            }
        }

        return result;
    }

    public boolean isSeatReserved(String flightNum, String seatNum) {
        String seatKey = generateSeatKey(flightNum, seatNum);
        return reservedSeats.containsKey(seatKey);
    }

    public List<Seat> getReservedSeatsForFlight(String flightNum) {
        List<Seat> result = new ArrayList<>();

        for (Map.Entry<String, Seat> entry : reservedSeats.entrySet()) {
            if (entry.getKey().startsWith(flightNum + "_")) {
                result.add(entry.getValue());
            }
        }

        return result;
    }

    public int getReservedSeatCountForFlight(String flightNum) {
        int count = 0;

        for (String key : reservedSeats.keySet()) {
            if (key.startsWith(flightNum + "_")) {
                count++;
            }
        }

        return count;
    }

    public void clearAllReservations() {
        synchronized (lock) {
            for (Reservation reservation : reservations.values()) {
                if (reservation.getSeat() != null) {
                    reservation.getSeat().setReserved(false);
                }
            }
            reservations.clear();
            reservedSeats.clear();
        }
    }

    private String generateSeatKey(String flightNum, String seatNum) {
        return flightNum + "_" + seatNum;
    }
}
