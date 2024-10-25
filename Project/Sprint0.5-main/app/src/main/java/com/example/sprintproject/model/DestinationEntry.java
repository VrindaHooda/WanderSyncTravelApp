package com.example.sprintproject.model;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DestinationEntry {

    private String destinationId;
    private String location;
    private Date startDate;
    private Date endDate;

    public DestinationEntry() {
        this.destinationId = "0";
        this.location = "Amsterdam";
        this.startDate = new Date(2024, 4, 4);
        this.endDate = new Date(2024, 4, 16) ;;
    }

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

    // New method to calculate the duration in days
    public long getDurationInDays() {
        long diffInMillis = endDate.getTime() - startDate.getTime();
        return TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
    }
}
