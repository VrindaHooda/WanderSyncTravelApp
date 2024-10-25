package com.example.sprintproject.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sprintproject.model.DestinationDatabase;
import com.example.sprintproject.model.DestinationEntry;
import com.example.sprintproject.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class DestinationViewModel extends ViewModel {

    private MutableLiveData<DestinationEntry> destinationEntryLiveData;
    private DestinationDatabase databaseHelper;

    public DestinationViewModel() {
        destinationEntryLiveData = new MutableLiveData<>();
        databaseHelper = DestinationDatabase.getInstance();
    }

    public LiveData<DestinationEntry> getDestinationEntryLiveData() {
        return destinationEntryLiveData;
    }

    public void writeDestinationEntryData(String userId, String destinationEntryId, DestinationEntry destinationEntry) {
        // Save destination details for the user
        databaseHelper.writeData("users/" + userId + "/destinations/" + destinationEntryId, destinationEntry);
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

    public void calculateTotalVacationDays(String userId, OnVacationDaysCalculatedListener listener) {
        databaseHelper.readData("users/" + userId + "/destinations").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int totalDays = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DestinationEntry entry = snapshot.getValue(DestinationEntry.class);
                    if (entry != null && entry.getDuration() != null) {
                        totalDays += Integer.parseInt(entry.getDuration());
                    }
                }
                listener.onTotalVacationDaysCalculated(totalDays);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError(databaseError.getMessage());
            }
        });
    }

    public interface OnVacationDaysCalculatedListener {
        void onTotalVacationDaysCalculated(int totalDays);
        void onError(String error);
    }
}
