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

public class DestinationViewModel extends ViewModel {
    private FirebaseRepository firebaseRepository;
    private MutableLiveData<List<TravelLog>> travelLogs;
    private MutableLiveData<Integer> totalTravelDays;
    private MutableLiveData<Integer> totalPlannedDays;
    private MutableLiveData<Integer> calculatedDuration;

    /**
     * Constructs a {@code DestinationViewModel} and initializes the required fields.
     */
    public DestinationViewModel() {
        firebaseRepository = new FirebaseRepository();
        travelLogs = new MutableLiveData<>();
        totalTravelDays = new MutableLiveData<>();
        totalPlannedDays = new MutableLiveData<>();
        calculatedDuration = new MutableLiveData<>();
    }

    /**
     * Retrieves the last five travel logs for a given user ID.
     * If no travel logs exist, the repository
     * prepopulates the user's travel logs.
     *
     * @param userId the user ID
     * @return a {@link LiveData} object containing a list of {@link TravelLog}
     */
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

    /**
     * Logs a past travel entry for a given user ID and refreshes the total travel days.
     *
     * @param userId    the user ID
     * @param travelLog the {@link TravelLog} object to log
     */
    public void logPastTravel(String userId, TravelLog travelLog) {
        firebaseRepository.saveTravelLog(userId, travelLog);
        refreshTotalTravelDays(userId);
    }

    /**
     * Saves a vacation entry for a given user ID and refreshes the total planned vacation days.
     *
     * @param userId        the user ID
     * @param vacationEntry the {@link VacationEntry} object to save
     */
    public void saveVacationEntry(String userId, VacationEntry vacationEntry) {
        firebaseRepository.saveVacationEntry(userId, vacationEntry);
        refreshTotalPlannedDays(userId);
    }

    /**
     * Calculates the vacation time based on provided start date, end date, or duration.
     *
     * @param startD the start date of the vacation
     * @param endD   the end date of the vacation
     * @param duration  the duration of the vacation in days
     */
    public void calculateVacationTime(Date startD, Date endD, Integer duration) {
        if (startD != null && endD != null) {
            firebaseRepository.calculateDuration(startD, endD, new OnCompleteListener<Integer>() {
                @Override
                public void onComplete(@NonNull Task<Integer> task) {
                    if (task.isSuccessful()) {
                        calculatedDuration.setValue(task.getResult());
                    }
                }
            });
        } else if (startD != null && duration != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startD);
            calendar.add(Calendar.DAY_OF_YEAR, duration);
            endD = calendar.getTime();
            calculateVacationTime(startD, endD, null);
        } else if (endD != null && duration != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(endD);
            calendar.add(Calendar.DAY_OF_YEAR, -duration);
            startD = calendar.getTime();
            calculateVacationTime(startD, endD, null);
        }
    }

    /**
     * Retrieves the total travel days for a given user ID.
     *
     * @param userId the user ID
     * @return a {@link LiveData} object containing the total travel days
     */
    public LiveData<Integer> getTotalTravelDays(String userId) {
        refreshTotalTravelDays(userId);
        return totalTravelDays;
    }

    /**
     * Retrieves the total planned vacation days for a given user ID.
     *
     * @param userId the user ID
     * @return a {@link LiveData} object containing the total planned vacation days
     */
    public LiveData<Integer> getTotalPlannedDays(String userId) {
        refreshTotalPlannedDays(userId);
        return totalPlannedDays;
    }

    /**
     * Retrieves the calculated duration between two dates.
     *
     * @return a {@link LiveData} object containing the calculated duration in days
     */
    public LiveData<Integer> getCalculatedDuration() {
        return calculatedDuration;
    }

    /**
     * Refreshes the total travel days for a given user ID by fetching data from the repository.
     *
     * @param userId the user ID
     */
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

    /**
     * Refreshes the total planned vacation days for a
     * given user ID by fetching data from the repository.
     *
     * @param userId the user ID
     */
    private void refreshTotalPlannedDays(String userId) {
        firebaseRepository.getTotalPlannedDays(userId, new OnCompleteListener<Integer>() {
            @Override
            public void onComplete(@NonNull Task<Integer> task) {
                if (task.isSuccessful()) {
                    totalPlannedDays.setValue(task.getResult());
                }
            }
        });
    }

    /**
     * Resets the calculated duration LiveData to {@code null}.
     * This can be used to clear any previously calculated duration data,
     * ensuring a fresh state for future calculations.
     */
    public void resetCalculatedDuration() {
        calculatedDuration.setValue(null);
    }
}
