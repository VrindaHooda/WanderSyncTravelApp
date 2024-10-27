package com.example.sprintproject.viewmodels;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sprintproject.model.DurationEntry;
import com.example.sprintproject.model.UserDurationDatabase;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class UserDurationViewModel extends ViewModel {
    private UserDurationDatabase userDurationDatabase;
    private MutableLiveData<List<DurationEntry>> durationEntriesLiveData = new MutableLiveData<>();
    private MutableLiveData<String> userId = new MutableLiveData<>();

    public UserDurationViewModel() {
        userDurationDatabase = UserDurationDatabase.getInstance();
    }

    public LiveData<String> getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId.setValue(userId);
    }

    public void saveDurationData(String vacationId, int duration, String startDate, String endDate) {
        String currentUserId = userId.getValue();
        if (currentUserId != null) {
            userDurationDatabase.addVacationDurationEntry(vacationId, new DurationEntry(vacationId, duration, new Date(startDate), new Date(endDate)));
        } else {
            Log.d("UserDurationDatabase", "User ID is null. Cannot save duration data.");
        }
    }

    public String generateVacationId(int duration, Date startDate) {
        return duration + "_" + startDate.getTime();
    }


    public LiveData<List<DurationEntry>> getDurationEntries() {
        return durationEntriesLiveData;
    }

    public void readDurationEntries() {
        userDurationDatabase.getAllUserDurationEntries(entries -> {
            durationEntriesLiveData.setValue(entries);
        });
    }

    public void addDuration(DurationEntry entry) {
        userDurationDatabase.addVacationDurationEntry(entry.getVacationId(), entry);
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
