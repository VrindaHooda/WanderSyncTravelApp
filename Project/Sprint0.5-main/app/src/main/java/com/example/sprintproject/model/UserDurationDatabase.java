package com.example.sprintproject.model;

import android.util.Log;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserDurationDatabase {

    private static UserDurationDatabase userDurationDatabaseInstance;
    private DatabaseReference userDurationDatabaseReference;

    private UserDurationDatabase() {
        userDurationDatabaseReference = FirebaseDatabase.getInstance().getReference("users");
    }

    public static synchronized UserDurationDatabase getInstance() {
        if (userDurationDatabaseInstance == null) {
            userDurationDatabaseInstance = new UserDurationDatabase();
        }
        return userDurationDatabaseInstance;
    }

    public void addVacationEntry(String userId, String email, DurationEntry entry) {
        UserEntry userData = new UserEntry(email, entry);
        userDurationDatabaseReference.child(userId)
                .setValue(userData)
                .addOnSuccessListener(aVoid ->
                        Log.d("UserDurationDatabase",
                                "Entry added successfully for userId: " + userId))
                .addOnFailureListener(e ->
                        Log.w("UserDurationDatabase", "Failed to add entry for userId: "
                                + userId, e));
    }

    public void getVacationEntry(String userId, final DataStatus dataStatus) {
        if (dataStatus == null) {
            Log.w("UserDurationDatabase", "DataStatus callback is null");
            return;
        }

        userDurationDatabaseReference.child(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserEntry userData = dataSnapshot.getValue(UserEntry.class);
                        if (userData != null) {
                            dataStatus.dataIsLoaded(userId, userData.getEmail(),
                                    userData.getEntry());
                        } else {
                            Log.w("UserDurationDatabase", "No data found for userId: "
                                    + userId);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("UserDurationDatabase",
                                "Failed to get data for userId: " + userId, databaseError
                                        .toException());
                    }
                });

    }

    public interface DataStatus {
        void dataIsLoaded(String userId, String email, DurationEntry entry);
    }
}
