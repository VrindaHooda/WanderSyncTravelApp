package com.example.sprintproject.model;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DestinationEntry {

    private String destinationId;
    private String location;
    private Date startDate;
    private Date endDate;
    private String duration;

    public DestinationEntry() {
        this.destinationId = "0";
        this.location = "Amsterdam";
        this.startDate = new Date(2024, 4, 4);
        this.endDate = new Date(2024, 4, 16) ;
        this.duration = "16 days";
    }

    public DestinationEntry(String destinationId, String location, Date startDate, Date endDate) {
        this.destinationId = destinationId;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
    }

    public String getDestinationId() {
        return destinationId;

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public long getDurationInDays() {
        long diffInMillis = endDate.getTime() - startDate.getTime();
        return TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
    }

    public String getDuration() {
        return duration;
    }
}
