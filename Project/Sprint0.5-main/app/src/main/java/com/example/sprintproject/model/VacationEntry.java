package com.example.sprintproject.model;

import java.util.Date;

public class VacationEntry {
    private Date startDate;
    private Date endDate;
    private int duration;

    // Default constructor required for calls to DataSnapshot.getValue(VacationEntry.class)
    /**
     * Default constructor required for deserialization, such as
     * calls to {@code DataSnapshot.getValue(VacationEntry.class)}.
     */
    public VacationEntry() {
    }

    /**
     * Constructs a {@code VacationEntry} with the specified details.
     *
     * @param startDate the start date of the vacation
     * @param endDate   the end date of the vacation
     * @param duration  the duration of the vacation in days
     */
    public VacationEntry(Date startDate, Date endDate, int duration) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
    }

    // Getters and Setters
    /**
     * Returns the start date of the vacation.
     *
     * @return the start date
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date of the vacation.
     *
     * @param startDate the start date to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Returns the end date of the vacation.
     *
     * @return the end date
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Sets the end date of the vacation.
     *
     * @param endDate the end date to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * Returns the duration of the vacation in days.
     *
     * @return the duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Sets the duration of the vacation in days.
     *
     * @param duration the duration to set
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }
}

