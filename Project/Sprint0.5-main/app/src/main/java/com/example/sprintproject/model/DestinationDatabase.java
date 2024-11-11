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
        databaseReference.child(PLANNED_VACATION_DAYS_KEY)
                .addListenerForSingleValueEvent(listener);
    }

    public void addLogEntry(String userId, String destinationId, DestinationEntry entry) {
        databaseReference.child(userId).child(destinationId)
                .setValue(entry)
                .addOnSuccessListener(aVoid -> Log.d("DestinationDatabase",
                        "Entry added successfully!"))
                .addOnFailureListener(e -> Log.w("DestinationDatabase",
                        "Failed to add entry", e));
    }

    public void prepopulateDestinationDatabase(String userId) {
        databaseReference.child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean hasNestedChildren = false;

                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            if (child.hasChildren()) {
                                hasNestedChildren = true;
                                break;
                            }
                        }
                        if (hasNestedChildren) {
                            addSampleEntries(userId);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("DestinationDatabase", "Failed to read data",
                                databaseError.toException());
                    }
                });
    }

    private void addSampleEntries(String userId) {
        addEntry(userId, "1", "Paris", 2024, Calendar.FEBRUARY, 15, Calendar.FEBRUARY, 20);
        addEntry(userId, "2", "Tokyo", 2024, Calendar.APRIL, 10, Calendar.APRIL, 20);
    }

    private void addEntry(String userId, String destinationId, String location,
                          int year, int monthStart, int dayStart, int monthEnd, int dayEnd) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthStart, dayStart);
        Date startDate = calendar.getTime();
        calendar.set(year, monthEnd, dayEnd);
        Date endDate = calendar.getTime();
        DestinationEntry entry = new DestinationEntry(destinationId, location, startDate, endDate);
        addLogEntry(userId, destinationId, entry);
    }

    public void getAllDestinationEntries(String userId, final DataStatus dataStatus) {
        databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<DestinationEntry> entries = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DestinationEntry entry = snapshot.getValue(DestinationEntry.class);
                    entries.add(entry);
                }
                dataStatus.dataIsLoaded(entries);
                Log.d("DestinationDatabase", "Data retrieved successfully: "
                        + entries.size() + " entries found.");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("DestinationDatabase", "Failed to get data",
                        databaseError.toException());
                dataStatus.dataLoadFailed(databaseError);
            }
        });
    }
    public interface DataStatus {
        void dataIsLoaded(List<DestinationEntry> entries);
        void dataLoadFailed(DatabaseError databaseError);
    }
}
