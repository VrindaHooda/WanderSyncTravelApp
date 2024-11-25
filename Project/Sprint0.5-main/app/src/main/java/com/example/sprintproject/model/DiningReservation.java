package com.example.sprintproject.model;

public class DiningReservation {
    private String location;
    private String time;
    private String website;
    private int rating;
    private int numPeople;
    private int month;
    private int day;
    private int year;

    /**
     * Default constructor for {@code DiningReservation}.
     */
    public DiningReservation() { }

    /**
     * Constructs a {@code DiningReservation} with the specified details.
     *
     * @param location  the location of the dining reservation
     * @param month     the month of the reservation date
     * @param day       the day of the reservation date
     * @param year      the year of the reservation date
     * @param time      the time of the dining reservation
     * @param numPeople the number of people for the reservation
     * @param website   the website associated with the reservation
     * @param rating    the rating of the dining reservation
     */
    public DiningReservation(String location, int month, int day, int year, String time,
                             int numPeople, String website, int rating) {
        this.location = location;
        this.month = month;
        this.day = day;
        this.year = year;
        this.time = time;
        this.numPeople = numPeople;
        this.website = website;
        this.rating = rating;
    }

    /**
     * Constructs a {@code DiningReservation} without a rating.
     *
     * @param location  the location of the dining reservation
     * @param month     the month of the reservation date
     * @param day       the day of the reservation date
     * @param year      the year of the reservation date
     * @param time      the time of the dining reservation
     * @param numPeople the number of people for the reservation
     * @param website   the website associated with the reservation
     */
    public DiningReservation(String location, int month, int day, int year,
                             String time, int numPeople, String website) {
        this.location = location;
        this.month = month;
        this.day = day;
        this.year = year;
        this.time = time;
        this.numPeople = numPeople;
        this.website = website;
    }


    /**
     * Constructs a {@code DiningReservation} with basic details.
     *
     * @param location the location of the dining reservation
     * @param time     the time of the dining reservation
     * @param website  the website associated with the reservation
     * @param rating   the rating of the dining reservation
     */
    public DiningReservation(String location, String time, String website, int rating) {
        this.location = location;
        this.time = time;
        this.website = website;
        this.rating = rating;
    }

    // Getters and Setters
    /**
     * Returns the location of the dining reservation.
     *
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location of the dining reservation.
     *
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Returns the time of the dining reservation.
     *
     * @return the time
     */
    public String getTime() {
        return time;
    }

    /**
     * Sets the time of the dining reservation.
     *
     * @param time the time to set
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * Returns the website associated with the dining reservation.
     *
     * @return the website
     */
    public String getWebsite() {
        return website;
    }

    /**
     * Sets the website associated with the dining reservation.
     *
     * @param website the website to set
     */
    public void setWebsite(String website) {
        this.website = website;
    }

    /**
     * Returns the rating of the dining reservation.
     *
     * @return the rating
     */
    public int getRating() {
        return rating;
    }

    /**
     * Sets the rating of the dining reservation.
     *
     * @param rating the rating to set
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * Returns the number of people for the dining reservation.
     *
     * @return the number of people
     */
    public int getNumPeople() {
        return numPeople;
    }

    /**
     * Sets the number of people for the dining reservation.
     *
     * @param numPeople the number of people to set
     */
    public void setNumPeople(int numPeople) {
        this.numPeople = numPeople;
    }

    /**
     * Returns the month of the reservation date.
     *
     * @return the month
     */
    public int getMonth() {
        return month;
    }

    /**
     * Sets the month of the reservation date.
     *
     * @param month the month to set
     */
    public void setMonth(int month) {
        this.month = month;
    }

    /**
     * Returns the day of the reservation date.
     *
     * @return the day
     */
    public int getDay() {
        return day;
    }

    /**
     * Sets the day of the reservation date.
     *
     * @param day the day to set
     */
    public void setDay(int day) {
        this.day = day;
    }

    /**
     * Returns the year of the reservation date.
     *
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * Sets the year of the reservation date.
     *
     * @param year the year to set
     */
    public void setYear(int year) {
        this.year = year;
    }

    // Method to get formatted date as a string (optional)
    /**
     * Returns the formatted date as a string in the format MM/DD/YYYY.
     *
     * @return the formatted date
     */
    public String getFormattedDate() {
        return String.format("%02d/%02d/%04d", month, day, year);
    }
}
