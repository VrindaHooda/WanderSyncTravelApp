package com.example.sprintproject.model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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

    public void addEntry(String destinationId, DestinationEntry entry) {
        databaseReference.child(destinationId).setValue(entry);
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
            }
        });
    }
}
