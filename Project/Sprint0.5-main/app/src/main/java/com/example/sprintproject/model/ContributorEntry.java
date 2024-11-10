package com.example.sprintproject.model;

public class ContributorEntry {
    private String userID;
    private String notes;
    private String location;

    public ContributorEntry() {
    }

    public ContributorEntry(String id, String note) {
        this.userID = id;
        this.notes = note;
        this.location = location;
    }

    public String getUserId() {
        return userID;
    }

    public String getNotes() {
        return notes;
    }

    public void setUserID(String id) {
        this.userID = id;
    }

    public void setNotes(String note) {
        this.notes = note;
    }

    public String getLocation() { // Getter for location
        return location;
    }

    public void setLocation(String location) { // Setter for location
        this.location = location;
    }



}
