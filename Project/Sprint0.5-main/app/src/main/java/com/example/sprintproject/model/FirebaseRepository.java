package com.example.sprintproject.model;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class FirebaseRepository {
    /**
     * The Firestore database instance.
     */
    private final FirebaseFirestore firestore;

    /**
     * Constructs a {@code FirebaseRepository} and initializes the Firestore instance.
     */
    public FirebaseRepository() {
        firestore = FirebaseFirestore.getInstance();
    }

    /**
     * Saves a travel log for a specified user.
     *
     * @param userId    the user ID
     * @param travelLog the {@code TravelLog} object to save
     */
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

    /**
     * Retrieves the last five travel logs for a specified user.
     *
     * @param userId   the user ID
     * @param listener the {@code OnCompleteListener} to handle the result
     */
    public void getLastFiveTravelLogs(String userId, OnCompleteListener<QuerySnapshot> listener) {
        firestore.collection("users")
                .document(userId)
                .collection("destination entries")
                .orderBy("startDate", Query.Direction.DESCENDING)
                .limit(5)
                .get()
                .addOnCompleteListener(listener);
    }

    /**
     * Saves a vacation entry for a specified user.
     *
     * @param userId        the user ID
     * @param vacationEntry the {@code VacationEntry} object to save
     */
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

    /**
     * Prepopulates travel logs for a user if none exist in their Firestore collection.
     *
     * @param userId the user ID
     */
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
                                TravelLog log1 = new TravelLog("Paris", new Date(123, 5, 1),
                                        new Date(123, 5, 10));
                                TravelLog log2 = new TravelLog("New York", new Date(124, 0, 15),
                                        new Date(124, 0, 20));

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

    /**
     * Retrieves the total planned days for all vacation entries of a specified user.
     *
     * @param userId   the user ID
     * @param listener the {@code OnCompleteListener} to handle the result
     */
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
                                    long diffInMillies = Math.abs(travelLog.getEndDate().getTime()
                                            - travelLog.getStartDate().getTime());
                                    int days = (int) TimeUnit.DAYS.convert(diffInMillies,
                                            TimeUnit.MILLISECONDS);
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

    /**
     * Calculates the duration in days between two dates.
     *
     * @param startDate the start date
     * @param endDate   the end date
     * @param listener  the {@code OnCompleteListener} to handle the result
     *                  with the calculated duration in days or an exception if the input is invalid
     */
    public void calculateDuration(Date startDate, Date endDate,
                                  OnCompleteListener<Integer> listener) {
        if (startDate != null && endDate != null) {
            long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
            int duration = (int) TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            listener.onComplete(Tasks.forResult(duration));
        } else {
            listener.onComplete(Tasks.forException(new IllegalArgumentException(
                    "Start date or end date cannot be null")));
        }
    }

    /**
     * Retrieves the total planned days for all vacation entries of a specified user.
     *
     * @param userId   the user ID
     * @param listener the {@code OnCompleteListener} to handle the result
     */
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
                                VacationEntry vacationEntry = document.toObject(
                                        VacationEntry.class);
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
    /**
     * Saves a plan for a specified user.
     *
     * @param userId    the user ID
     * @param plan      the {@code Plan} object to save
     * @param listener  the {@code OnCompleteListener} to handle the result
     */
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
    /**
     * Retrieves all plans for a specified user.
     *
     * @param userId   the user ID
     * @param listener the {@code OnCompleteListener} to handle the result
     */
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
    /**
     * Updates an existing plan for a specified user.
     *
     * @param userId    the user ID
     * @param planId    the unique ID of the plan to update
     * @param updatedPlan the updated {@code Plan} object
     * @param listener  the {@code OnCompleteListener} to handle the result of the update operation
     */
    public void updatePlan(String userId, String planId,
                           Plan updatedPlan, OnCompleteListener<Void> listener) {
        firestore.collection("users")
                .document(userId)
                .collection("plans")
                .document(planId)
                .set(updatedPlan)
                .addOnCompleteListener(listener)
                .addOnFailureListener(e -> {
                    // Handle failure
                    e.printStackTrace();
                });
    }

    // Delete a plan
    /**
     * Deletes an existing plan for a specified user.
     *
     * @param userId   the user ID
     * @param planId   the unique ID of the plan to delete
     * @param listener the {@code OnCompleteListener} to handle the result of the delete operation
     */
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

    /**
     * Sends an invite for a trip plan to another user.
     *
     * @param userId   the ID of the user receiving the invite
     * @param planId   the unique ID of the plan being shared
     * @param senderId the ID of the user sending the invite
     * @param tripName the name of the trip
     */
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

    /**
     * Adds a plan for a specified user and invokes a
     * callback with the generated plan ID or an error message.
     *
     * @param userId   the user ID
     * @param plan     the {@code Plan} object to add
     * @param callback the {@code PlanCallback} to handle success or failure
     */
    public void addPlan(String userId, Plan plan, PlanCallback callback) {
        firestore.collection("users")
                .document(userId)
                .collection("plans")
                .add(plan)
                .addOnSuccessListener(documentReference -> {
                    String planId = documentReference.getId(); // Get generated document ID
                    callback.onPlanAdded(planId);
                })
                .addOnFailureListener(e -> {
                    callback.onFailure(e.getMessage());
                });
    }

    // Callback interface for adding plans
    /**
     * A callback interface for handling the result of adding a plan.
     */
    public interface PlanCallback {
        /**
         * Called when a plan is successfully added.
         *
         * @param planId the generated ID of the newly added plan
         */
        void onPlanAdded(String planId);

        /**
         * Called when there is an error adding a plan.
         *
         * @param error the error message
         */
        void onFailure(String error);
    }


    private static final String COLLECTION_NAME = "dining_reservations";

    public void saveDiningReservation(String userId, DiningReservation reservation, OnCompleteListener<Void> listener) {
        // Set the userId in the reservation object
        reservation.setUserId(userId);

        // Generate a new document ID if not already set
        if (reservation.getId() == null || reservation.getId().isEmpty()) {
            String reservationId = firestore.collection(COLLECTION_NAME).document().getId();
            reservation.setId(reservationId);
        }

        // Save the reservation to Firestore
        firestore.collection(COLLECTION_NAME)
                .document(reservation.getId())
                .set(reservation, SetOptions.merge())
                .addOnCompleteListener(listener);
    }

    public interface OnCategorizedReservationsListener {
        void onSuccess(Map<String, List<DiningReservation>> categorizedReservations);
        void onFailure(Exception e);
    }

    public void getCategorizedDiningReservations(String userId, OnCategorizedReservationsListener listener) {
        CollectionReference reservationsRef = firestore.collection(COLLECTION_NAME);

        // Query for reservations belonging to the user
        reservationsRef
                .whereEqualTo("userId", userId)
                .orderBy("reservationDate", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<DiningReservation> upcoming = new ArrayList<>();
                        List<DiningReservation> past = new ArrayList<>();
                        Date currentDate = new Date();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            DiningReservation reservation = document.toObject(DiningReservation.class);

                            if (reservation.getReservationDate() != null) {
                                if (reservation.getReservationDate().after(currentDate)) {
                                    upcoming.add(reservation);
                                } else {
                                    past.add(reservation);
                                }
                            }
                        }

                        Map<String, List<DiningReservation>> categorizedReservations = new HashMap<>();
                        categorizedReservations.put("upcoming", upcoming);
                        categorizedReservations.put("past", past);

                        // Call the onSuccess method directly
                        listener.onSuccess(categorizedReservations);

                    } else {
                        // Call the onFailure method with the exception
                        listener.onFailure(task.getException());
                    }
                });
    }


}
