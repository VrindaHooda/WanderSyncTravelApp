package com.example.sprintproject.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.sprintproject.model.DestinationDatabase;
import com.example.sprintproject.model.DestinationEntry;

import java.util.Date;
import java.util.List;

public class DestinationViewModel extends ViewModel {

    private DestinationDatabase destinationDatabase;
    private MutableLiveData<List<DestinationEntry>> destinationEntriesLiveData;
    private MutableLiveData<Long> totalPlannedDays = new MutableLiveData<>(); // Holds sum of planned days for trips
    private static final long TOTAL_ALLOTTED_DAYS = 30; // Example value for allotted days, can be adjusted or fetched from Firebase



    public DestinationViewModel() {
        destinationDatabase = DestinationDatabase.getInstance();
        destinationEntriesLiveData = new MutableLiveData<>();
        loadTotalPlannedDays(); // Load and calculate planned days
    }

    public String generateDestinationId(String location, Date startDate) {
        return location + "_" + startDate.getTime();
    }

    public void prepopulateDatabase() {
        destinationDatabase.prepopulateDatabase();
    }

    public void addDestination(DestinationEntry entry) {
        destinationDatabase.addEntry(entry.getDestinationId(), entry);
    }

    public void readEntries() {
        destinationDatabase.getAllEntries(new DestinationDatabase.DataStatus() {
            @Override
            public void DataIsLoaded(List<DestinationEntry> entries) {
                destinationEntriesLiveData.setValue(entries);
            }
        });
    }

    public LiveData<List<DestinationEntry>> getDestinationEntries() {
        return destinationEntriesLiveData;
    }

    // Provides LiveData for total planned days, observed by LogisticsActivity
    public LiveData<Long> getTotalPlannedDays() {
        return totalPlannedDays;
    }

    // Returns the total allotted days; can be adjusted to fetch from Firebase if configurable
    public long getTotalAllottedDays() {
        return TOTAL_ALLOTTED_DAYS;
    }

    // Expose contributors for activity observation
    public LiveData<List<String>> getContributors() {
        return contributorsLiveData; // doesnt exist yet, needs to be created
    }

    // Expose notes for activity observation
    public LiveData<List<String>> getNotes() {
        return notesLiveData; // doesnt exist yet, needs to be created
    }

    // Ensures entries are loaded and planned days calculated when ViewModel is initialized
    private void loadTotalPlannedDays() {
        readEntries(); // Load data from Firebase
    }

    // Calculates the total planned days by summing the duration of each destination entry
    private void calculateTotalPlannedDays(List<DestinationEntry> entries) {
        long totalDays = 0;
        for (DestinationEntry entry : entries) {
            totalDays += entry.getDurationInDays(); // Sum duration of each entry
        }
        totalPlannedDays.setValue(totalDays); // Update LiveData with total planned days
    }

    /**
     * Loads all contributors for the trip from Firebase and updates the LiveData.
     */
    private void loadContributors() {
        // Firebase logic to load contributors and set LiveData
    }

    /**
     * Loads all notes for the trip from Firebase and updates the LiveData.
     */
    private void loadNotes() {
        // Firebase logic to load notes and set LiveData
    }

    /**
     * Adds a contributor to the trip in Firebase.
     * Updates the contributors list after successfully adding the new user.
     */
    public void addContributor(String email) {
        // Firebase logic to add a new contributor
    }

    /**
     * Adds a new note for the trip in Firebase.
     * Updates the notes list after successfully adding the note.
     */
    public void addNoteToTrip(String noteText) {
        // Firebase logic to add a new note
    }
}
