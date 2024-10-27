package com.example.sprintproject.model;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DestinationEntry {

    private String destinationId;
    private String location;
    private Date startDate;
    private Date endDate;
    private String duration;

    public DestinationEntry() {
    }

    public DestinationEntry(String destinationId, String location, Date startDate, Date endDate) {
        this.destinationId = destinationId;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = calculateDuration(startDate, endDate);
    }

    public DestinationEntry(String destinationId, int duration, Date startDate, Date endDate) {
        this.destinationId = destinationId;
        this.duration = String.valueOf(duration);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

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
        this.duration = calculateDuration(startDate, this.endDate); // Recalculate duration
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
        this.duration = calculateDuration(this.startDate, endDate); // Recalculate duration
    }

    public String getDuration() {
        return duration;
    }

    private String calculateDuration(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return "N/A"; // or handle this case as needed
        }
        long durationInMillis = endDate.getTime() - startDate.getTime();
        long days = TimeUnit.MILLISECONDS.toDays(durationInMillis);
        return String.valueOf(days);
    }

    @Override
    public String toString() {
        return "DestinationEntry{" +
                "destinationId='" + destinationId + '\'' +
                ", location='" + location + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", duration='" + duration + '\'' +
                '}';
    }
}
