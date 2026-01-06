
import service.FlightSearchEngine;

import model.flight.Flight;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class FlightSearchEngineTest {

    private FlightSearchEngine searchEngine;
    private List<Flight> flights;

    @BeforeEach
    void setUp() {
        searchEngine = new FlightSearchEngine();
        flights = new ArrayList<>();

        // Create some sample flights
        // Note: Using dummy values for fields not relevant to search logic
        flights.add(new Flight("TK001", "Istanbul", "Ankara", null, LocalTime.now(), 0, 0));
        flights.add(new Flight("TK002", "Istanbul", "Izmir", null, LocalTime.now(), 0, 0));
        flights.add(new Flight("TK003", "Ankara", "Istanbul", null, LocalTime.now(), 0, 0));
    }

    @Test
    void testSearchByRoute_Found() {
        List<Flight> result = searchEngine.searchByRoute("Istanbul", "Ankara", flights);

        assertEquals(1, result.size());
        assertEquals("TK001", result.get(0).getFlightNum());
    }

    @Test
    void testSearchByRoute_NotFound() {
        List<Flight> result = searchEngine.searchByRoute("Izmir", "Ankara", flights);

        assertTrue(result.isEmpty(), "Should return empty list for non-existent route");
    }

    @Test
    void testSearchByRoute_CaseInsensitive() {
        List<Flight> result = searchEngine.searchByRoute("istanbul", "ANKARA", flights);

        assertEquals(1, result.size());
        assertEquals("TK001", result.get(0).getFlightNum());
    }

    @Test
    void testExcludeDepartedFlights_PastFlight() {
        // Setup a flight in the past
        Flight pastFlight = new Flight("OLD01", "A", "B", null, LocalTime.of(10, 0), 0, 0);
        // Assuming Flight.date is stored. Since Flight constructor doesn't take Date in
        // the simplified version seen in other files,
        // but FlightSearchEngine uses LocalDateTime.of(flight.getDate(),
        // flight.getHour()).
        // We need to ensure Flight has a Date.
        // Based on Flight.java seen previously or implied, we might need to set it.
        // If the simple constructor sets date to null, search engine might NPE.
        // Let's assume we can use setter or the constructor initializes it.
        // The viewed Flight.java constructor didn't strictly show date handling, but
        // let's assume setters work.

        pastFlight.setDate(java.time.LocalDate.now().minusDays(1));

        List<Flight> list = new ArrayList<>();
        list.add(pastFlight);

        List<Flight> result = searchEngine.excludeDepartedFlights(list, LocalDateTime.now());

        assertTrue(result.isEmpty(), "Past flight should be excluded");
    }

    @Test
    void testExcludeDepartedFlights_FutureFlight() {
        Flight futureFlight = new Flight("FUT01", "A", "B", null, LocalTime.of(10, 0), 0, 0);
        futureFlight.setDate(java.time.LocalDate.now().plusDays(1));

        List<Flight> list = new ArrayList<>();
        list.add(futureFlight);

        List<Flight> result = searchEngine.excludeDepartedFlights(list, LocalDateTime.now());

        assertEquals(1, result.size(), "Future flight should be included");
    }
}
