package com.example.sprintproject.model;

import java.util.List;

public class UserEntry {

    private String userId;
    private String email;
    private Entry entry;
    private List<ContributorEntry> contributorEntryList;

    public UserEntry() {
    }

    public UserEntry(String email, Entry entry) {
        this.email = email;
        this.entry = entry;

    }
    public UserEntry(String userId, String email, Entry entry, List<ContributorEntry> contributorEntryList) {
        this.userId = userId;
        this.email = email;
        this.entry = entry;
        this.contributorEntryList = contributorEntryList;
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

    public List<ContributorEntry> getContributorEntryList() {
        return contributorEntryList;
    }

    public void setContributorEntryList(List<ContributorEntry> contributorEntryList) {
        this.contributorEntryList = contributorEntryList;
    }
}
