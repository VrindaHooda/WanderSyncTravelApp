package com.example.sprintproject.views;

public class DurationData {
    private int duration;

    /**
     * Constructs a new {@code DurationData} instance with the specified duration.
     *
     * @param duration the duration in days
     */
    public DurationData(int duration) {
        this.duration = duration;

    }

    /**
     * Returns the duration in days.
     *
     * @return the duration in days
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Sets the duration in days.
     *
     * @param totalDays duration the duration in days
     */
    public void setDuration(int totalDays) {
        this.duration = duration;
    }
}

