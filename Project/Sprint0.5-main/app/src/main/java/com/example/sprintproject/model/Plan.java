package com.example.sprintproject.model;

import java.util.List;

public class Plan {
    private int duration;
    private List<Destination> destinations;
    private String notes;
    private List<String> collaborators;

    // Default constructor required for Firestore
    /**
     * Default constructor required for Firestore.
     */
    public Plan() {
    }

    /**
     * Constructs a {@code Plan} with the specified details.
     *
     * @param duration      the duration of the plan in days
     * @param destinations  a list of destinations included in the plan
     * @param notes         notes or additional details about the plan
     * @param collaborators a list of collaborator IDs for the plan
     */
    public Plan(int duration, List<Destination> destinations, String notes, List<String> collaborators) {
        this.duration = duration;
        this.destinations = destinations;
        this.notes = notes;
        this.collaborators = collaborators;
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

    /**
     * Returns the list of destinations included in the plan.
     *
     * @return the list of destinations
     */
    public List<Destination> getDestinations() {
        return destinations;
    }

    /**
     * Sets the list of destinations included in the plan.
     *
     * @param destinations the destinations to set
     */
    public void setDestinations(List<Destination> destinations) {
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

    /**
     * Returns the list of collaborator IDs for the plan.
     *
     * @return the list of collaborator IDs
     */
    public List<String> getCollaborators() {
        return collaborators;
    }

    /**
     * Sets the list of collaborator IDs for the plan.
     *
     * @param collaborators the collaborators to set
     */
    public void setCollaborators(List<String> collaborators) {
        this.collaborators = collaborators;
    }
}

