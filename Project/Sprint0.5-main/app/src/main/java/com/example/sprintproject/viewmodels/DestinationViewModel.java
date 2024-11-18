package com.example.sprintproject.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sprintproject.model.FirebaseRepository;
import com.example.sprintproject.model.TravelLog;
import com.example.sprintproject.model.VacationEntry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DestinationViewModel extends ViewModel {
    private FirebaseRepository firebaseRepository;
    private MutableLiveData<List<TravelLog>> travelLogs;
    private MutableLiveData<Integer> totalTravelDays;
    private MutableLiveData<Integer> calculatedDuration;

    public DestinationViewModel() {
        firebaseRepository = new FirebaseRepository();
        travelLogs = new MutableLiveData<>();
        totalTravelDays = new MutableLiveData<>();
        calculatedDuration = new MutableLiveData<>();
    }

    public LiveData<List<TravelLog>> getLastFiveTravelLogs(String userId) {
        // Prepopulate travel logs if none exist
        firebaseRepository.prepopulateTravelLogsIfNoneExist(userId);

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
        refreshTotalTravelDays(userId);
    }

    public void saveVacationEntry(String userId, VacationEntry vacationEntry) {
        firebaseRepository.saveVacationEntry(userId, vacationEntry);
    }

    public void calculateVacationTime(Date startDate, Date endDate, Integer duration) {
        if (startDate != null && endDate != null) {
            firebaseRepository.calculateDuration(startDate, endDate, new OnCompleteListener<Integer>() {
                @Override
                public void onComplete(@NonNull Task<Integer> task) {
                    if (task.isSuccessful()) {
                        calculatedDuration.setValue(task.getResult());
                    }
                }
            });
        } else if (startDate != null && duration != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            calendar.add(Calendar.DAY_OF_YEAR, duration);
            endDate = calendar.getTime();
            calculateVacationTime(startDate, endDate, null);
        } else if (endDate != null && duration != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(endDate);
            calendar.add(Calendar.DAY_OF_YEAR, -duration);
            startDate = calendar.getTime();
            calculateVacationTime(startDate, endDate, null);
        }
    }

    public LiveData<Integer> getTotalTravelDays(String userId) {
        refreshTotalTravelDays(userId);
        return totalTravelDays;
    }

    public LiveData<Integer> getCalculatedDuration() {
        return calculatedDuration;
    }

    private void refreshTotalTravelDays(String userId) {
        firebaseRepository.getTotalTravelDays(userId, new OnCompleteListener<Integer>() {
            @Override
            public void onComplete(@NonNull Task<Integer> task) {
                if (task.isSuccessful()) {
                    totalTravelDays.setValue(task.getResult());
                }
            }
        });
    }
}
