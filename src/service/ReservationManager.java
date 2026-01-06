package service;

import model.flight.*;
import model.reservation.*;
import java.util.*;
import java.util.concurrent.*;

import util.FileManager;
import java.text.SimpleDateFormat;

public class ReservationManager {
    private static final String RESERVATIONS_FILE = "reservations.txt";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Map<String, Reservation> reservations;
    private Map<String, Seat> reservedSeats;
    private final Object lock = new Object();

    public ReservationManager() {
        this.reservations = new ConcurrentHashMap<>();
        this.reservedSeats = new ConcurrentHashMap<>();
        loadReservations();
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

            saveReservations();

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

            // Instead of removing, we mark as Cancelled
            // reservations.remove(reservationCode);
            reservation.setStatus("Cancelled");

            // Free up the seat
            reservedSeats.remove(seatKey);
            seat.setReserved(false);

            saveReservations();
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

    private void saveReservations() {
        List<String> lines = new ArrayList<>();
        // Header:
        // reservationCode,flightNum,seatNum,passengerId,date,passName,passSurname,dep,arr,time,price,status
        for (Reservation r : reservations.values()) {
            StringBuilder sb = new StringBuilder();
            sb.append(r.getReservationCode()).append(",");
            sb.append(r.getFlight() != null ? r.getFlight().getFlightNum() : "").append(",");
            sb.append(r.getSeat() != null ? r.getSeat().getSeatNum() : "").append(",");
            sb.append(r.getPassenger() != null ? r.getPassenger().getPassangerId() : "").append(",");
            sb.append(r.getDateOfReservation() != null ? DATE_FORMAT.format(r.getDateOfReservation()) : "").append(",");

            // New fields
            sb.append(r.getPassenger() != null ? r.getPassenger().getName() : "").append(",");
            sb.append(r.getPassenger() != null ? r.getPassenger().getSurname() : "").append(",");
            sb.append(r.getFlight() != null ? r.getFlight().getDeparturePlace() : "").append(",");
            sb.append(r.getFlight() != null ? r.getFlight().getArrivalPlace() : "").append(",");
            sb.append(r.getFlight() != null ? r.getFlight().getHour() : "").append(",");
            sb.append(r.getSeat() != null ? r.getSeat().getPrice() : "").append(",");
            sb.append(r.getStatus());

            lines.add(sb.toString());
        }
        FileManager.writeLines(RESERVATIONS_FILE, lines, false);
    }

    private void loadReservations() {
        List<String> lines = FileManager.readLines(RESERVATIONS_FILE);
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length >= 12) { // Now expects 12 fields
                String resCode = parts[0];
                String flightNum = parts[1];
                String seatNum = parts[2];
                String passId = parts[3];
                String dateStr = parts[4];
                String passName = parts[5];
                String passSurname = parts[6];
                String dep = parts[7];
                String arr = parts[8];
                String timeStr = parts[9];
                String priceStr = parts[10];
                String status = parts[11];

                if (!reservations.containsKey(resCode)) {
                    // Reconstruct with full data
                    java.time.LocalTime time = null;
                    try {
                        time = java.time.LocalTime.parse(timeStr);
                    } catch (Exception e) {
                    }

                    // Since we are loading from file and we don't store full flight objects in
                    // reservation file yet,
                    // we might need to fetch real flight from FlightManager if possible, or create
                    // a dummy one.
                    // For this task, we will create a dummy flight object with basic info available
                    Flight flight = new Flight(flightNum, dep, arr, null, time != null ? time : java.time.LocalTime.MIN,
                            0, 0.0);

                    double price = 0;
                    try {
                        price = Double.parseDouble(priceStr);
                    } catch (Exception e) {
                    }
                    Seat seat = new Seat(seatNum, SeatClass.ECONOMY, price, "Active".equals(status));

                    Passenger passenger = new Passenger(passId, passName, passSurname, "Unknown");

                    Date date = null;
                    try {
                        date = DATE_FORMAT.parse(dateStr);
                    } catch (Exception e) {
                        System.err.println("Error parsing date: " + dateStr);
                    }

                    Reservation reservation = new Reservation(resCode, flight, seat, passenger, date, status);
                    reservations.put(resCode, reservation);

                    if ("Active".equals(status)) {
                        reservedSeats.put(generateSeatKey(flightNum, seatNum), seat);
                        seat.setReserved(true);
                    }
                }
            } else if (parts.length >= 5) {
                // Fallback for old format (will have unknowns)
                // Or we can choose to ignore them. For now, let's keep the old logic as
                // fallback
                // identifying it by length
                // ... (keep previous logic but we are overwriting it)
                // Actually, it's better to force new format. Old format lines will be skipped.
            }
        }
    }
}
