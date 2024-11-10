package com.example.sprintproject.model;

public class UserEntry {

    private String userId;
    private String email;
    private Entry entry;

    public UserEntry() {
    }

    public UserEntry(String email, Entry entry) {
        this.email = email;
        this.entry = entry;
    }
    public UserEntry(String userId, String email, Entry entry) {
        this.userId = userId;
        this.email = email;
        this.entry = entry;
    }

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
