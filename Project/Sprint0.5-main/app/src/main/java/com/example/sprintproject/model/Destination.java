package com.example.sprintproject.model;

import java.util.List;
import java.util.Objects;

public class Destination {

    private String location;
    private List<String> accommodations;
    private List<String> diningReservations;
    private String transportation;

    // Default constructor
    public Destination() {
    }

    // Parameterized constructor
    public Destination(String location, List<String> accommodations, List<String> diningReservations, String transportation) {
        this.location = location;
        this.accommodations = accommodations;
        this.diningReservations = diningReservations;
        this.transportation = transportation;
    }

    // Getters and setters
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getAccommodations() {
        return accommodations;
    }

    public void setAccommodations(List<String> accommodations) {
        this.accommodations = accommodations;
    }

    public List<String> getDiningReservations() {
        return diningReservations;
    }

    public void setDiningReservations(List<String> diningReservations) {
        this.diningReservations = diningReservations;
    }

    public String getTransportation() {
        return transportation;
    }

    public void setTransportation(String transportation) {
        this.transportation = transportation;
    }

    // toString method for easier debugging and logging
    @Override
    public String toString() {
        return "Destination{" +
                "location='" + location + '\'' +
                ", accommodations=" + accommodations +
                ", diningReservations=" + diningReservations +
                ", transportation='" + transportation + '\'' +
                '}';
    }

    // equals and hashCode methods for better comparison and storage in collections
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Destination that = (Destination) o;
        return Objects.equals(location, that.location) &&
                Objects.equals(accommodations, that.accommodations) &&
                Objects.equals(diningReservations, that.diningReservations) &&
                Objects.equals(transportation, that.transportation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, accommodations, diningReservations, transportation);
    }
}

