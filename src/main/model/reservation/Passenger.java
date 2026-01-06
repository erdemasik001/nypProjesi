package model.reservation;

public class Passenger {
    private String passangerId;
    private String name;
    private String surname;
    private String contactInfo;

    public Passenger(String passangerId, String name, String surname, String contactInfo) {
        this.passangerId = passangerId;
        this.name = name;
        this.surname = surname;
        this.contactInfo = contactInfo;
    }

    public String getPassangerId() {
        return passangerId;
    }
    public String getName() {
        return name;
    }
    public String getSurname() {
        return surname;
    }
    public String getContactInfo() {
        return contactInfo;
    }

    public void setPassangerId(String passangerId) {
        this.passangerId = passangerId;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }
}
