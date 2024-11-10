package com.example.sprintproject.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.sprintproject.model.Entry;
import com.example.sprintproject.model.UserEntry;
import com.example.sprintproject.viewmodels.AuthViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserDurationDatabase {
    private static UserDurationDatabase userDurationDatabaseinstance;
    private DatabaseReference userDurationDatabaseReference;
    private static String userId = AuthRepository.getAuthRepository().getCurrentUser().getUid();
    private String plan;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");


    public static String getUserId() {
        return userId;
    }

    public interface DataStatus {
        void DataIsLoaded(String userId, String email, Entry entry);
        void DataNotFound(String userId); // Callback for no data
    }

    public interface DataStatus2 {
        void DataIsLoaded(String userId, ContributorEntryList contributorEntryList);
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

    public void addVacationEntry(String email, Entry entry) {
        if (userId == null || email == null || entry == null) {
            Log.w("UserDurationDatabase", "Invalid input: userId, email, or entry is null.");
            return;
        }

        UserEntry userData = new UserEntry(email, entry);
        userDurationDatabaseReference.child(userId).setValue(userData)
                .addOnSuccessListener(aVoid -> Log.d("UserDurationDatabase", "Entry added successfully for userId: " + userId + ", Email: " + email))
                .addOnFailureListener(e -> Log.w("UserDurationDatabase", "Failed to add entry for userId: " + userId, e));
    }

    public void getVacationEntry(final DataStatus dataStatus) {
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

    public void addContributorsListEntry(ContributorEntry entry, ContributorEntryList contributorsList) {
        if (userId == null || entry == null || contributorsList == null) {
            Log.w("UserDurationDatabase", "Invalid input: userId, entry, or contributorsList is null.");
            return;
        }

        databaseReference.child(userId).child("plan").child("planValue").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String planValue = dataSnapshot.getValue(String.class);
                    plan = planValue;
                    Log.d("PlanValue", "Plan value: " + planValue);
                } else {
                    Log.d("PlanValue", "No plan value found for this user.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DatabaseError", "Error fetching plan value", databaseError.toException());
            }
        });

        DatabaseReference userRef = userDurationDatabaseReference.child(userId).child(plan);

        // Check if the userId child exists in the database
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().exists()) {
                    // If the userId child exists, retrieve the current contributorsList and add the new entry
                    ContributorEntryList existingList = task.getResult().getValue(new GenericTypeIndicator<ContributorEntryList>() {});
                    if (existingList == null) {
                        existingList = new ContributorEntryList();
                    }
                    existingList.add(entry);

                    // Update the database with the updated list
                    userRef.setValue(existingList)
                            .addOnSuccessListener(aVoid -> Log.d("UserDurationDatabase", "ContributorEntryList updated successfully for userId: " + userId))
                            .addOnFailureListener(e -> Log.w("UserDurationDatabase", "Failed to update entry for userId: " + userId, e));
                } else {
                    // If the userId child doesn't exist, create it with the provided list
                    contributorsList.add(entry); // add the entry to the provided list
                    userRef.setValue(contributorsList)
                            .addOnSuccessListener(aVoid -> Log.d("UserDurationDatabase", "ContributorEntryList created successfully for userId: " + userId))
                            .addOnFailureListener(e -> Log.w("UserDurationDatabase", "Failed to create entry for userId: " + userId, e));
                }
            } else {
                Log.w("UserDurationDatabase", "Database check failed for userId: " + userId, task.getException());
            }
        });
    }

    public void getContributorsList(final DataStatus2 dataStatus2) {
        if (dataStatus2 == null) {
            Log.w("UserDurationDatabase", "DataStatus callback is null");
            return;
        }

        if (userId == null) {
            Log.w("UserDurationDatabase", "UserId is null");
            return;
        }

        databaseReference.child(userId).child("plan").child("planValue").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String planValue = dataSnapshot.getValue(String.class);
                    plan = planValue;
                    Log.d("PlanValue", "Plan value: " + planValue);
                } else {
                    Log.d("PlanValue", "No plan value found for this user.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DatabaseError", "Error fetching plan value", databaseError.toException());
            }
        });


        userDurationDatabaseReference.child(userId).child(plan).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ContributorEntryList contributorsList = dataSnapshot.getValue(ContributorEntryList.class);
                if (contributorsList != null) {
                    dataStatus2.DataIsLoaded(userId, contributorsList);
                } else {
                    dataStatus2.DataNotFound(userId);
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
