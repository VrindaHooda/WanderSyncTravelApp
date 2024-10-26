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

    /**
     * Adds a contributor to a specific trip's contributors list.
     * @param destinationId The unique identifier for the trip.
     * @param userId The ID of the user being added.
     * @param email The email of the contributor.
     */
    public void addContributor(String destinationId, String userId, String email) {
        databaseReference.child(destinationId).child("contributors").child(userId).setValue(email);
    }

    /**
     * Adds or updates a destination in the trip's destinations list.
     * @param destinationId The unique identifier for the trip.
     * @param destination The DestinationEntry object to add or update.
     */
    public void addOrUpdateDestination(String destinationId, DestinationEntry destination) {
        databaseReference.child(destinationId).child("destinations").child(destination.getDestinationId()).setValue(destination);
    }

    /**
     * Adds a note to a specific trip's notes list.
     * @param destinationId The unique identifier for the trip.
     * @param noteText The text content of the note.
     */
    public void addNoteToTrip(String destinationId, String noteText) {
        // Generate a unique note ID under the notes section for the trip
        String noteId = databaseReference.child(destinationId).child("notes").push().getKey();
        if (noteId != null) {
            databaseReference.child(destinationId).child("notes").child(noteId).setValue(new NoteEntry(noteId, noteText));
        } else {
            Log.e("DestinationDatabase", "Failed to generate note ID");
        }
    }

    /**
     * Retrieves the list of contributors for a specific trip and returns it via a callback.
     * @param destinationId The unique identifier for the trip.
     * @param callback The callback interface for handling the list of contributors.
     */
    public void getContributors(String destinationId, DataStatus callback) {
        databaseReference.child(destinationId).child("contributors").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<String> contributors = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    String email = child.getValue(String.class);
                    if (email != null) {
                        contributors.add(email);
                    }
                }
                callback.DataIsLoaded(contributors);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("DestinationDatabase", "Failed to load contributors", error.toException());
            }
        });
    }

}
