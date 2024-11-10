package com.example.sprintproject.viewmodels;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.sprintproject.model.DestinationDatabase;
import com.example.sprintproject.model.DestinationEntry;
import com.example.sprintproject.model.UserDurationDatabase;
import com.google.firebase.database.DatabaseError;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DestinationViewModel extends ViewModel {

    private static final String TAG = "DestinationViewModel";

    private DestinationDatabase destinationDatabase;
    private UserDurationDatabase userDurationDatabase;
    private MutableLiveData<List<DestinationEntry>> destinationEntriesLiveData = new MutableLiveData<>();
    private MutableLiveData<String> errorMessageLiveData = new MutableLiveData<>();

    public DestinationViewModel() {
        destinationDatabase = DestinationDatabase.getInstance();
        userDurationDatabase = UserDurationDatabase.getInstance();
        destinationEntriesLiveData.setValue(new ArrayList<>());  // Initialize with an empty list
    }

    // Generate a unique destination ID based on the location and start date
    public String generateDestinationId(String location, Date startDate) {
        return location + "_" + startDate.getTime();
    }

    // Prepopulate the database with default entries
    public void prepopulateDatabase(String userId) {
        destinationDatabase.prepopulateDestinationDatabase(userId);
    }

    // Return LiveData for observing destination entries
    public LiveData<List<DestinationEntry>> getDestinationEntries() {
        return destinationEntriesLiveData;
    }

    // Return LiveData for error messages
    public LiveData<String> getErrorMessage() {
        return errorMessageLiveData;
    }

    // Fetch destination entries for the given user
    public void readEntries(String userId) {
        destinationDatabase.getAllDestinationEntries(userId, new DestinationDatabase.DataStatus() {
            @Override
            public void DataIsLoaded(List<DestinationEntry> entries) {
                // Set the fetched entries to LiveData
                destinationEntriesLiveData.setValue(entries);
            }

            @Override
            public void DataLoadFailed(DatabaseError databaseError) {
                Log.w(TAG, "Failed to load entries: " + databaseError.getMessage());
                errorMessageLiveData.setValue("Failed to load entries. Please try again.");
                destinationEntriesLiveData.setValue(new ArrayList<>());
            }
        });
    }

    // Add a new destination entry to the database
    public void addDestination(String userId, DestinationEntry entry) {
        destinationDatabase.addLogEntry(userId, entry.getDestinationId(), entry);
    }

    // Handle button click to log travel to a new destination
    public void onLogTravelClicked(String userId, String location, Date startDate, Date endDate) {
        if (userId != null && location != null && startDate != null && endDate != null) {
            // Create a new DestinationEntry object, duration will be automatically calculated
            DestinationEntry newEntry = new DestinationEntry(generateDestinationId(location, startDate), location, startDate, endDate);

            // Add the new entry to the database
            addDestination(userId, newEntry);

            // Optionally re-fetch the entries to reflect the new addition
            List<DestinationEntry> currentEntries = destinationEntriesLiveData.getValue();
            if (currentEntries != null) {
                currentEntries.add(newEntry);
                destinationEntriesLiveData.setValue(currentEntries);
            }
        } else {
            Log.w(TAG, "Invalid input: location, startDate, and endDate must be provided.");
        }
    }

    // Calculate total vacation days from all destination entries
    public void onCalculateVacationClicked() {
        List<DestinationEntry> entries = destinationEntriesLiveData.getValue();
        if (entries != null && !entries.isEmpty()) {
            int totalVacationDays = 0;
            for (DestinationEntry entry : entries) {
                try {
                    // Parse the duration (which is a string) to an integer
                    totalVacationDays += Integer.parseInt(entry.getDuration());
                } catch (NumberFormatException e) {
                    Log.w(TAG, "Error parsing duration for entry: " + entry.getDestinationId());
                    // Optionally, log or show an error message to the user for this specific entry
                }
            }
            // Log or display the total vacation days
            Log.d(TAG, "Total Vacation Days: " + totalVacationDays);
        }
    }
}