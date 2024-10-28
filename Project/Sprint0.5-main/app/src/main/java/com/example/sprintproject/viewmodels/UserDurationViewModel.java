package com.example.sprintproject.viewmodels;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sprintproject.model.ContributorEntry;
import com.example.sprintproject.model.DurationEntry;
import com.example.sprintproject.model.UserDurationDatabase;

import java.util.ArrayList;
import java.util.Date;

import java.util.concurrent.TimeUnit;

public class UserDurationViewModel extends ViewModel {
    private UserDurationDatabase userDurationDatabase;
    private MutableLiveData<DurationEntry> durationEntryLiveData = new MutableLiveData<>();


    public UserDurationViewModel() {
        userDurationDatabase = UserDurationDatabase.getInstance();
    }

    public void saveDurationData(String userId, String email,
                                 DurationEntry entry, ArrayList<ContributorEntry> contributors) {
        userDurationDatabase.addVacationEntry(userId, email, entry, contributors);
    }

    public String generateVacationId(int duration, Date startDate) {
        return duration + "_" + startDate.getTime();
    }

    public LiveData<DurationEntry> getDurationEntry() {
        return durationEntryLiveData;
    }

    public String calculateMissingValue(Date startDate, Date endDate, Long durationInDays) {
        if (startDate != null && endDate != null) {
            // Calculate duration if both startDate and endDate are provided
            long diffInMillis = endDate.getTime() - startDate.getTime();
            long days = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
            Log.d("Calculation", "Calculated Duration: " + days + " days");
            return Long.toString(days);
        } else if (startDate != null && durationInDays != null) {
            // Calculate endDate if startDate and duration are provided
            long endMillis = startDate.getTime()
                    + TimeUnit.MILLISECONDS.convert(durationInDays, TimeUnit.DAYS);
            Date calculatedEndDate = new Date(endMillis);
            Log.d("Calculation", "Calculated End Date: " + calculatedEndDate);
            return calculatedEndDate.toString();
        } else if (endDate != null && durationInDays != null) {
            // Calculate startDate if endDate and duration are provided
            long startMillis = endDate.getTime() - TimeUnit.MILLISECONDS
                    .convert(durationInDays, TimeUnit.DAYS);
            Date calculatedStartDate = new Date(startMillis);
            Log.d("Calculation", "Calculated Start Date: " + calculatedStartDate);
            return calculatedStartDate.toString();
        } else {
            Log.d("Calculation", "Insufficient data to calculate");
            return "Insufficient data to calculate";
        }
    }
}

