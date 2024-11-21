package com.example.sprintproject.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Destination {
    private String id;
    private String location;
    private List<String> accommodations;
    private List<String> diningReservations;
    private String transportation;

    private Date startDate;
    private Date endDate;


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

    public Destination(String location, List<String> accommodations, List<String> diningReservations, String transportation, Date startDate, Date endDate) {
        this.location = location;
        this.accommodations = accommodations;
        this.diningReservations = diningReservations;
        this.transportation = transportation;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Destination(String id, String location, Date startDate, Date endDate) {
        this.id = id;
        this.location = location;
        this.accommodations = new ArrayList<>(); // Default
        this.diningReservations = new ArrayList<>(); // Default
        this.transportation = ""; // Default
        this.startDate = startDate;
        this.endDate = endDate;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }


    // toString method for easier debugging and logging
    @Override
    public String toString() {
        return "Destination{" +
                "location='" + location + '\'' +
                ", accommodations=" + accommodations +
                ", diningReservations=" + diningReservations +
                ", transportation='" + transportation + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
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

