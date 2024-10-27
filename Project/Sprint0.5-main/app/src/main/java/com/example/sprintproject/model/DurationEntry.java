package com.example.sprintproject.model;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DurationEntry {

    private String vacationId;
    private int duration;
    private Date startDate;
    private Date endDate;

    public DurationEntry(String vacationId, int duration, Date startDate, Date endDate) {
        this.vacationId = vacationId;
        this.duration = duration;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getVacationId() {
        return vacationId;
    }

    public void setVacationId(String vacationId) {
        this.vacationId = vacationId;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
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

    private int calculateDuration(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return 0; // or handle this case as needed
        }
        long durationInMillis = endDate.getTime() - startDate.getTime();
        long days = TimeUnit.MILLISECONDS.toDays(durationInMillis);
        int durationInDays = Math.toIntExact(days);
        return durationInDays;
    }

    @Override
    public String toString() {
        return "DestinationEntry{" +
                "vacationId='" + vacationId + '\'' +
                ", duration='" + duration + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
