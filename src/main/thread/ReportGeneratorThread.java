package thread;

import service.FlightManager;
import service.SeatManager;
import model.flight.Flight;
import model.flight.Plane;
import model.flight.Seat;
import model.flight.SeatClass;
import java.util.List;

public class ReportGeneratorThread extends Thread {

    private FlightManager flightManager;
    private SeatManager seatManager;

    private ReportCallback callback;
    private volatile boolean isRunning;

    public interface ReportCallback {
        void onReportStarted();

        void onReportCompleted(String report);

        void onReportError(String error);
    }

    public ReportGeneratorThread(FlightManager flightManager,
            SeatManager seatManager,
            ReportCallback callback) {
        this.flightManager = flightManager;
        this.seatManager = seatManager;
        this.callback = callback;
        this.isRunning = false;
    }

    @Override
    public void run() {
        isRunning = true;

        try {
            if (callback != null) {
                callback.onReportStarted();
            }

            String report = generateOccupancyReport();

            if (callback != null) {
                callback.onReportCompleted(report);
            }

        } catch (Exception e) {
            if (callback != null) {
                callback.onReportError("Error generating report: " + e.getMessage());
            }
        } finally {
            isRunning = false;
        }
    }

    private String generateOccupancyReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== FLIGHT OCCUPANCY REPORT ===\n\n");

        try {
            Thread.sleep(2000);

            List<Flight> allFlights = flightManager.getAllFlights();

            if (allFlights == null || allFlights.isEmpty()) {
                report.append("No flights available for reporting.\n");
                return report.toString();
            }

            int totalFlights = 0;
            int totalSeats = 0;
            int totalOccupied = 0;
            int totalAvailable = 0;

            report.append(String.format("%-15s %-20s %-20s %10s %10s %10s %12s\n",
                    "Flight No", "Departure", "Arrival", "Total", "Occupied", "Available", "Occupancy %"));
            report.append("-".repeat(110)).append("\n");

            for (Flight flight : allFlights) {
                Thread.sleep(100);

                String flightNum = flight.getFlightNum();
                // Dynamically load plane data from file
                Plane plane = new Plane(flightNum, "Generic", 180);
                List<String> lines = util.FileManager.readLines("src/" + flightNum + ".txt");

                if (lines != null) {
                    for (String line : lines) {
                        try {
                            String[] parts = line.split(",");
                            if (parts.length >= 3) {
                                String seatNum = parts[0];
                                SeatClass seatClass = SeatClass.valueOf(parts[1]); // e.g. ECONOMY
                                boolean occupied = Boolean.parseBoolean(parts[2]);

                                Seat seat = new Seat(seatNum, seatClass, 0.0, occupied);
                                plane.getSeatMap().put(seatNum, seat);
                            }
                        } catch (Exception e) {
                            // Ignore malformed lines
                        }
                    }
                }

                int total = seatManager.getTotalSeatsCount(plane);
                int occupied = seatManager.getReservedSeatsCount(plane);
                int available = seatManager.getAvailableSeatsCount(plane);
                double occupancyRate = total > 0 ? (occupied * 100.0 / total) : 0.0;

                report.append(String.format("%-15s %-20s %-20s %10d %10d %10d %11.2f%%\n",
                        flightNum,
                        flight.getDeparturePlace(),
                        flight.getArrivalPlace(),
                        total,
                        occupied,
                        available,
                        occupancyRate));

                totalFlights++;
                totalSeats += total;
                totalOccupied += occupied;
                totalAvailable += available;
            }

            double overallOccupancyRate = totalSeats > 0 ? (totalOccupied * 100.0 / totalSeats) : 0.0;

            report.append("-".repeat(110)).append("\n");
            report.append(String.format("\nSUMMARY:\n"));
            report.append(String.format("Total Flights: %d\n", totalFlights));
            report.append(String.format("Total Seats: %d\n", totalSeats));
            report.append(String.format("Total Occupied: %d\n", totalOccupied));
            report.append(String.format("Total Available: %d\n", totalAvailable));
            report.append(String.format("Overall Occupancy Rate: %.2f%%\n", overallOccupancyRate));

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            report.append("\nReport generation was interrupted.\n");
        }

        return report.toString();
    }

    public boolean isReportRunning() {
        return isRunning;
    }

    public void stopReport() {
        if (isRunning) {
            this.interrupt();
        }
    }
}
