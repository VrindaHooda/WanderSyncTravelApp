package com.example.sprintproject.model;



import java.util.ArrayList;


public class UserEntry {

    private String userId;
    private String email;
    private DurationEntry entry;
    private ArrayList<ContributorEntry> contributors;

    public UserEntry() {
    }

    public UserEntry(String email, DurationEntry entry, ArrayList<ContributorEntry> contributors) {
        this.email = email;
        this.entry = entry;
        this.contributors = contributors;
    }

    public UserEntry(String userId, String email, DurationEntry entry,
                     ArrayList<ContributorEntry> contributors) {
        this.userId = userId;
        this.email = email;
        this.entry = entry;
        this.contributors = contributors;
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

    public ArrayList<ContributorEntry> getContributors() {
        return contributors;
    }

    public void setContributors(ArrayList<ContributorEntry> contributors) {
        this.contributors = contributors;
    }
}