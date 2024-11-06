package com.example.sprintproject.model;

public class DiningReservation {
    private String location;
    private String time;
    private String website;
    private int rating;
    private int numPeople;
    private String date;

    public DiningReservation() { }

    public DiningReservation(String location, String date, String time, int numPeople, String website, int rating) {
        this.location = location;
        this.date = date;
        this.time = time;
        this.numPeople = numPeople;
        this.website = website;
        this.rating = rating;
    }

    public DiningReservation(String location, String date, String time, int numPeople, String website) {
        this.location = location;
        this.date = date;
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


    // Getters and setters
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public int getNumPeople() { return numPeople; }
    public void setNumPeople(int numPeople) { this.numPeople = numPeople; }


}
