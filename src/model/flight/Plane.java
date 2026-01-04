package model.flight;
import java.util.*;

public class Plane {
    private String planeId;
    private String planeModel;
    private int capacity;
    private Map<String,Seat> seatMap;
    
    public Plane(String planeId, String planeModel, int capacity){
        this.planeId = planeId;
        this.planeModel = planeModel;
        this.capacity = capacity;
        this.seatMap = new HashMap<>();
    }
    public String getPlaneId(){
        return planeId;
    }
    public String getPlaneModel(){
        return planeModel;
    }
    public int getCapacity(){
        return capacity;
    }
    public Map<String,Seat> getSeatMap(){
        return seatMap;
    }
    public void setPlaneId(String planeId){
        this.planeId = planeId;
    }
    public void setPlaneModel(String planeModel){
        this.planeModel = planeModel;
    }
    public void setCapacity(int capacity){
        this.capacity = capacity;
    }
}
