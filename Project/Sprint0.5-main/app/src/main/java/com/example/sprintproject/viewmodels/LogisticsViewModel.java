package com.example.sprintproject.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sprintproject.model.FirebaseRepository;
import com.example.sprintproject.model.Plan;

import java.util.List;

public class LogisticsViewModel extends ViewModel {
    private final FirebaseRepository firebaseRepository;
    private final MutableLiveData<List<Plan>> plansLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    /**
     * Constructs a {@code LogisticsViewModel} and initializes the repository and loading state.
     */
    public LogisticsViewModel() {
        firebaseRepository = new FirebaseRepository();
        isLoading.setValue(false);
    }

    /**
     * Returns a {@link LiveData} object to observe the list of plans.
     *
     * @return the LiveData object containing a list of {@link Plan}
     */
    public LiveData<List<Plan>> getPlansLiveData() {
        return plansLiveData;
    }

    /**
     * Returns a {@link LiveData} object to observe the loading state.
     *
     * @return the LiveData object containing the loading state as {@link Boolean}
     */
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    /**
     * Returns a {@link LiveData} object to observe error messages.
     *
     * @return the LiveData object containing error messages as {@link String}
     */
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    /**
     * Fetches the list of plans for the given user ID and updates the plans LiveData.
     *
     * @param userId the user ID whose plans need to be fetched
     */
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

    /**
     * Adds a new plan for the given user ID and refreshes the plans list upon success.
     *
     * @param userId the user ID for whom the plan is being added
     * @param plan   the {@link Plan} object to add
     */
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

    /**
     * Deletes a plan for the given user ID and plan ID, refreshing the plans list upon success.
     *
     * @param userId the user ID whose plan needs to be deleted
     * @param planId the ID of the plan to delete
     */
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

    /**
     * Updates an existing plan for the given user ID and plan ID, refreshing the plans list upon success.
     *
     * @param userId      the user ID whose plan needs to be updated
     * @param planId      the ID of the plan to update
     * @param updatedPlan the updated {@link Plan} object
     */
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

