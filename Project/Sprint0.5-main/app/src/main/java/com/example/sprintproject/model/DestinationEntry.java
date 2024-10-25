package com.example.sprintproject.model;

import java.util.Date;

public class DestinationEntry {

    private String destinationId;
    private String location;
    private Date startDate;
    private Date endDate;

    public DestinationEntry(String destinationId, String location, Date startDate, Date endDate) {
        this.destinationId = destinationId;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public String getLocation() {
        return location;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }
}
