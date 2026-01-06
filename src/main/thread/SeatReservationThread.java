package thread;

import model.flight.Plane;
import model.flight.Seat;
import service.SeatManager;

public class SeatReservationThread extends Thread {

    private Plane plane;
    private SeatManager seatManager;
    private Object lock;
    private boolean useSynchronization;
    private ReservationCallback callback;
    private volatile boolean reservationSuccess;
    private String reservedSeatNumber;

    public interface ReservationCallback {
        void onSeatReserved(String seatNumber, boolean success);

        void onReservationError(String error);
    }

    public SeatReservationThread(Plane plane, SeatManager seatManager, Object lock,
            boolean useSynchronization, ReservationCallback callback) {
        this.plane = plane;
        this.seatManager = seatManager;
        this.lock = lock;
        this.useSynchronization = useSynchronization;
        this.callback = callback;
        this.reservationSuccess = false;
        this.reservedSeatNumber = null;
    }

    @Override
    public void run() {
        try {
            if (useSynchronization) {
                synchronized (lock) {
                    performReservation();
                }
            } else {
                performReservation();
            }
        } catch (Exception e) {
            if (callback != null) {
                callback.onReservationError("Reservation failed: " + e.getMessage());
            }
        }
    }

    private void performReservation() {
        try {
            Seat selectedSeat = seatManager.getRandomAvailableSeat(plane);

            if (selectedSeat == null) {
                reservationSuccess = false;
                if (callback != null) {
                    callback.onReservationError("No available seats");
                }
                return;
            }

            if (!selectedSeat.isReserved()) {
                selectedSeat.setReserved(true);
                reservedSeatNumber = selectedSeat.getSeatNum();
                reservationSuccess = true;

                Thread.sleep(10);

                if (callback != null) {
                    callback.onSeatReserved(reservedSeatNumber, true);
                }
            } else {
                reservationSuccess = false;
                if (callback != null) {
                    callback.onSeatReserved(null, false);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            reservationSuccess = false;
        }
    }

    public boolean isReservationSuccessful() {
        return reservationSuccess;
    }

    public String getReservedSeatNumber() {
        return reservedSeatNumber;
    }
}
