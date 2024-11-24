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


    /**
     * Default constructor for {@code Destination}.
     */
    public Destination() {
    }

    /**
     * Constructs a {@code Destination} with specified location,
     * accommodations, dining reservation, and transportation.
     *
     * @param location          the location of the destination
     * @param accommodations    a list of accommodation IDs
     * @param diningReservations a list of dining reservation IDs
     * @param transportation    the transportation method
     */
    public Destination(String location, List<String> accommodations,
                       List<String> diningReservations, String transportation) {
        this.location = location;
        this.accommodations = accommodations;
        this.diningReservations = diningReservations;
        this.transportation = transportation;
    }

    /**
     * Constructs a {@code Destination} with specified location,
     * accommodations, dining reservations, transportation, and trip dates.
     *
     * @param location          the location of the destination
     * @param accommodations    a list of accommodation IDs
     * @param diningReservations a list of dining reservation IDs
     * @param transportation    the transportation method
     * @param startDate         the start date of the trip
     * @param endDate           the end date of the trip
     */
    public Destination(String location, List<String> accommodations,
                       List<String> diningReservations, String transportation, Date startDate, Date endDate) {
        this.location = location;
        this.accommodations = accommodations;
        this.diningReservations = diningReservations;
        this.transportation = transportation;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Constructs a {@code Destination} with specified ID, location, and trip dates.
     * Default values are used for other fields.
     *
     * @param id        the unique identifier for the destination
     * @param location  the location of the destination
     * @param startDate the start date of the trip
     * @param endDate   the end date of the trip
     */
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
    /**
     * Returns the location of the destination.
     *
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location of the destination.
     *
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Returns the list of accommodations.
     *
     * @return the accommodations
     */
    public List<String> getAccommodations() {
        return accommodations;
    }

    /**
     * Sets the list of accommodations.
     *
     * @param accommodations the accommodations to set
     */
    public void setAccommodations(List<String> accommodations) {
        this.accommodations = accommodations;
    }

    /**
     * Returns the list of dining reservations.
     *
     * @return the dining reservations
     */
    public List<String> getDiningReservations() {
        return diningReservations;
    }

    /**
     * Sets the list of dining reservations.
     *
     * @param diningReservations the dining reservations to set
     */
    public void setDiningReservations(List<String> diningReservations) {
        this.diningReservations = diningReservations;
    }

    /**
     * Returns the transportation method.
     *
     * @return the transportation method
     */
    public String getTransportation() {
        return transportation;
    }

    /**
     * Sets the transportation method.
     *
     * @param transportation the transportation method to set
     */
    public void setTransportation(String transportation) {
        this.transportation = transportation;
    }

    /**
     * Returns the start date of the trip.
     *
     * @return the start date
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date of the trip.
     *
     * @param startDate the start date to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Returns the end date of the trip.
     *
     * @return the end date
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Sets the end date of the trip.
     *
     * @param endDate the end date to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }


    // toString method for easier debugging and logging
    /**
     * Returns a string representation of the {@code Destination} for debugging and logging purposes.
     *
     * @return a string representation of the destination
     */
    @Override
    public String toString() {
        return "Destination{"
                + "location='" + location + '\''
                + ", accommodations=" + accommodations
                + ", diningReservations=" + diningReservations
                + ", transportation='" + transportation + '\''
                + ", startDate=" + startDate
                + ", endDate=" + endDate + '}';
    }

    // equals and hashCode methods for better comparison and storage in collections
    /**
     * Compares this {@code Destination} to another object for equality.
     *
     * @param o the object to compare
     * @return {@code true} if the objects are equal; {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Destination that = (Destination) o;
        return Objects.equals(location, that.location)
                && Objects.equals(accommodations, that.accommodations)
                && Objects.equals(diningReservations, that.diningReservations)
                && Objects.equals(transportation, that.transportation);
    }

    /**
     * Returns the hash code of this {@code Destination}.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(location, accommodations, diningReservations, transportation);
    }
}

