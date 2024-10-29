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

    public void prepopulateDatabase(String userId) {
        destinationDatabase.prepopulateDestinationDatabase(userId);
    }

    public LiveData<List<DestinationEntry>> getDestinationEntries() {
        return destinationEntriesLiveData;
    }

    public void readEntries(String userId) {
        destinationDatabase.getAllDestinationEntries(userId, entries -> {
            destinationEntriesLiveData.setValue(entries);
        });
    }
    public void addDestination(String userId, DestinationEntry entry) {
        destinationDatabase.addLogEntry( userId,entry.getDestinationId(), entry);

    }

}
