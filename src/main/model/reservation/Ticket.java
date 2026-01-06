package model.reservation;
import model.flight.*;


public class Ticket {
    private String ticketId;
    private Reservation reservation;
    private double price;
    private double baggageAllowance;

    public Ticket(String ticketId, Reservation reservation, double price, double baggageAllowance){
        this.ticketId = ticketId;
        this.reservation = reservation;
        this.price = price;
        this.baggageAllowance = baggageAllowance;
    }
    public String getTicketId(){
        return ticketId;
    }
    public Reservation getReservation(){
        return reservation;
    }
    public double getPrice(){
        return price;
    }
    public double getBaggageAllowance(){
        return baggageAllowance;
    }
    public void setTicketId(String ticketId){
        this.ticketId = ticketId;
    }
    public void setReservation(Reservation reservation){
        this.reservation = reservation;
    }
    public void setPrice(double price){
        this.price=price;
    }
    public void setBaggageAllowance(double baggageAllowance){
        this.baggageAllowance=baggageAllowance;
    }
    public boolean hasReservation(){
        if (reservation != null){
            return true;
        }else{
            return false;
        }
    }
    public boolean isBusinessClass(){
        if(reservation==null){
            return false;
        }else{
            return reservation.getSeat().getSeatClass() == SeatClass.BUSINESS;
        }
    }
    public boolean isEconomyClass(){
        if(reservation==null){
            return false;
        }else{
            return reservation.getSeat().getSeatClass() == SeatClass.ECONOMY;
        }
    }
}
