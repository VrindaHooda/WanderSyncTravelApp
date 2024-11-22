package com.example.sprintproject.model;

import java.util.Date;

public class TravelLog {
    private String location;
    private Date startDate;
    private Date endDate;

    // Default constructor required for calls to DataSnapshot.getValue(TravelLog.class)
    /**
     * Default constructor required for deserialization, such as
     * calls to {@code DataSnapshot.getValue(TravelLog.class)} in Firebase.
     */
    public TravelLog() {
    }

    /**
     * Constructs a {@code TravelLog} with the specified details.
     *
     * @param location  the location of the travel
     * @param startDate the start date of the travel
     * @param endDate   the end date of the travel
     */
    public TravelLog(String location, Date startDate, Date endDate) {
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters and Setters
    /**
     * Returns the location of the travel.
     *
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location of the travel.
     *
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Returns the start date of the travel.
     *
     * @return the start date
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date of the travel.
     *
     * @param startDate the start date to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Returns the end date of the travel.
     *
     * @return the end date
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Sets the end date of the travel.
     *
     * @param endDate the end date to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}

