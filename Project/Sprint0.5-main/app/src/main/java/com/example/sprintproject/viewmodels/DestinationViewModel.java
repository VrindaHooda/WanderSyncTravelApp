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

    public DestinationViewModel() {
        // Get singleton instance of DestinationDatabase
        destinationDatabase = DestinationDatabase.getInstance();
        destinationEntriesLiveData = new MutableLiveData<>();
    }

    // Method to generate a unique destination ID
    public String generateDestinationId(String location, Date startDate) {
        return location + "_" + startDate.getTime();
    }

    // Add destination entry to Firebase using destinationId
    public void addDestination(DestinationEntry entry) {
        destinationDatabase.addEntry(entry.getDestinationId(), entry);
    }

    // Read all destination entries from Firebase
    public void readEntries() {
        destinationDatabase.getAllEntries(new DestinationDatabase.DataStatus() {
            @Override
            public void DataIsLoaded(List<DestinationEntry> entries) {
                // Update LiveData when data is loaded from Firebase
                destinationEntriesLiveData.setValue(entries);
            }
        });
    }

    // Return the LiveData to observe in the UI
    public LiveData<List<DestinationEntry>> getDestinationEntries() {
        return destinationEntriesLiveData;
    }
}
