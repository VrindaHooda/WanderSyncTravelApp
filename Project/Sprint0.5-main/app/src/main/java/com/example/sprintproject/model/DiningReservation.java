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

    public DiningReservation() { }

    public DiningReservation(String location, int month, int day, int year, String time, int numPeople, String website, int rating) {
        this.location = location;
        this.month = month;
        this.day = day;
        this.year = year;
        this.time = time;
        this.numPeople = numPeople;
        this.website = website;
        this.rating = rating;
    }

    public DiningReservation(String location, int month, int day, int year, String time, int numPeople, String website) {
        this.location = location;
        this.month = month;
        this.day = day;
        this.year = year;
        this.time = time;
        this.numPeople = numPeople;
        this.website = website;
    }

    public DiningReservation(String location, String time, String website, int rating) {
        this.location = location;
        this.time = time;
        this.website = website;
        this.rating = rating;
    }

    // Getters and Setters
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getNumPeople() {
        return numPeople;
    }

    public void setNumPeople(int numPeople) {
        this.numPeople = numPeople;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    // Method to get formatted date as a string (optional)
    public String getFormattedDate() {
        return String.format("%02d/%02d/%04d", month, day, year);
    }
}
