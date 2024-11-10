package com.example.sprintproject.model;

public class ContributorEntry {
    private String contributorEmail;
    private String notes;

    public ContributorEntry() {
    }

    public ContributorEntry(String contributorEmail, String note) {
        this.contributorEmail = contributorEmail;
        this.notes = note;
    }

    public String getContributorEmail() {
        return contributorEmail;
    }

    public String getNotes() {
        return notes;
    }

    public void setContributorEmail(String contributorEmail) {
        this.contributorEmail = contributorEmail;
    }

    public void setNotes(String note) {
        this.notes = note;
    }




}
