package com.example.sprintproject.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Entry {

    private String vacationId;
    private int duration;
    private Date startDate;
    private Date endDate;


    public Entry() {
    }

    public Entry(String vacationId, int duration, Date startDate, Date endDate) {
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

    public Entry(String vacationId, String startDateStr, String endDateStr) {
        this.vacationId = vacationId;
        this.startDate = parseDate(startDateStr);
        this.endDate = parseDate(endDateStr);
        this.duration = calculateDuration(this.startDate, this.endDate);
    }

    private Date parseDate(String dateStr) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
