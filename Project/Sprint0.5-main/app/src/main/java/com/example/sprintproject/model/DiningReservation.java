package com.example.sprintproject.model;

import java.util.Date;

public class DiningReservation {
    private String id;
    private String userId;
    private String restaurantName;
    private Date reservationDate;
    private int numberOfGuests;
    private String notes;
    private String website;
    private float rating;

    // Required no-argument constructor for Firebase serialization
    public DiningReservation() {
    }

    //More than 7 parameters, not sure how this could be fixed
    public DiningReservation(String id, String userId, String restaurantName, Date reservationDate,
                             int numberOfGuests, String notes, String website, float rating) {
        this.id = id;
        this.userId = userId;
        this.restaurantName = restaurantName;
        this.reservationDate = reservationDate;
        this.numberOfGuests = numberOfGuests;
        this.notes = notes;
        this.website = website;
        this.rating = rating;
    }

    // Getters and setters for all fields

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public Date getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
