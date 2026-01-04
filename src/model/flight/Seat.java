package model.flight;

public class Seat {
    private String seatNum;
    private SeatClass seatClass;
    private double price;
    private boolean reserveStatus;

    public Seat(String seatNum, SeatClass seatClass, double price, boolean reserveStatus){
        this.seatNum = seatNum;
        this.seatClass = seatClass;
        this.price = price;
        this.reserveStatus = reserveStatus;
    }
    public String getSeatNum(){
        return seatNum;
    }
    public SeatClass getSeatClass(){
        return seatClass;
    }
    public double getPrice(){
        return price;
    }
    public boolean isReserved(){
        return reserveStatus;
    }
    public void setReserved(boolean reserveStatus){
        this.reserveStatus = reserveStatus;
    }
}
