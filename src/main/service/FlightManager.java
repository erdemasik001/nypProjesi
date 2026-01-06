package service;

import model.flight.*;
import java.time.*;
import java.util.*;

public class FlightManager {

    private Map<String, Flight> flights;

    private static final String FLIGHTS_FILE = "flights.txt";

    public FlightManager() {
        this.flights = new HashMap<>();
        loadFlights();
    }

    private void saveFlights() {
        List<String> lines = new ArrayList<>();

        for (Flight f : flights.values()) {
            String line = String.format(Locale.US, "%s,%s,%s,%s,%s,%d,%.2f",
                    f.getFlightNum(), f.getDeparturePlace(), f.getArrivalPlace(),
                    f.getDate().toString(), f.getHour().toString(), f.getDuration(), f.getPrice());
            lines.add(line);
        }
        util.FileManager.writeLines(util.FileManager.getDataFilePath(FLIGHTS_FILE), lines, false);
    }

    private void loadFlights() {
        List<String> lines = util.FileManager.readLines(util.FileManager.getDataFilePath(FLIGHTS_FILE));
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length >= 7) {
                String num = parts[0];
                String dep = parts[1];
                String arr = parts[2];
                LocalDate date = LocalDate.parse(parts[3]);
                LocalTime time = LocalTime.parse(parts[4]);
                int duration = Integer.parseInt(parts[5]);
                double price = Double.parseDouble(parts[6]);

                Flight flight = new Flight(num, dep, arr, date, time, duration, price);
                flights.put(num, flight);
            } else if (parts.length == 6) {

                String num = parts[0];
                String dep = parts[1];
                String arr = parts[2];
                LocalDate date = LocalDate.parse(parts[3]);
                LocalTime time = LocalTime.parse(parts[4]);
                int duration = Integer.parseInt(parts[5]);

                Flight flight = new Flight(num, dep, arr, date, time, duration, 1500.0);
                flights.put(num, flight);
            }
        }

        for (Flight f : flights.values()) {
            java.io.File seatFile = new java.io.File(util.FileManager.getDataFilePath(f.getFlightNum() + ".txt"));
            if (!seatFile.exists()) {
                createSeatFile(f.getFlightNum(), f.getDuration());
            }
        }
    }

    public void createFlight(String flightNum, String departurePlace, String arrivalPlace,
            LocalDate date, LocalTime hour, int duration, double price) {
        if (flightNum == null || flightNum.trim().isEmpty()) {
            throw new IllegalArgumentException("Flight number cannot be null or empty");
        }

        if (flights.containsKey(flightNum)) {
            throw new IllegalArgumentException("Flight with number " + flightNum + " already exists");
        }

        Flight flight = new Flight(flightNum, departurePlace, arrivalPlace, date, hour, duration, price);
        flights.put(flightNum, flight);
        saveFlights();

        createSeatFile(flightNum, duration);
    }

    public void createFlight(Flight flight) {
        if (flight == null) {
            throw new IllegalArgumentException("Flight cannot be null");
        }

        if (flight.getFlightNum() == null || flight.getFlightNum().trim().isEmpty()) {
            throw new IllegalArgumentException("Flight number cannot be null or empty");
        }

        if (flights.containsKey(flight.getFlightNum())) {
            throw new IllegalArgumentException("Flight with number " + flight.getFlightNum() + " already exists");
        }

        flights.put(flight.getFlightNum(), flight);
        saveFlights();
        createSeatFile(flight.getFlightNum(), flight.getDuration());
    }

    public void updateFlight(String flightNum, String departurePlace, String arrivalPlace,
            LocalDate date, LocalTime hour, int duration, double price) {
        Flight flight = flights.get(flightNum);

        if (flight == null) {
            throw new IllegalArgumentException("Flight with number " + flightNum + " does not exist");
        }

        if (departurePlace != null) {
            flight.setDeparturePlace(departurePlace);
        }
        if (arrivalPlace != null) {
            flight.setArrivalPlace(arrivalPlace);
        }
        if (date != null) {
            flight.setDate(date);
        }
        if (hour != null) {
            flight.setHour(hour);
        }
        if (duration > 0) {
            flight.setDuration(duration);
        }
        if (price >= 0) {
            flight.setPrice(price);
        }
        saveFlights();
    }

    public void updateFlightDeparturePlace(String flightNum, String departurePlace) {
        Flight flight = flights.get(flightNum);

        if (flight == null) {
            throw new IllegalArgumentException("Flight with number " + flightNum + " does not exist");
        }

        flight.setDeparturePlace(departurePlace);
        saveFlights();
    }

    public void updateFlightArrivalPlace(String flightNum, String arrivalPlace) {
        Flight flight = flights.get(flightNum);

        if (flight == null) {
            throw new IllegalArgumentException("Flight with number " + flightNum + " does not exist");
        }

        flight.setArrivalPlace(arrivalPlace);
        saveFlights();
    }

    public void updateFlightDuration(String flightNum, int duration) {
        Flight flight = flights.get(flightNum);

        if (flight == null) {
            throw new IllegalArgumentException("Flight with number " + flightNum + " does not exist");
        }

        if (duration <= 0) {
            throw new IllegalArgumentException("Duration must be positive");
        }

        flight.setDuration(duration);
        saveFlights();
    }

    public void deleteFlight(String flightNum) {
        if (flightNum == null || flightNum.trim().isEmpty()) {
            throw new IllegalArgumentException("Flight number cannot be null or empty");
        }

        Flight removed = flights.remove(flightNum);

        if (removed == null) {
            throw new IllegalArgumentException("Flight with number " + flightNum + " does not exist");
        }
        saveFlights();
    }

    public Flight getFlight(String flightNum) {
        return flights.get(flightNum);
    }

    public List<Flight> getAllFlights() {
        return new ArrayList<>(flights.values());
    }

    public boolean flightExists(String flightNum) {
        return flights.containsKey(flightNum);
    }

    public int getFlightCount() {
        return flights.size();
    }

    public List<Flight> getFlightsByDeparturePlace(String departurePlace) {
        List<Flight> result = new ArrayList<>();

        for (Flight flight : flights.values()) {
            if (flight.getDeparturePlace() != null &&
                    flight.getDeparturePlace().equalsIgnoreCase(departurePlace)) {
                result.add(flight);
            }
        }

        return result;
    }

    public List<Flight> getFlightsByArrivalPlace(String arrivalPlace) {
        List<Flight> result = new ArrayList<>();

        for (Flight flight : flights.values()) {
            if (flight.getArrivalPlace() != null &&
                    flight.getArrivalPlace().equalsIgnoreCase(arrivalPlace)) {
                result.add(flight);
            }
        }

        return result;
    }

    public List<Flight> getFlightsByRoute(String departurePlace, String arrivalPlace) {
        List<Flight> result = new ArrayList<>();

        for (Flight flight : flights.values()) {
            if (flight.getDeparturePlace() != null &&
                    flight.getDeparturePlace().equalsIgnoreCase(departurePlace) &&
                    flight.getArrivalPlace() != null &&
                    flight.getArrivalPlace().equalsIgnoreCase(arrivalPlace)) {
                result.add(flight);
            }
        }

        return result;
    }

    public List<Flight> getFlightsByDate(LocalDate date) {
        List<Flight> result = new ArrayList<>();

        for (Flight flight : flights.values()) {
            if (flight.getDate() != null && flight.getDate().equals(date)) {
                result.add(flight);
            }
        }

        return result;
    }

    public void clearAllFlights() {
        flights.clear();
    }

    private void createSeatFile(String flightNum, int capacity) {
        List<String> lines = new ArrayList<>();
        int seatCounter = 1;

        while (seatCounter <= capacity) {

            int row = (seatCounter - 1) / 6 + 1;
            char letter = (char) ('A' + (seatCounter - 1) % 6);
            String seatNum = row + String.valueOf(letter);

            String seatClass = (row <= 6) ? "BUSINESS" : "ECONOMY";

            lines.add(seatNum + "," + seatClass + ",false");
            seatCounter++;
        }
        util.FileManager.writeLines(util.FileManager.getDataFilePath(flightNum + ".txt"), lines, false);
    }

    public synchronized void updateSeatStatus(String flightNum, String seatNum, boolean occupied) {
        String filename = util.FileManager.getDataFilePath(flightNum + ".txt");
        List<String> lines = util.FileManager.readLines(filename);
        List<String> newLines = new ArrayList<>();
        boolean updated = false;

        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length >= 3 && parts[0].equals(seatNum)) {

                newLines.add(parts[0] + "," + parts[1] + "," + occupied);
                updated = true;
            } else {
                newLines.add(line);
            }
        }

        if (updated) {
            util.FileManager.writeLines(filename, newLines, false);
        } else {
            System.err.println("Seat " + seatNum + " not found in file " + filename);
        }
    }
}
