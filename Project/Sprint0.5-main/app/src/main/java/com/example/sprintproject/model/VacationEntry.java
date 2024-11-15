package com.example.sprintproject.model;

import java.util.Date;

public class VacationEntry {
    private Date startDate;
    private Date endDate;
    private int duration;

    // Default constructor required for calls to DataSnapshot.getValue(VacationEntry.class)
    public VacationEntry() {
    }

    public VacationEntry(Date startDate, Date endDate, int duration) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
    }

    // Getters and Setters
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}

