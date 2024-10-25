package com.example.sprintproject.model;

public class DestinationEntry {
    private String userId;
    private String location;
    private String startDate;
    private String endDate;
    private String duration;

    public DestinationEntry() {
        // Default constructor required for Firebase
    }

    public DestinationEntry(String userId, String startDate, String endDate, String duration) {
        this.userId = userId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
