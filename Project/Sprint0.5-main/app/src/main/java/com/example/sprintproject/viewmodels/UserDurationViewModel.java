package com.example.sprintproject.viewmodels;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.sprintproject.model.DurationEntry;
import com.example.sprintproject.model.UserDurationDatabase;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class UserDurationViewModel extends ViewModel {
    private UserDurationDatabase userDurationDatabase;
    private MutableLiveData<DurationEntry> durationEntryLiveData = new MutableLiveData<>();
    private MutableLiveData<String> calculatedValue = new MutableLiveData<>();
    private MutableLiveData<String> vacationId = new MutableLiveData<>();

    public UserDurationViewModel() {
        userDurationDatabase = UserDurationDatabase.getInstance();
    }

    public void saveDurationData(String userId, String email, DurationEntry entry) {
        userDurationDatabase.addVacationEntry(userId, email, entry);
        vacationId.setValue(generateVacationId(entry.getDuration(), entry.getStartDate()));
    }

    public LiveData<DurationEntry> getDurationEntry() {
        return durationEntryLiveData;
    }

    public LiveData<String> getCalculatedValue() {
        return calculatedValue;
    }

    public LiveData<String> getVacationId() {
        return vacationId;
    }

    public void readDurationEntry(String userId) {
        userDurationDatabase.getVacationEntry(userId, (userIdCallback, email, entry) -> {
            if (entry != null) {
                durationEntryLiveData.setValue(entry);
                vacationId.setValue(generateVacationId(entry.getDuration(), entry.getStartDate()));
            } else {
                Log.d("UserDurationViewModel", "No entry found for userId: " + userIdCallback);
            }
        });
    }

    public void calculateMissingValue(Date startDate, Date endDate, Long durationInDays) {
        String result;
        if (startDate != null && endDate != null) {
            long diffInMillis = endDate.getTime() - startDate.getTime();
            long days = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
            result = Long.toString(days);
        } else if (startDate != null && durationInDays != null) {
            long endMillis = startDate.getTime() + TimeUnit.MILLISECONDS.convert(durationInDays, TimeUnit.DAYS);
            result = new Date(endMillis).toString();
        } else if (endDate != null && durationInDays != null) {
            long startMillis = endDate.getTime() - TimeUnit.MILLISECONDS.convert(durationInDays, TimeUnit.DAYS);
            result = new Date(startMillis).toString();
        } else {
            result = "Insufficient data to calculate";
        }
        calculatedValue.setValue(result);
    }

    private String generateVacationId(int duration, Date startDate) {
        return duration + "_" + startDate.getTime();
    }
}
