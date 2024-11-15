package com.example.sprintproject.model;

import java.util.Date;

public class TravelLog {
    private String location;
    private Date startDate;
    private Date endDate;

    // Default constructor required for calls to DataSnapshot.getValue(TravelLog.class)
    public TravelLog() {
    }

    public TravelLog(String location, Date startDate, Date endDate) {
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters and Setters
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
}

