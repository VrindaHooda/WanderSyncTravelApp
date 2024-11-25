package com.example.sprintproject.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sprintproject.model.DiningReservation;
import com.example.sprintproject.model.FirebaseRepository;

import java.util.List;
import java.util.Map;

public class DiningViewModel extends ViewModel {
    private final FirebaseRepository repository;
    private final MutableLiveData<List<DiningReservation>> upcomingReservations;
    private final MutableLiveData<List<DiningReservation>> pastReservations;
    private final MutableLiveData<Boolean> isLoading;
    private final MutableLiveData<String> errorMessage;

    public DiningViewModel() {
        repository = new FirebaseRepository();
        upcomingReservations = new MutableLiveData<>();
        pastReservations = new MutableLiveData<>();
        isLoading = new MutableLiveData<>(false);
        errorMessage = new MutableLiveData<>(null);
    }

    public LiveData<List<DiningReservation>> getUpcomingReservations() {
        return upcomingReservations;
    }

    public LiveData<List<DiningReservation>> getPastReservations() {
        return pastReservations;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void fetchCategorizedDiningReservations(String userId) {
        isLoading.setValue(true);
        repository.getCategorizedDiningReservations(userId, new FirebaseRepository.OnCategorizedReservationsListener() {
            @Override
            public void onSuccess(Map<String, List<DiningReservation>> categorizedReservations) {
                isLoading.postValue(false);
                upcomingReservations.postValue(categorizedReservations.get("upcoming"));
                pastReservations.postValue(categorizedReservations.get("past"));
            }

            @Override
            public void onFailure(Exception e) {
                isLoading.postValue(false);
                errorMessage.postValue(e != null ? e.getMessage() : "Failed to load categorized reservations.");
            }
        });
    }


    public void addDiningReservation(String userId, DiningReservation reservation) {
        isLoading.setValue(true);
        repository.saveDiningReservation(userId, reservation, task -> {
            isLoading.postValue(false);
            if (task.isSuccessful()) {
                fetchCategorizedDiningReservations(userId);
            } else {
                Exception e = task.getException();
                errorMessage.postValue(e != null ? e.getMessage() : "Failed to add reservation.");
            }
        });
    }
}
