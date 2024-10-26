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
    private MutableLiveData<List<DestinationEntry>> destinationEntriesLiveData;

    public DestinationViewModel() {
        destinationDatabase = DestinationDatabase.getInstance();
        destinationEntriesLiveData = new MutableLiveData<>();
    }

    public String generateDestinationId(String location, Date startDate) {
        return location + "_" + startDate.getTime();
    }

    public void prepopulateDatabase() {
        destinationDatabase.prepopulateDatabase();
    }

    // DestinationViewModel.java

    public String getDuration() {
        long duration = destinationDatabase.getDurationInDays();
        if (duration == -1) { // Check for invalid duration from DestinationDatabase
            return "No calculated time available"; // Provide a default message
        }
        return duration + " days";
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

    // In DestinationViewModel.java

    public String calculateMissingValue(Date startDate, Date endDate, Long durationInDays) {
        if (startDate != null && endDate != null) {
            // Calculate duration if both startDate and endDate are provided
            long diffInMillis = endDate.getTime() - startDate.getTime();
            long days = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
            Log.d("Calculation", "Calculated Duration: " + days + " days");
            return days + " days";
        } else if (startDate != null && durationInDays != null) {
            // Calculate endDate if startDate and duration are provided
            long endMillis = startDate.getTime() + TimeUnit.MILLISECONDS.convert(durationInDays, TimeUnit.DAYS);
            Date calculatedEndDate = new Date(endMillis);
            Log.d("Calculation", "Calculated End Date: " + calculatedEndDate);
            return calculatedEndDate.toString();
        } else if (endDate != null && durationInDays != null) {
            // Calculate startDate if endDate and duration are provided
            long startMillis = endDate.getTime() - TimeUnit.MILLISECONDS.convert(durationInDays, TimeUnit.DAYS);
            Date calculatedStartDate = new Date(startMillis);
            Log.d("Calculation", "Calculated Start Date: " + calculatedStartDate);
            return calculatedStartDate.toString();
        } else {
            Log.d("Calculation", "Insufficient data to calculate");
            return "Insufficient data to calculate";
        }
    }





}
