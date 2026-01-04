package service;

import model.flight.*;
import java.time.*;
import java.util.*;

public class FlightManager {

    private Map<String, Flight> flights;

    public FlightManager() {
        this.flights = new HashMap<>();
    }

    public void createFlight(String flightNum, String departurePlace, String arrivalPlace,
            LocalDate date, LocalTime hour, int duration) {
        if (flightNum == null || flightNum.trim().isEmpty()) {
            throw new IllegalArgumentException("Flight number cannot be null or empty");
        }

        if (flights.containsKey(flightNum)) {
            throw new IllegalArgumentException("Flight with number " + flightNum + " already exists");
        }

        Flight flight = new Flight(flightNum, departurePlace, arrivalPlace, date, hour, duration);
        flights.put(flightNum, flight);
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
    }

    public void updateFlight(String flightNum, String departurePlace, String arrivalPlace,
            LocalDate date, LocalTime hour, int duration) {
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
        if (duration > 0) {
            flight.setDuration(duration);
        }
    }

    public void updateFlightDeparturePlace(String flightNum, String departurePlace) {
        Flight flight = flights.get(flightNum);

        if (flight == null) {
            throw new IllegalArgumentException("Flight with number " + flightNum + " does not exist");
        }

        flight.setDeparturePlace(departurePlace);
    }

    public void updateFlightArrivalPlace(String flightNum, String arrivalPlace) {
        Flight flight = flights.get(flightNum);

        if (flight == null) {
            throw new IllegalArgumentException("Flight with number " + flightNum + " does not exist");
        }

        flight.setArrivalPlace(arrivalPlace);
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
    }

    public void deleteFlight(String flightNum) {
        if (flightNum == null || flightNum.trim().isEmpty()) {
            throw new IllegalArgumentException("Flight number cannot be null or empty");
        }

        Flight removed = flights.remove(flightNum);

        if (removed == null) {
            throw new IllegalArgumentException("Flight with number " + flightNum + " does not exist");
        }
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
}
