package com.example.sprintproject.viewmodels;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.sprintproject.model.Entry;
import com.example.sprintproject.model.UserDurationDatabase;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogTravelViewModel extends ViewModel {

    // LiveData fields for UI binding
    private MutableLiveData<String> startDate = new MutableLiveData<>();
    private MutableLiveData<String> endDate = new MutableLiveData<>();
    private MutableLiveData<String> location = new MutableLiveData<>();
    private MutableLiveData<String> calculatedValue = new MutableLiveData<>();
    private MutableLiveData<String> vacationId = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    // LiveData to signal the UI to open the DatePickerDialog
    private MutableLiveData<Boolean> openStartDatePicker = new MutableLiveData<>();
    private MutableLiveData<Boolean> openEndDatePicker = new MutableLiveData<>();

    // Instance of the UserDurationDatabase (singleton pattern)
    private UserDurationDatabase userDurationDatabase;

    // Constructor initializes the database instance
    public LogTravelViewModel() {
        userDurationDatabase = UserDurationDatabase.getInstance();
    }

    // Getter methods to observe LiveData from the UI
    public LiveData<String> getStartDate() {
        return startDate;
    }

    public LiveData<String> getEndDate() {
        return endDate;
    }

    public LiveData<String> getLocation() {
        return location;
    }

    public LiveData<String> getCalculatedValue() {
        return calculatedValue;
    }

    public LiveData<String> getVacationId() {
        return vacationId;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> getOpenStartDatePicker() {
        return openStartDatePicker;
    }

    public LiveData<Boolean> getOpenEndDatePicker() {
        return openEndDatePicker;
    }

    // Method for handling the start date picker (to be implemented in the UI)
    public void onStartDateClick() {
        // Signal the UI to open the Start Date Picker
        openStartDatePicker.setValue(true);
    }

    // Method for handling the end date picker trigger
    public void onEndDateClick() {
        // Signal the UI to open the End Date Picker
        openEndDatePicker.setValue(true);
    }

    // Method to save the travel log data to the database
    public void saveTravelLog(String userId, String email) {
        String start = startDate.getValue();
        String end = endDate.getValue();
        String loc = location.getValue();

        // Ensure all fields are filled out
        if (start == null || end == null || loc == null) {
            errorMessage.setValue("Incomplete travel log data. Cannot save.");
            return;
        }

        // Parse the start and end dates
        Date parsedStartDate = parseDate(start);
        Date parsedEndDate = parseDate(end);

        if (parsedStartDate == null || parsedEndDate == null) {
            return; // Error already set in the errorMessage LiveData
        }

        // Ensure start date is before the end date
        if (parsedStartDate.after(parsedEndDate)) {
            errorMessage.setValue("Start date cannot be after the end date.");
            return;
        }

        // Calculate the duration between the start and end dates
        long durationInMillis = parsedEndDate.getTime() - parsedStartDate.getTime();
        long durationInDays = durationInMillis / (1000 * 60 * 60 * 24);
        String duration = String.valueOf(durationInDays);

        // Create an Entry object with the data
        Entry entry = new Entry(duration, start, end);

        // Save the entry in the database
        userDurationDatabase.addVacationEntry(userId, email, entry);

        // Generate a unique vacation ID based on the duration and start date
        vacationId.setValue(generateVacationId(durationInDays, parsedStartDate));

        Log.d("LogTravelViewModel", "Travel log saved: " + loc + ", Duration: " + durationInDays + " days");
    }

    // Setters for updating the LiveData values
    public void setStartDate(String startDate) {
        this.startDate.setValue(startDate);
    }

    public void setEndDate(String endDate) {
        this.endDate.setValue(endDate);
    }

    public void setLocation(String location) {
        this.location.setValue(location);
    }

    // Helper method to parse a date string into a Date object
    private Date parseDate(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(dateStr);
        } catch (Exception e) {
            Log.e("LogTravelViewModel", "Error parsing date: " + e.getMessage());
            errorMessage.setValue("Invalid date format.");
            return null;
        }
    }

    // Helper method to generate a unique vacation ID based on the duration and start date
    private String generateVacationId(long duration, Date startDate) {
        return duration + "_" + startDate.getTime();
    }

    // Method to calculate the vacation duration and update the calculated value in the UI
    public void calculateVacationDuration() {
        String start = startDate.getValue();
        String end = endDate.getValue();

        if (start == null || end == null) {
            errorMessage.setValue("Please select both start and end dates.");
            return;
        }

        // Parse the dates
        Date parsedStartDate = parseDate(start);
        Date parsedEndDate = parseDate(end);

        if (parsedStartDate != null && parsedEndDate != null) {
            // Calculate the duration
            long durationInMillis = parsedEndDate.getTime() - parsedStartDate.getTime();
            long durationInDays = durationInMillis / (1000 * 60 * 60 * 24);
            calculatedValue.setValue("Vacation duration: " + durationInDays + " days");
        }
    }
}
