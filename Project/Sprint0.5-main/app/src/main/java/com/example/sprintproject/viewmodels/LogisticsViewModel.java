package com.example.sprintproject.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sprintproject.model.FirebaseRepository;
import com.example.sprintproject.model.Plan;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class LogisticsViewModel extends ViewModel {
    private final FirebaseRepository firebaseRepository;
    private final MutableLiveData<List<Plan>> plansLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LogisticsViewModel() {
        firebaseRepository = new FirebaseRepository();
        isLoading.setValue(false);
    }

    public LiveData<List<Plan>> getPlansLiveData() {
        return plansLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void fetchPlans(String userId) {
        isLoading.setValue(true);
        firebaseRepository.getPlans(userId, task -> {
            isLoading.setValue(false);
            if (task.isSuccessful() && task.getResult() != null) {
                List<Plan> plans = task.getResult().toObjects(Plan.class);
                plansLiveData.setValue(plans);
            } else {
                errorMessage.setValue("Failed to fetch plans.");
            }
        });
    }

    public void addPlan(String userId, Plan plan) {
        isLoading.setValue(true);
        firebaseRepository.savePlan(userId, plan, task -> {
            isLoading.setValue(false);
            if (task.isSuccessful()) {
                fetchPlans(userId); // Refresh the plans after adding
            } else {
                errorMessage.setValue("Failed to add plan.");
            }
        });
    }

    public void deletePlan(String userId, String planId) {
        isLoading.setValue(true);
        firebaseRepository.deletePlan(userId, planId, task -> {
            isLoading.setValue(false);
            if (task.isSuccessful()) {
                fetchPlans(userId); // Refresh the plans after deleting
            } else {
                errorMessage.setValue("Failed to delete plan.");
            }
        });
    }

    public void updatePlan(String userId, String planId, Plan updatedPlan) {
        isLoading.setValue(true);
        firebaseRepository.updatePlan(userId, planId, updatedPlan, task -> {
            isLoading.setValue(false);
            if (task.isSuccessful()) {
                fetchPlans(userId); // Refresh the plans after updating
            } else {
                errorMessage.setValue("Failed to update plan.");
            }
        });
    }

}

