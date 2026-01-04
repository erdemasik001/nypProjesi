package service;

import model.flight.*;
import java.util.*;

public class SeatManager {

    private Map<String, Plane> planes;

    public SeatManager() {
        this.planes = new HashMap<>();
    }

    public void createSeatingArrangement(Plane plane, int economySeats, int businessSeats,
            double economyBasePrice, double businessBasePrice) {
        if (plane == null) {
            throw new IllegalArgumentException("Plane cannot be empty");
        }

        if (economySeats < 0 || businessSeats < 0) {
            throw new IllegalArgumentException("Seat counts cannot be negative");
        }

        if (economySeats + businessSeats > plane.getCapacity()) {
            throw new IllegalArgumentException("Total seats exceed plane capacity");
        }

        Map<String, Seat> seatMap = plane.getSeatMap();
        seatMap.clear();

        int seatCounter = 1;

        for (int i = 0; i < businessSeats; i++) {
            String seatNum = generateSeatNumber(seatCounter++);
            Seat seat = new Seat(seatNum, SeatClass.BUSINESS, businessBasePrice, false);
            seatMap.put(seatNum, seat);
        }

        for (int i = 0; i < economySeats; i++) {
            String seatNum = generateSeatNumber(seatCounter++);
            Seat seat = new Seat(seatNum, SeatClass.ECONOMY, economyBasePrice, false);
            seatMap.put(seatNum, seat);
        }

        planes.put(plane.getPlaneId(), plane);
    }

    public void addSeatToPlane(Plane plane, String seatNum, SeatClass seatClass, double price) {
        if (plane == null) {
            throw new IllegalArgumentException("Plane cannot be empty");
        }

        if (seatNum == null || seatNum.trim().isEmpty()) {
            throw new IllegalArgumentException("Seat number cannot be null or empty");
        }

        Map<String, Seat> seatMap = plane.getSeatMap();

        if (seatMap.containsKey(seatNum)) {
            throw new IllegalArgumentException("Seat " + seatNum + " already exists on this plane");
        }

        if (seatMap.size() >= plane.getCapacity()) {
            throw new IllegalStateException("Plane has reached maximum capacity");
        }

        Seat seat = new Seat(seatNum, seatClass, price, false);
        seatMap.put(seatNum, seat);
    }

    public void removeSeatFromPlane(Plane plane, String seatNum) {
        if (plane == null) {
            throw new IllegalArgumentException("Plane cannot be empty");
        }

        if (seatNum == null || seatNum.trim().isEmpty()) {
            throw new IllegalArgumentException("Seat number cannot be null or empty");
        }

        Map<String, Seat> seatMap = plane.getSeatMap();
        Seat removed = seatMap.remove(seatNum);

        if (removed == null) {
            throw new IllegalArgumentException("Seat " + seatNum + " does not exist on this plane");
        }

        if (removed.isReserved()) {
            throw new IllegalStateException("Cannot remove reserved seat " + seatNum);
        }
    }

    public int getAvailableSeatsCount(Plane plane) {
        if (plane == null) {
            throw new IllegalArgumentException("Plane cannot be empty");
        }

        int count = 0;
        for (Seat seat : plane.getSeatMap().values()) {
            if (!seat.isReserved()) {
                count++;
            }
        }

        return count;
    }

    public int getTotalSeatsCount(Plane plane) {
        if (plane == null) {
            throw new IllegalArgumentException("Plane cannot be empty");
        }

        return plane.getSeatMap().size();
    }

    public int getReservedSeatsCount(Plane plane) {
        if (plane == null) {
            throw new IllegalArgumentException("Plane cannot be empty");
        }

        int count = 0;
        for (Seat seat : plane.getSeatMap().values()) {
            if (seat.isReserved()) {
                count++;
            }
        }

        return count;
    }

    public List<Seat> getAvailableSeats(Plane plane) {
        if (plane == null) {
            throw new IllegalArgumentException("Plane cannot be empty");
        }

        List<Seat> availableSeats = new ArrayList<>();
        for (Seat seat : plane.getSeatMap().values()) {
            if (!seat.isReserved()) {
                availableSeats.add(seat);
            }
        }

        return availableSeats;
    }

    public List<Seat> getReservedSeats(Plane plane) {
        if (plane == null) {
            throw new IllegalArgumentException("Plane cannot be empty");
        }

        List<Seat> reservedSeats = new ArrayList<>();
        for (Seat seat : plane.getSeatMap().values()) {
            if (seat.isReserved()) {
                reservedSeats.add(seat);
            }
        }

        return reservedSeats;
    }

    public List<Seat> getSeatsByClass(Plane plane, SeatClass seatClass) {
        if (plane == null) {
            throw new IllegalArgumentException("Plane cannot be empty");
        }

        List<Seat> seats = new ArrayList<>();
        for (Seat seat : plane.getSeatMap().values()) {
            if (seat.getSeatClass() == seatClass) {
                seats.add(seat);
            }
        }

        return seats;
    }

    public int getAvailableSeatsByClass(Plane plane, SeatClass seatClass) {
        if (plane == null) {
            throw new IllegalArgumentException("Plane cannot be empty");
        }

        int count = 0;
        for (Seat seat : plane.getSeatMap().values()) {
            if (seat.getSeatClass() == seatClass && !seat.isReserved()) {
                count++;
            }
        }

        return count;
    }

    public Seat getSeat(Plane plane, String seatNum) {
        if (plane == null) {
            throw new IllegalArgumentException("Plane cannot be empty");
        }

        return plane.getSeatMap().get(seatNum);
    }

    public boolean isSeatAvailable(Plane plane, String seatNum) {
        if (plane == null) {
            throw new IllegalArgumentException("Plane cannot be empty");
        }

        Seat seat = plane.getSeatMap().get(seatNum);
        return seat != null && !seat.isReserved();
    }

    public boolean seatExists(Plane plane, String seatNum) {
        if (plane == null) {
            throw new IllegalArgumentException("Plane cannot be empty");
        }

        return plane.getSeatMap().containsKey(seatNum);
    }

    public void clearSeatingArrangement(Plane plane) {
        if (plane == null) {
            throw new IllegalArgumentException("Plane cannot be empty");
        }

        plane.getSeatMap().clear();
    }

    private String generateSeatNumber(int number) {
        int row = (number - 1) / 6 + 1;
        char letter = (char) ('A' + (number - 1) % 6);
        return row + String.valueOf(letter);
    }
}
