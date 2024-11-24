package com.example.sprintproject.model;

import java.util.List;

public class Plan {
    private String id;
    private int duration;
    private List<Destination> destinations;
    private String notes;
    private List<String> collaborators;

    // Default constructor required for Firestore
    public Plan() {
    }

    public Plan(int duration, List<Destination> destinations, String notes, List<String> collaborators) {
        this.duration = duration;
        this.destinations = destinations;
        this.notes = notes;
        this.collaborators = collaborators;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
}

