package com.example.sprintproject.model;

import java.util.List;

public class Plan {
    private int duration;
    private List<Destination> destinations;
    private String notes;
    private List<String> collaborators;
    private String owner; // Add this field

    // Constructors
    public Plan() {
        // Default constructor required for calls to DataSnapshot.getValue(Plan.class)
    }

    public Plan(int duration, List<Destination> destinations,
                String notes, List<String> collaborators, String owner) {
        this.duration = duration;
        this.destinations = destinations;
        this.notes = notes;
        this.collaborators = collaborators;
        this.owner = owner;
    }

    // Getters and Setters
    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public List<Destination> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<Destination> destinations) {
        this.destinations = destinations;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<String> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(List<String> collaborators) {
        this.collaborators = collaborators;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
