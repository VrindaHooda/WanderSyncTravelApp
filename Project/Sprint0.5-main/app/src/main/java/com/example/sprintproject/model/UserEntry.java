package com.example.sprintproject.model;

import java.util.Date;

public class UserEntry {

    private String userId;
    private String email;
    private DurationEntry entry;

    public UserEntry() {
    }

    public UserEntry(String email, DurationEntry entry) {
        this.email = email;
        this.entry = entry;
    }

    public UserEntry(String userId, String email, DurationEntry entry) {
        this.userId = userId;
        this.email = email;
        this.entry = entry;
    }

    public DurationEntry getEntry() {
        return entry;
    }

    public void setEntry(DurationEntry entry) {
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
