package com.example.sprintproject.viewmodels;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.sprintproject.model.DestinationDatabase;
import com.example.sprintproject.model.DestinationEntry;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DestinationViewModel extends ViewModel {

    private DestinationDatabase destinationDatabase;
    private MutableLiveData<List<DestinationEntry>> destinationEntriesLiveData = new MutableLiveData<>();

    public DestinationViewModel() {
        destinationDatabase = DestinationDatabase.getInstance();
    }

    public String generateDestinationId(String location, Date startDate) {
        return location + "_" + startDate.getTime();
    }

    public void prepopulateDatabase() {
        destinationDatabase.prepopulateDestinationDatabase();
    }

    public LiveData<List<DestinationEntry>> getDestinationEntries() {
        return destinationEntriesLiveData;
    }

    public void readEntries() {
        destinationDatabase.getAllDestinationEntries(entries -> {
            destinationEntriesLiveData.setValue(entries);
        });
    }
    public void addDestination(DestinationEntry entry) {
        destinationDatabase.addLogEntry(entry.getDestinationId(), entry);
    }

    public boolean isDestinationAdded(String location) {
        List<DestinationEntry> entries = destinationEntriesLiveData.getValue();
        if (entries != null) {
            for (DestinationEntry entry : entries) {
                if (entry.getLocation().equals(location)) {
                    return true; // Destination found
                }
            }
        }
        return false; // Destination not found
    }

    public boolean isTravelLogSaved(String location, Date startDate, Date endDate) {
        List<DestinationEntry> entries = destinationEntriesLiveData.getValue();
        if (entries != null) {
            for (DestinationEntry entry : entries) {
                if (entry.getLocation().equals(location) &&
                        entry.getStartDate().equals(startDate) &&
                        entry.getEndDate().equals(endDate)) {
                    return true;
                }
            }
        }
        return false;
    }


}
