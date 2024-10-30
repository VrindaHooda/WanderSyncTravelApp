package com.example.sprintproject.viewmodels;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.sprintproject.model.DestinationDatabase;
import com.example.sprintproject.model.DestinationEntry;
import com.google.firebase.database.DatabaseError;  // Make sure to import DatabaseError

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DestinationViewModel extends ViewModel {

    private DestinationDatabase destinationDatabase;
    private MutableLiveData<List<DestinationEntry>> destinationEntriesLiveData =
            new MutableLiveData<>();

    public DestinationViewModel() {
        destinationDatabase = DestinationDatabase.getInstance();
    }

    public String generateDestinationId(String location, Date startDate) {
        return location + "_" + startDate.getTime();
    }

    public void prepopulateDatabase(String userId) {
        destinationDatabase.prepopulateDestinationDatabase(userId);
    }

    public LiveData<List<DestinationEntry>> getDestinationEntries() {
        return destinationEntriesLiveData;
    }

    public void readEntries(String userId) {
        destinationDatabase.getAllDestinationEntries(userId, new DestinationDatabase.DataStatus() {
            @Override
            public void DataIsLoaded(List<DestinationEntry> entries) {
                destinationEntriesLiveData.setValue(entries);
            }

            @Override
            public void DataLoadFailed(DatabaseError databaseError) {
                Log.w("DestinationViewModel", "Failed to load entries: "
                        + databaseError.getMessage());
                // Optionally, set an empty list or handle the error state
                destinationEntriesLiveData.setValue(new ArrayList<>()); // Handle as needed
            }
        });
    }

    public void addDestination(String userId, DestinationEntry entry) {
        destinationDatabase.addLogEntry(userId, entry.getDestinationId(), entry);
    }
}
