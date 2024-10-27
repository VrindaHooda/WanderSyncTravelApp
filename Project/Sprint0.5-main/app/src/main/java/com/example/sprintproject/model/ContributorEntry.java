package com.example.sprintproject.model;

public class ContributorEntry {
    private String userID;
    private String notes;


    public ContributorEntry() {
    }

    public ContributorEntry(String id, String note) {
        this.userID = id;
        this.notes = note;
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



}
