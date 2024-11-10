package com.example.sprintproject.model;

import android.util.Log;

import com.example.sprintproject.model.Entry;
import com.example.sprintproject.model.UserEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserDurationDatabase {
    private static UserDurationDatabase userDurationDatabaseinstance;
    private DatabaseReference userDurationDatabaseReference;

    public interface DataStatus {
        void DataIsLoaded(String userId, String email, Entry entry);
        void DataNotFound(String userId); // Callback for no data
    }

    private UserDurationDatabase() {
        userDurationDatabaseReference = FirebaseDatabase.getInstance().getReference("users");
    }

    public static synchronized UserDurationDatabase getInstance() {
        if (userDurationDatabaseinstance == null) {
            userDurationDatabaseinstance = new UserDurationDatabase();
        }
        return userDurationDatabaseinstance;
    }

    public void addVacationEntry(String userId, String email, Entry entry) {
        if (userId == null || email == null || entry == null) {
            Log.w("UserDurationDatabase", "Invalid input: userId, email, or entry is null.");
            return;
        }

        UserEntry userData = new UserEntry(email, entry);
        userDurationDatabaseReference.child(userId).setValue(userData)
                .addOnSuccessListener(aVoid -> Log.d("UserDurationDatabase", "Entry added successfully for userId: " + userId + ", Email: " + email))
                .addOnFailureListener(e -> Log.w("UserDurationDatabase", "Failed to add entry for userId: " + userId, e));
    }

    public void getVacationEntry(String userId, final DataStatus dataStatus) {
        if (dataStatus == null) {
            Log.w("UserDurationDatabase", "DataStatus callback is null");
            return;
        }

        if (userId == null) {
            Log.w("UserDurationDatabase", "UserId is null");
            return;
        }

        userDurationDatabaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserEntry userData = dataSnapshot.getValue(UserEntry.class);
                if (userData != null) {
                    dataStatus.DataIsLoaded(userId, userData.getEmail(), userData.getEntry());
                } else {
                    dataStatus.DataNotFound(userId);
                    Log.w("UserDurationDatabase", "No data found for userId: " + userId);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("UserDurationDatabase", "Failed to get data for userId: " + userId, databaseError.toException());
            }
        });
    }
}
