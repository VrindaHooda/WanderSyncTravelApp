package com.example.sprintproject.model;

public class TravelLogEntry {
    private String location;
    private String duration;
    private String startDate;
    private String endDate;


    public TravelLogEntry(String location, String duration) {
        this.location = location;
        this.duration = duration;
    }

    public TravelLogEntry(String location, String startDate, String endDate) {
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getLocation() {
        return location;
    }

    public String getDuration() {
        return duration;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
