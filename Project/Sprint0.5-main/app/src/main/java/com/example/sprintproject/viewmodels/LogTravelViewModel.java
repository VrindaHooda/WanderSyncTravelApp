package com.example.sprintproject.viewmodels;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LogTravelViewModel extends ViewModel {

    private MutableLiveData<String> startDate = new MutableLiveData<>();
    private MutableLiveData<String> endDate = new MutableLiveData<>();
    private MutableLiveData<String> location = new MutableLiveData<>();

    public LiveData<String> getStartDate() {
        return startDate;
    }

    public LiveData<String> getEndDate() {
        return endDate;
    }

    public LiveData<String> getLocation() {
        return location;
    }

    public void onStartDateClick() {
        // Logic for opening the date picker
    }

    public void onEndDateClick() {
        // Logic for opening the date picker
    }

    public void saveTravelLog() {
        // Save travel log data logic
        Log.d("LogTravelViewModel", "Travel log saved: " + location.getValue());
    }

    public void setStartDate(String startDate) {
        this.startDate.setValue(startDate);
    }

    public void setEndDate(String endDate) {
        this.endDate.setValue(endDate);
    }

    public void setLocation(String location) {
        this.location.setValue(location);
    }
}
