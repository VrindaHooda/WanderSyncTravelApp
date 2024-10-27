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

    //writing to the database
    public void addLogEntry(String destinationId, DestinationEntry entry) {
        //set up a child to the destination ID and set the value as an entry
        databaseReference.child(destinationId).setValue(entry)
                .addOnSuccessListener(aVoid -> Log.d("DestinationDatabase", "Entry added successfully!"))
                .addOnFailureListener(e -> Log.w("DestinationDatabase", "Failed to add entry", e));
    }

    public void prepopulateDestinationDatabase() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            //takes a data snapshot, as soon as a data changes, it catches it
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChildren()) { // Check if the database is empty
                    // Create dates using Calendar
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(2024, Calendar.FEBRUARY, 15);
                    Date startDate1 = calendar.getTime();
                    calendar.set(2024, Calendar.FEBRUARY, 20);
                    Date endDate1 = calendar.getTime();

                    DestinationEntry entry1 = new DestinationEntry("1", "Paris", startDate1, endDate1);

                    calendar.set(2024, Calendar.APRIL, 10);
                    Date startDate2 = calendar.getTime();
                    calendar.set(2024, Calendar.APRIL, 20);
                    Date endDate2 = calendar.getTime();

                    DestinationEntry entry2 = new DestinationEntry("2", "Tokyo", startDate2, endDate2);

                    addLogEntry("1", entry1);
                    addLogEntry("2", entry2);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("DestinationDatabase", "Failed to read data", databaseError.toException());
            }
        });
    }

    //reads the data
    public void getAllDestinationEntries(final DataStatus dataStatus) {
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
