package com.example.sprintproject.model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DestinationDatabase {

    private static DestinationDatabase instance;
    private DatabaseReference databaseReference;
    private DestinationEntry destinationEntry;

    public interface DataStatus {
        void DataIsLoaded(List<DestinationEntry> entries);
    }

    private DestinationDatabase() {
        databaseReference = FirebaseDatabase.getInstance().getReference("destinations");
    }

    public static synchronized DestinationDatabase getInstance() {
        if (instance == null) {
            instance = new DestinationDatabase();
        }
        return instance;
    }

    public void addEntry(String destinationId, DestinationEntry entry) {
        databaseReference.child(destinationId).setValue(entry);
    }

    public void prepopulateDatabase() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChildren()) { // Check if the database is empty
                    // Prepopulate with 2 entries
                    DestinationEntry entry1 = new DestinationEntry(
                            "1",
                            "Paris",
                            new Date(2024, 2, 15), // Replace with actual date
                            new Date(2024, 2, 20)
                    );

                    DestinationEntry entry2 = new DestinationEntry(
                            "2",
                            "Tokyo",
                            new Date(2024, 4, 10),
                            new Date(2024, 4, 20)
                    );

                    addEntry( "1",entry1);
                    addEntry("2", entry2);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("DestinationDatabase", "Failed to read data", databaseError.toException());
            }
        });
    }

    // New method to calculate the duration in days
    public long getDurationInDays() {
        long diffInMillis = destinationEntry.getEndDate().getTime() - destinationEntry.getStartDate().getTime();
        return TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
    }

    public void getAllEntries(final DataStatus dataStatus) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<DestinationEntry> entries = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DestinationEntry entry = snapshot.getValue(DestinationEntry.class);
                    entries.add(entry);
                }
                dataStatus.DataIsLoaded(entries);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("DestinationDatabase", "Failed to get data", databaseError.toException());
            }
        });
    }
}
