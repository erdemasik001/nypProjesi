import service.SeatManager;
import model.flight.Plane;
import model.flight.Seat;
import model.flight.SeatClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SeatManagerTest {

    private SeatManager seatManager;
    private Plane plane;

    @BeforeEach
    void setUp() {
        seatManager = new SeatManager();
        plane = new Plane("P001", "Boeing 737", 100);
        // Add some seats using SeatManager
        seatManager.createSeatingArrangement(plane, 5, 5, 100.0, 200.0);
    }

    @Test
    void testEmptySeatsCountDecreaseAfterReservation() {
        int initialEmpty = seatManager.getAvailableSeatsCount(plane);
        assertEquals(10, initialEmpty, "Should start with 10 empty seats");

        // Reserve a seat
        Seat seat = seatManager.getSeat(plane, "1A");
        assertNotNull(seat, "Seat 1A should exist");
        seat.setReserved(true); // Manually reserve since SeatManager just reads the state

        int newEmpty = seatManager.getAvailableSeatsCount(plane);
        assertEquals(9, newEmpty, "Empty seats count should decrease by 1");
    }

    @Test
    void testGetAvailableSeatsCount_AllEmpty() {
        assertEquals(10, seatManager.getAvailableSeatsCount(plane));
    }

    @Test
    void testExceptionWhenRemovingNonExistentSeat() {
        // SeatManager.removeSeatFromPlane throws exception if seat doesn't exist
        assertThrows(IllegalArgumentException.class, () -> {
            seatManager.removeSeatFromPlane(plane, "99Z");
        }, "Should throw exception when trying to remove a seat that doesn't exist");
    }

    @Test
    void testAddSeatToPlane_DuplicateException() {
        assertThrows(IllegalArgumentException.class, () -> {
            seatManager.addSeatToPlane(plane, "1A", SeatClass.BUSINESS, 200.0);
        }, "Should throw exception when adding a duplicate seat");
    }

    @Test
    void testGetSeat_NonExistent() {
        Seat seat = seatManager.getSeat(plane, "99X");
        assertNull(seat, "Should return null for non-existent seat via getSeat");
    }
}
