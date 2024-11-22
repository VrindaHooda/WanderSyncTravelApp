package com.example.sprintproject.views;

public class TripData {

    private int totalDays;

    /**
     * Constructs a new {@code TripData} instance with the specified total days.
     *
     * @param totalDays the total number of days for the trip
     */
    public TripData(int totalDays) {
        this.totalDays = totalDays;

    }

    /**
     * Retrieves the total number of days for the trip.
     *
     * @return the total number of days
     */
    public int getTotalDays() {
        return totalDays;
    }

    /**
     * Updates the total number of days for the trip.
     *
     * @param totalDays the new total number of days
     */
    public void setTotalDays(int totalDays) {
        this.totalDays = totalDays;
    }
}