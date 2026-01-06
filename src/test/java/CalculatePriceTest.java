
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
        // Economy allowance is 20.0
        Seat seat = new Seat("1A", SeatClass.ECONOMY, 100.0, false);
        Baggage baggage = new Baggage(15.0); // Within limit

        double total = calculatePrice.calculateTotalPrice(seat, baggage);

        // Price should be just base price: 100.0
        assertEquals(100.0, total, 0.001, "Economy price within baggage limit should be base price");
    }

    @Test
    void testCalculateTotalPrice_Economy_ExcessBaggage() {
        // Economy allowance is 20.0, rate is 15.0 per kg
        Seat seat = new Seat("1A", SeatClass.ECONOMY, 100.0, false);
        Baggage baggage = new Baggage(25.0); // 5kg excess

        double total = calculatePrice.calculateTotalPrice(seat, baggage);

        // Base: 100.0
        // Excess: 5.0 * 15.0 = 75.0
        // Total: 175.0
        assertEquals(175.0, total, 0.001, "Economy price with excess baggage calculated incorrectly");
    }

    @Test
    void testCalculateTotalPrice_Business_NoExcessBaggage() {
        // Business multiplier is 2.5
        // Business allowance is 30.0
        Seat seat = new Seat("1B", SeatClass.BUSINESS, 100.0, false);
        Baggage baggage = new Baggage(25.0); // Within limit

        double total = calculatePrice.calculateTotalPrice(seat, baggage);

        // Base: 100.0 * 2.5 = 250.0
        // Excess: 0
        // Total: 250.0
        assertEquals(250.0, total, 0.001, "Business price within baggage limit should use multiplier");
    }

    @Test
    void testCalculateTotalPrice_Business_ExcessBaggage() {
        // Business multiplier is 2.5
        // Business allowance is 30.0
        Seat seat = new Seat("1B", SeatClass.BUSINESS, 100.0, false);
        Baggage baggage = new Baggage(35.0); // 5kg excess

        double total = calculatePrice.calculateTotalPrice(seat, baggage);

        // Base: 100.0 * 2.5 = 250.0
        // Excess: 5.0 * 15.0 = 75.0
        // Total: 325.0
        assertEquals(325.0, total, 0.001, "Business price with excess baggage calculated incorrectly");
    }

    @Test
    void testCalculateTotalPrice_NullBaggage() {
        Seat seat = new Seat("1A", SeatClass.ECONOMY, 100.0, false);
        double total = calculatePrice.calculateTotalPrice(seat, null);

        assertEquals(100.0, total, 0.001, "Price with null baggage should be base price");
    }
}
