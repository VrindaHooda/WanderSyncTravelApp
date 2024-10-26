package com.example.sprintproject.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sprintproject.model.DestinationDatabase;
import com.example.sprintproject.model.DestinationEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.List;

public class DestinationViewModel extends ViewModel {

    private DestinationDatabase destinationDatabase;
    private MutableLiveData<List<DestinationEntry>> destinationEntriesLiveData;
    private MutableLiveData<DestinationEntry> destinationEntryLiveData;

    public DestinationViewModel() {
        destinationDatabase = DestinationDatabase.getInstance();
        destinationEntriesLiveData = new MutableLiveData<>();
        destinationEntryLiveData = new MutableLiveData<>();
    }

    public String generateDestinationId(String location, Date startDate) {
        return location + "_" + startDate.getTime();
    }

    // Write destination entry data to the database
    public void writeDestinationEntryData(String userId, String destinationEntryId, DestinationEntry destinationEntry) {
        destinationDatabase.writeData("users/" + userId + "/destinations/" + destinationEntryId, destinationEntry);
    }

    // Read a specific destination entry
    public void readDestinationEntryData(String destinationEntryId) {
        destinationDatabase.readData("destinationEntries/" + destinationEntryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DestinationEntry destinationEntry = dataSnapshot.getValue(DestinationEntry.class);
                destinationEntryLiveData.postValue(destinationEntry);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error if needed
            }
        });
    }

    // Calculate total vacation days for a specific user
    public void calculateTotalVacationDays(String userId, OnVacationDaysCalculatedListener listener) {
        destinationDatabase.readData("users/" + userId + "/destinations").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int totalDays = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DestinationEntry entry = snapshot.getValue(DestinationEntry.class);
                    if (entry != null && entry.getDuration() != null) {
                        totalDays += Integer.parseInt(entry.getDuration());
                    }
                }
                listener.onTotalVacationDaysCalculated(totalDays);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError(databaseError.getMessage());
            }
        });
    }

    // Interface for handling total vacation days calculation
    public interface OnVacationDaysCalculatedListener {
        void onTotalVacationDaysCalculated(int totalDays);
        void onError(String error);
    }

    // Prepopulate the database if needed
    public void prepopulateDatabase() {
        destinationDatabase.prepopulateDatabase();
    }

    // Add a new destination entry
    public void addDestination(DestinationEntry entry) {
        destinationDatabase.addEntry(entry.getDestinationId(), entry);
    }

    // Read all destination entries and post the result to live data
    public void readEntries() {
        destinationDatabase.getAllEntries(new DestinationDatabase.DataStatus() {
            @Override
            public void DataIsLoaded(List<DestinationEntry> entries) {
                destinationEntriesLiveData.setValue(entries);
            }
        });
    }

    // Get live data for all destination entries
    public LiveData<List<DestinationEntry>> getDestinationEntries() {
        return destinationEntriesLiveData;
    }

    // Get live data for a specific destination entry
    public LiveData<DestinationEntry> getDestinationEntryLiveData() {
        return destinationEntryLiveData;
    }
}
