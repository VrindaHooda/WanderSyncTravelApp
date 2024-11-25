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
    /**
     * Default constructor required for Firestore.
     */
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


    public Plan(String planName, String location, int duration, List<Destination> destinations, String notes, List<String> collaborators) {
        this.planName = planName;
        this.location = location;
        this.duration = duration;
        this.destinations = destinations;
        this.notes = notes;
        this.collaborators = collaborators;
    }

    public String getPlanName() {
        return planName;
    }

    public String getLocation() {
        return location;
    }

    /**
     * Returns the duration of the plan in days.
     *
     * @return the duration of the plan
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Sets the duration of the plan in days.
     *
     * @param duration the duration to set
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    public ArrayList<Destination> getDestinations() {
        return destinations;
    }

    public void setDestinations(ArrayList<Destination> destinations) {
        this.destinations = destinations;
    }

    /**
     * Returns the notes or additional details about the plan.
     *
     * @return the notes of the plan
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets the notes or additional details about the plan.
     *
     * @param notes the notes to set
     */
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

