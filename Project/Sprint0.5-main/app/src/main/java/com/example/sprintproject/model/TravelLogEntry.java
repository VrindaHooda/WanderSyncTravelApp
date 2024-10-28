package com.example.sprintproject.model;

public class TravelLogEntry {
    private String location;
    private String duration;

    public TravelLogEntry(String location, String duration) {
        this.location = location;
        this.duration = duration;
    }

    public String getLocation() {
        return location;
    }

    public String getDuration() {
        return duration;
    }
}
