package com.example.sprintproject.model;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import androidx.annotation.NonNull;

import java.util.Date;

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
}
