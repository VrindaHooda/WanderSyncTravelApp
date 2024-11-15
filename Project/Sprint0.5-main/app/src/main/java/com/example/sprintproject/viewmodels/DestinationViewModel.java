package com.example.sprintproject.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sprintproject.model.FirebaseRepository;
import com.example.sprintproject.model.TravelLog;
import com.example.sprintproject.model.VacationEntry;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DestinationViewModel extends ViewModel {
    private FirebaseRepository firebaseRepository;
    private MutableLiveData<List<TravelLog>> travelLogs;

    public DestinationViewModel() {
        firebaseRepository = new FirebaseRepository();
        travelLogs = new MutableLiveData<>();
    }

    public LiveData<List<TravelLog>> getLastFiveTravelLogs(String userId) {
        firebaseRepository.getLastFiveTravelLogs(userId, task -> {
            if (task.isSuccessful()) {
                List<TravelLog> logs = new ArrayList<>();
                for (DocumentSnapshot document : task.getResult()) {
                    logs.add(document.toObject(TravelLog.class));
                }
                travelLogs.setValue(logs);
            }
        });
        return travelLogs;
    }

    public void logPastTravel(String userId, TravelLog travelLog) {
        firebaseRepository.saveTravelLog(userId, travelLog);
    }

    public void saveVacationEntry(String userId, VacationEntry vacationEntry) {
        firebaseRepository.saveVacationEntry(userId, vacationEntry);
    }

    public void calculateVacationTime(Date startDate, Date endDate, Integer duration) {
        if (startDate != null && endDate != null) {
            long diff = endDate.getTime() - startDate.getTime();
            duration = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        } else if (startDate != null && duration != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            calendar.add(Calendar.DAY_OF_YEAR, duration);
            endDate = calendar.getTime();
        } else if (endDate != null && duration != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(endDate);
            calendar.add(Calendar.DAY_OF_YEAR, -duration);
            startDate = calendar.getTime();
        }

        // Update LiveData to notify the View
        // You can use another LiveData to update the calculated values in the form if needed
    }
}
