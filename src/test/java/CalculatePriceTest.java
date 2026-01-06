
import service.CalculatePrice;

import model.flight.Seat;
import model.flight.SeatClass;
import model.reservation.Baggage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CalculatePriceTest {

    private CalculatePrice calculatePrice;

    @BeforeEach
    void setUp() {
        calculatePrice = new CalculatePrice();
    }

    @Test
    void testCalculateTotalPrice_Economy_NoExcessBaggage() {
        Seat seat = new Seat("1A", SeatClass.ECONOMY, 100.0, false);
        Baggage baggage = new Baggage(15.0);

        double total = calculatePrice.calculateTotalPrice(seat, baggage);

        assertEquals(100.0, total, 0.001, "Economy price within baggage limit should be base price");
    }

    @Test
    void testCalculateTotalPrice_Economy_ExcessBaggage() {
        Seat seat = new Seat("1A", SeatClass.ECONOMY, 100.0, false);
        Baggage baggage = new Baggage(25.0);

        double total = calculatePrice.calculateTotalPrice(seat, baggage);

        assertEquals(175.0, total, 0.001, "Economy price with excess baggage calculated incorrectly");
    }

    @Test
    void testCalculateTotalPrice_Business_NoExcessBaggage() {
        Seat seat = new Seat("1B", SeatClass.BUSINESS, 100.0, false);
        Baggage baggage = new Baggage(25.0);

        double total = calculatePrice.calculateTotalPrice(seat, baggage);

        assertEquals(250.0, total, 0.001, "Business price within baggage limit should use multiplier");
    }

    @Test
    void testCalculateTotalPrice_Business_ExcessBaggage() {
        Seat seat = new Seat("1B", SeatClass.BUSINESS, 100.0, false);
        Baggage baggage = new Baggage(35.0);

        double total = calculatePrice.calculateTotalPrice(seat, baggage);

        assertEquals(325.0, total, 0.001, "Business price with excess baggage calculated incorrectly");
    }

    @Test
    void testCalculateTotalPrice_NullBaggage() {
        Seat seat = new Seat("1A", SeatClass.ECONOMY, 100.0, false);
        double total = calculatePrice.calculateTotalPrice(seat, null);

        assertEquals(100.0, total, 0.001, "Price with null baggage should be base price");
    }
}
