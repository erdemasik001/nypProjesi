package service;

import model.flight.*;
import java.time.*;
import java.util.*;

public class FlightSearchEngine {
    public List<Flight> searchByRoute(String departurePlace, String arrivalPlace, List<Flight> flights) {
        List<Flight> result = new ArrayList<>();

        if (flights == null) {
            return result;
        }
        for (Flight flight : flights) {
            if (flight == null) {
                continue;
            }
            if (flight.getDeparturePlace() != null && flight.getDeparturePlace().equalsIgnoreCase(departurePlace) && flight.getArrivalPlace() != null && flight.getArrivalPlace().equalsIgnoreCase(arrivalPlace)) {
                result.add(flight);
            }
        }
        return result;
    }
    public List<Flight> excludeDepartedFlights(List<Flight> flights , LocalDateTime now){
        List<Flight> result = new ArrayList<>();
        for (Flight flight : flights) {
            LocalDateTime departureDateTime = LocalDateTime.of(flight.getDate(),flight.getHour());

            if (departureDateTime.isAfter(now)) {
                result.add(flight);
            }
        }
        return result;
    }
}