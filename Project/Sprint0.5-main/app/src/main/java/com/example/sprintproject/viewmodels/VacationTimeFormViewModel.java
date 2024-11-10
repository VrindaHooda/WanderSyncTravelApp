package com.example.sprintproject.viewmodels;

import android.util.Log;

import androidx.databinding.BaseObservable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.sprintproject.model.Entry;
import com.example.sprintproject.model.UserDurationDatabase;
import com.example.sprintproject.model.UserEntry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.databinding.Bindable;
import com.example.sprintproject.model.Entry;
import com.example.sprintproject.model.UserDurationDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VacationTimeFormViewModel extends BaseObservable {

    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<Entry> vacationEntry = new MutableLiveData<>();
    private MutableLiveData<String> resultText = new MutableLiveData<>();

    @Bindable
    public String startDateStr = "";
    @Bindable
    public String endDateStr = "";
    @Bindable
    public String durationStr = "";

    public LiveData<Entry> getVacationEntry() {
        return vacationEntry;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<String> getResultText() {
        return resultText;
    }

    public void calculateVacationTime(String userId, String email) {
        try {
            String startDateInput = startDateStr.trim();
            String endDateInput = endDateStr.trim();
            Integer duration = null;

            if (!durationStr.isEmpty()) {
                duration = Integer.parseInt(durationStr);
            }

            // Logic for calculating vacation time based on inputs
            Date startDate = parseDate(startDateInput);
            Date endDate = parseDate(endDateInput);

            if (startDate == null || endDate == null) {
                errorMessage.setValue("Invalid date format. Please use yyyy-MM-dd.");
                return;
            }

            if (startDate.after(endDate)) {
                errorMessage.setValue("End date cannot be before start date.");
                return;
            }

            if (duration == null) {
                duration = calculateDuration(startDate, endDate);
            } else if (startDate != null && endDate == null) {
                endDate = calculateEndDateFromDuration(startDate, duration);
            } else if (endDate != null && startDate == null) {
                startDate = calculateStartDateFromDuration(endDate, duration);
            }

            Entry entry = new Entry(generateVacationId(duration, startDate), duration, startDate, endDate);
            vacationEntry.setValue(entry);
            saveVacationEntry(userId, email, entry);

        } catch (Exception e) {
            Log.e("VacationTimeForm", "Error calculating vacation time", e);
            errorMessage.setValue("An error occurred while calculating vacation time.");
        }
    }

    private void saveVacationEntry(String userId, String email, Entry entry) {
        // Logic to save the entry to the Firebase database
        UserDurationDatabase.getInstance().addVacationEntry(userId, email, entry);
    }

    private Date parseDate(String dateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    private int calculateDuration(Date startDate, Date endDate) {
        long durationInMillis = endDate.getTime() - startDate.getTime();
        return (int) (durationInMillis / (1000 * 60 * 60 * 24)); // Duration in days
    }

    private Date calculateEndDateFromDuration(Date startDate, int duration) {
        long durationInMillis = duration * (1000 * 60 * 60 * 24); // Convert duration to milliseconds
        long endDateMillis = startDate.getTime() + durationInMillis;
        return new Date(endDateMillis);
    }

    private Date calculateStartDateFromDuration(Date endDate, int duration) {
        long durationInMillis = duration * (1000 * 60 * 60 * 24); // Convert duration to milliseconds
        long startDateMillis = endDate.getTime() - durationInMillis;
        return new Date(startDateMillis);
    }

    private String generateVacationId(long duration, Date startDate) {
        return duration + "_" + startDate.getTime();
    }

    public void setResultText(String text) {
        resultText.setValue(text);
    }
}
