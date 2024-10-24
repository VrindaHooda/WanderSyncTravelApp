package com.example.sprintproject.model;

import java.text.DateFormat;

public class DestinationEntry {
    private String location;
    private DateFormat startDate;
    private DateFormat endDate;

    public DestinationEntry() {
        this.location = "defaultLocation";
        this.startDate = DateFormat.getTimeInstance(00);
        this.endDate =  DateFormat.getTimeInstance(00);
    }
    public DestinationEntry(String location, DateFormat startDate, DateFormat endDate) {
        this.location = location;
        this.startDate = startDate;
        this.endDate =  endDate;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public DateFormat getStartDate() { return startDate; }
    public void setStartDate(DateFormat startDate) {
        this.startDate = startDate;
    }
    public DateFormat endDate() { return endDate; }
    public void setEndDate(DateFormat endDate) {
        this.endDate = endDate;
    }
}
