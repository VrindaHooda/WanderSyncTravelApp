package com.example.sprintproject.model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DestinationDatabase {

    private static DestinationDatabase instance;
    private DatabaseReference databaseReference;

    private static final String DESTINATIONS_KEY = "destinations";
    private static final String PLANNED_VACATION_DAYS_KEY = "plannedVacationDays";

    public interface DataStatus {
        void DataIsLoaded(List<DestinationEntry> entries);
        void DataLoadFailed(DatabaseError databaseError);
    }

    private DestinationDatabase() {
        databaseReference = FirebaseDatabase.getInstance().getReference(DESTINATIONS_KEY);
    }

    public static synchronized DestinationDatabase getInstance() {
        if (instance == null) {
            instance = new DestinationDatabase();
        }
        return instance;
    }

    public void getPlannedVacationDays(final ValueEventListener listener) {
        databaseReference.child(PLANNED_VACATION_DAYS_KEY).addListenerForSingleValueEvent(listener);
    }

    // Writing to the database
    public void addLogEntry(String userId, String destinationId, DestinationEntry entry) {
        databaseReference.child(userId).child(destinationId).setValue(entry)
                .addOnSuccessListener(aVoid -> Log.d("DestinationDatabase", "Entry added successfully!"))
                .addOnFailureListener(e -> Log.w("DestinationDatabase", "Failed to add entry", e));
    }

    public void prepopulateDestinationDatabase(String userId) {
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean hasNestedChildren = false;

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if (child.hasChildren()) {
                        hasNestedChildren = true;
                        break; // Exit loop as soon as one nested child is found
                    }
                }
                if (hasNestedChildren) {
                    Calendar calendar = Calendar.getInstance();

                    // Example of adding entries
                    addEntry(userId, "1", "Paris", calendar, 2024, Calendar.FEBRUARY, 15, Calendar.FEBRUARY, 20);
                    addEntry(userId, "2", "Tokyo", calendar, 2024, Calendar.APRIL, 10, Calendar.APRIL, 20);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("DestinationDatabase", "Failed to read data", databaseError.toException());
            }
        });
    }

    private void addEntry(String userId, String destinationId, String location, Calendar calendar, int year, int monthStart, int dayStart, int monthEnd, int dayEnd) {
        calendar.set(year, monthStart, dayStart);
        Date startDate = calendar.getTime();
        calendar.set(year, monthEnd, dayEnd);
        Date endDate = calendar.getTime();
        DestinationEntry entry = new DestinationEntry(destinationId, location, startDate, endDate);
        addLogEntry(userId, destinationId, entry);
    }

    // Reads the data
    public void getAllDestinationEntries(String userId, final DataStatus dataStatus) {
        databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<DestinationEntry> entries = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DestinationEntry entry = snapshot.getValue(DestinationEntry.class);
                    entries.add(entry);
                }
                dataStatus.DataIsLoaded(entries);
                Log.d("DestinationDatabase", "Data retrieved successfully: " + entries.size() + " entries found.");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("DestinationDatabase", "Failed to get data", databaseError.toException());
                dataStatus.DataLoadFailed(databaseError);  // Notify the observer about the error
            }
        });
    }
}