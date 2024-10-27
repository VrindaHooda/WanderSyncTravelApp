package com.example.sprintproject.model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserDurationDatabase {
    private static UserDurationDatabase userDurationDatabaseinstance;
    private DatabaseReference userDurationDatabaseReference;

    public interface DataStatus {
        void DataIsLoaded(List<DurationEntry> entries);
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

    public void addVacationDurationEntry(String vacationId, DurationEntry entry) {
        userDurationDatabaseReference.child(vacationId).setValue(entry)
                .addOnSuccessListener(aVoid -> Log.d("UserDurationDatabase", "Entry added successfully!"))
                .addOnFailureListener(e -> Log.w("UserDurationDatabase", "Failed to add entry", e));
    }

    public void getAllUserDurationEntries(final UserDurationDatabase.DataStatus dataStatus) {
        userDurationDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<DurationEntry> entries = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DurationEntry entry = snapshot.getValue(DurationEntry.class);
                    entries.add(entry);
                }
                dataStatus.DataIsLoaded(entries);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("UserDurationDatabase", "Failed to get data", databaseError.toException());
            }
        });
    }
}
