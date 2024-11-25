package com.example.sprintproject.model;

import android.util.Log;

import java.util.ArrayList;

public class Plan {
    private String id;
    private int duration;
    private ArrayList<Destination> destinations;
    private String notes;
    private ArrayList<String> collaborators;

    // Default constructor required for Firestore
    public Plan() {
    }

    public Plan(int duration, ArrayList<Destination> destinations, String notes, ArrayList<String> collaborators, String id) {
        this.duration = duration;
        this.destinations = destinations;
        Log.d("Hello", this.destinations.toString());
        this.notes = notes;
        this.collaborators = collaborators;
        this.id = id;
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

    public ArrayList<Destination> getDestinations() {
        return destinations;
    }

    public void setDestinations(ArrayList<Destination> destinations) {
        this.destinations = destinations;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ArrayList<String> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(ArrayList<String> collaborators) {
        this.collaborators = collaborators;
    }
}

