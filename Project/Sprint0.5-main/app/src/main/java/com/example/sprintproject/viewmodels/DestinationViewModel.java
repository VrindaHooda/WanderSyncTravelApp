package com.example.sprintproject.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.sprintproject.model.DestinationDatabase;
import com.example.sprintproject.model.DestinationEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class DestinationViewModel extends ViewModel {

    private MutableLiveData<DestinationEntry> destinationEntryLiveData;
    private DestinationDatabase databaseHelper;

    public void DestinationViewModel() {
        destinationEntryLiveData = new MutableLiveData<>();

        databaseHelper = DestinationDatabase.getInstance();
    }

    public LiveData<DestinationEntry> getDestinationEntryLiveData() {
        return destinationEntryLiveData;
    }

    public void writeDestinationEntryData(String destinationEntryId, DestinationEntry destinationEntry) {
        databaseHelper.writeData("destinationEntries/" + destinationEntryId, destinationEntry);
    }

    public void readDestinationEntryData(String destinationEntryId) {
        databaseHelper.readData("destinationEntries/" + destinationEntryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DestinationEntry destinationEntry = dataSnapshot.getValue(DestinationEntry.class);

                destinationEntryLiveData.postValue(destinationEntry);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error if needed
            }
        });
    }
}