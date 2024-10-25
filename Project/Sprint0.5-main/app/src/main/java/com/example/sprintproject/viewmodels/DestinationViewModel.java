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

    public DestinationViewModel() {
        destinationDatabase = DestinationDatabase.getInstance();
        destinationEntriesLiveData = new MutableLiveData<>();
    }

    public String generateDestinationId(String location, Date startDate) {
        return location + "_" + startDate.getTime();
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
}
