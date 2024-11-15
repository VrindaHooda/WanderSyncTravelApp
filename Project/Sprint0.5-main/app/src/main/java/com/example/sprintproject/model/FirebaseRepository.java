package com.example.sprintproject.model;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

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
}

