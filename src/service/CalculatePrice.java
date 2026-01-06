package service;

import model.flight.*;
import model.reservation.*;

public class CalculatePrice {

    private static final double BAGGAGE_PRICE_PER_KG = 15.0;
    private static final double ECONOMY_BAGGAGE_ALLOWANCE = 20.0;
    private static final double BUSINESS_BAGGAGE_ALLOWANCE = 30.0;
    private static final double BUSINESS_CLASS_MULTIPLIER = 2.5;

    public double calculateTotalPrice(Seat seat, Baggage baggage) {
        if (seat == null) {
            throw new IllegalArgumentException("Seat cannot be null");
        }

        // Koltuk fiyatını sınıfa göre (Economy / Business) ayarla
        // Economy -> basePrice
        // Business -> basePrice * BUSINESS_CLASS_MULTIPLIER
        double basePrice = calculateSeatPrice(seat.getPrice(), seat.getSeatClass());
        double baggageFee = calculateBaggageFee(seat.getSeatClass(), baggage);

        return basePrice + baggageFee;
    }

    public double calculateTotalPrice(Reservation reservation, Baggage baggage) {
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation cannot be null");
        }

        Seat seat = reservation.getSeat();
        if (seat == null) {
            throw new IllegalArgumentException("Reservation must have a valid seat");
        }

        return calculateTotalPrice(seat, baggage);
    }

    public double calculateBaggageFee(SeatClass seatClass, Baggage baggage) {
        if (baggage == null || baggage.getWeight() <= 0) {
            return 0.0;
        }

        double allowance = getBaggageAllowance(seatClass);
        double excessWeight = baggage.getWeight() - allowance;

        if (excessWeight <= 0) {
            return 0.0;
        }

        return excessWeight * BAGGAGE_PRICE_PER_KG;
    }

    public double getBaggageAllowance(SeatClass seatClass) {
        if (seatClass == null) {
            return ECONOMY_BAGGAGE_ALLOWANCE;
        }

        switch (seatClass) {
            case BUSINESS:
                return BUSINESS_BAGGAGE_ALLOWANCE;
            case ECONOMY:
            default:
                return ECONOMY_BAGGAGE_ALLOWANCE;
        }
    }

    public double calculateSeatPrice(double basePrice, SeatClass seatClass) {
        if (basePrice < 0) {
            throw new IllegalArgumentException("Base price cannot be negative");
        }

        if (seatClass == null) {
            return basePrice;
        }

        switch (seatClass) {
            case BUSINESS:
                return basePrice * BUSINESS_CLASS_MULTIPLIER;
            case ECONOMY:
            default:
                return basePrice;
        }
    }

    public double calculateExcessBaggageWeight(SeatClass seatClass, Baggage baggage) {
        if (baggage == null || baggage.getWeight() <= 0) {
            return 0.0;
        }

        double allowance = getBaggageAllowance(seatClass);
        double excessWeight = baggage.getWeight() - allowance;

        return Math.max(0.0, excessWeight);
    }

    public boolean isBaggageWithinAllowance(SeatClass seatClass, Baggage baggage) {
        if (baggage == null || baggage.getWeight() <= 0) {
            return true;
        }

        double allowance = getBaggageAllowance(seatClass);
        return baggage.getWeight() <= allowance;
    }

    public double getBaggagePricePerKg() {
        return BAGGAGE_PRICE_PER_KG;
    }

    public double getBusinessClassMultiplier() {
        return BUSINESS_CLASS_MULTIPLIER;
    }
}
