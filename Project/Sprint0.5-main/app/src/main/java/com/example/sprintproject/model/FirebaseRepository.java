package com.example.sprintproject.model;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import androidx.annotation.NonNull;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class FirebaseRepository {
    private final FirebaseFirestore firestore;

    public FirebaseRepository() {
        firestore = FirebaseFirestore.getInstance();
    }

    public void saveTravelLog(String userId, TravelLog travelLog) {
        firestore.collection("users")
                .document(userId)
                .collection("destination entries")
                .add(travelLog)
                .addOnSuccessListener(documentReference -> {
                    // Handle success
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                });
    }

    public void getLastFiveTravelLogs(String userId, OnCompleteListener<QuerySnapshot> listener) {
        firestore.collection("users")
                .document(userId)
                .collection("destination entries")
                .orderBy("startDate", Query.Direction.DESCENDING)
                .limit(5)
                .get()
                .addOnCompleteListener(listener);
    }

    public void saveVacationEntry(String userId, VacationEntry vacationEntry) {
        firestore.collection("users")
                .document(userId)
                .collection("vacation entries")
                .add(vacationEntry)
                .addOnSuccessListener(documentReference -> {
                    // Handle success
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                });
    }

    public void prepopulateTravelLogsIfNoneExist(String userId) {
        firestore.collection("users")
                .document(userId)
                .collection("destination entries")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && querySnapshot.isEmpty()) {
                                // No travel logs exist, prepopulate with two default travel logs
                                TravelLog log1 = new TravelLog("Paris", new Date(123, 5, 1), new Date(123, 5, 10));
                                TravelLog log2 = new TravelLog("New York", new Date(124, 0, 15), new Date(124, 0, 20));

                                // Add both travel logs to Firestore
                                firestore.collection("users")
                                        .document(userId)
                                        .collection("destination entries")
                                        .add(log1)
                                        .addOnSuccessListener(documentReference -> {
                                            // Handle success for log1
                                        })
                                        .addOnFailureListener(e -> {
                                            // Handle failure for log1
                                        });

                                firestore.collection("users")
                                        .document(userId)
                                        .collection("destination entries")
                                        .add(log2)
                                        .addOnSuccessListener(documentReference -> {
                                            // Handle success for log2
                                        })
                                        .addOnFailureListener(e -> {
                                            // Handle failure for log2
                                        });
                            }
                        } else {
                            // Handle failure to check for existing travel logs
                            Exception exception = task.getException();
                            if (exception != null) {
                                exception.printStackTrace();
                            }
                        }
                    }
                });
    }

    public void getTotalTravelDays(String userId, OnCompleteListener<Integer> listener) {
        firestore.collection("users")
                .document(userId)
                .collection("destination entries")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            int totalDays = 0;
                            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                TravelLog travelLog = document.toObject(TravelLog.class);
                                if (travelLog != null) {
                                    long diffInMillies = Math.abs(travelLog.getEndDate().getTime() - travelLog.getStartDate().getTime());
                                    int days = (int) TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                                    totalDays += days;
                                }
                            }
                            listener.onComplete(Tasks.forResult(totalDays));
                        } else {
                            listener.onComplete(Tasks.forResult(0));
                        }
                    } else {
                        // Handle failure to retrieve travel logs
                        listener.onComplete(Tasks.forException(task.getException()));
                    }
                });
    }

    public void calculateDuration(Date startDate, Date endDate, OnCompleteListener<Integer> listener) {
        if (startDate != null && endDate != null) {
            long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
            int duration = (int) TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            listener.onComplete(Tasks.forResult(duration));
        } else {
            listener.onComplete(Tasks.forException(new IllegalArgumentException("Start date or end date cannot be null")));
        }
    }

    public void getTotalPlannedDays(String userId, OnCompleteListener<Integer> listener) {
        firestore.collection("users")
                .document(userId)
                .collection("vacation entries")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            int totalPlannedDays = 0;
                            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                VacationEntry vacationEntry = document.toObject(VacationEntry.class);
                                if (vacationEntry != null) {
                                    totalPlannedDays += vacationEntry.getDuration();
                                }
                            }
                            listener.onComplete(Tasks.forResult(totalPlannedDays));
                        } else {
                            listener.onComplete(Tasks.forResult(0));
                        }
                    } else {
                        // Handle failure to retrieve vacation entries
                        listener.onComplete(Tasks.forException(task.getException()));
                    }
                });
    }

    // Add a new plan
    public void savePlan(String userId, Plan plan, OnCompleteListener<DocumentReference> listener) {
        firestore.collection("users")
                .document(userId)
                .collection("plans")
                .add(plan)
                .addOnCompleteListener(listener)
                .addOnFailureListener(e -> {
                    // Handle failure
                    e.printStackTrace();
                });
    }

    // Get all plans for a user
    public void getPlans(String userId, OnCompleteListener<QuerySnapshot> listener) {
        firestore.collection("users")
                .document(userId)
                .collection("plans")
                .get()
                .addOnCompleteListener(listener)
                .addOnFailureListener(e -> {
                    // Handle failure
                    e.printStackTrace();
                });
    }

    // Update an existing plan
    public void updatePlan(String userId, String planId, Plan updatedPlan, PlanCallback callback) {
        firestore.collection("users")
                .document(userId)
                .collection("plans")
                .document(planId)
                .set(updatedPlan)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onPlanAdded(updatedPlan); // Successfully updated the plan
                    } else {
                        callback.onFailure("Failed to update plan"); // Handle failure
                    }
                })
                .addOnFailureListener(e -> {
                    // Log failure in case addOnCompleteListener doesn't catch it
                    e.printStackTrace();
                    callback.onFailure(e.getMessage());
                });
    }

    // Delete a plan
    public void deletePlan(String userId, String planId, OnCompleteListener<Void> listener) {
        firestore.collection("users")
                .document(userId)
                .collection("plans")
                .document(planId)
                .delete()
                .addOnCompleteListener(listener)
                .addOnFailureListener(e -> {
                    // Handle failure
                    e.printStackTrace();
                });
    }

    public void sendInvite(String userId, String planId, String senderId, String tripName) {
        Map<String, Object> invite = new HashMap<>();
        invite.put("planId", planId);
        invite.put("senderId", senderId);
        invite.put("tripName", tripName);
        invite.put("status", "Pending");

        firestore.collection("users")
                .document(userId)
                .collection("invites")
                .add(invite)
                .addOnSuccessListener(documentReference -> {
                    Log.d("FirebaseRepository", "Invite sent successfully");
                })
                .addOnFailureListener(e -> {
                    Log.e("FirebaseRepository", "Failed to send invite", e);
                });
    }

    public void addPlan(String userId, Plan plan, PlanCallback callback) {
        if (plan == null || userId == null) {
            callback.onFailure("Invalid plan or user ID");
            return;
        }

        // Validate plan fields before proceeding (optional)
        if (plan.getDestinations() == null || plan.getDestinations().isEmpty()) {
            callback.onFailure("Plan must contain at least one destination.");
            return;
        }

        firestore.collection("users")
                .document(userId)
                .collection("plans")
                .add(plan)
                .addOnSuccessListener(documentReference -> {
                    // Firestore automatically generates a planId, no need for explicit setId here.
                    String planId = documentReference.getId();
                    plan.setId(planId);  // Still set the ID on the Plan object if needed for the next operations.

                    // Handle post-add actions
                    callback.onPlanAdded(plan);
                })
                .addOnFailureListener(e -> {
                    // Log the error for debugging
                    Log.e("AddPlanError", "Error adding plan: " + e.getMessage());
                    callback.onFailure("Failed to add plan: " + e.getMessage());
                });
    }

    // Callback interface for adding plans
    public interface PlanCallback {
        void onPlanAdded(Plan plan);
        void onFailure(String error);
    }

}