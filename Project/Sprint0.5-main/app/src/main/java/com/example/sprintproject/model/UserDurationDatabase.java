package com.example.sprintproject.model;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class UserDurationDatabase {
    private static UserDurationDatabase userDurationDatabaseinstance;
    private DatabaseReference userDurationDatabaseReference;

    public interface DataStatus {
        void DataIsLoaded(String userId, String email, DurationEntry entry, List<LiveData<ContributorEntry>> contributors);
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

    public void addVacationEntry(String userId, String email, DurationEntry entry, List<LiveData<ContributorEntry>> contributors) {
        UserEntry userData = new UserEntry(email, entry, contributors);
        userDurationDatabaseReference.child(userId).setValue(userData)
                .addOnSuccessListener(aVoid -> Log.d("UserDurationDatabase", "Entry added successfully for userId: " + userId))
                .addOnFailureListener(e -> Log.w("UserDurationDatabase", "Failed to add entry for userId: " + userId, e));
    }


    public void getVacationEntry(String userId, final DataStatus dataStatus, List<LiveData<ContributorEntry>> contributors) {
        if (dataStatus == null) {
            Log.w("UserDurationDatabase", "DataStatus callback is null");
            return;
        }

        userDurationDatabaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserEntry userData = dataSnapshot.getValue(UserEntry.class);
                if (userData != null) {
                    dataStatus.DataIsLoaded(userId, userData.getEmail(), userData.getEntry(), userData.getContributors());
                } else {
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
