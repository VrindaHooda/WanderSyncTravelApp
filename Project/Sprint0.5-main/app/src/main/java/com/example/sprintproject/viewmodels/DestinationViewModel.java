package com.example.sprintproject.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.example.sprintproject.model.DestinationDatabase;
import com.example.sprintproject.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class DestinationViewModel extends ViewModel {

    private MutableLiveData<User> userLiveData;
    private DestinationDatabase databaseHelper;

    public void DestinationViewModell() {
        userLiveData = new MutableLiveData<>();

        databaseHelper = DestinationDatabase.getInstance();
    }

    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public void writeUserData(String userId, User user) {
        databaseHelper.writeData("users/" + userId, user);
    }

    public void readUserData(String userId) {
        databaseHelper.readData("users/" + userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                userLiveData.postValue(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error if needed
            }
        });
    }
}