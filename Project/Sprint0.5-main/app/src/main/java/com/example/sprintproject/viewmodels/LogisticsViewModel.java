package com.example.sprintproject.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sprintproject.model.ContributorEntryList;
import com.example.sprintproject.model.UserDurationDatabase;

public class LogisticsViewModel extends ViewModel {
    private UserDurationDatabase userDurationDatabase;
    private MutableLiveData<ContributorEntryList> contributorEntryListLiveData
            = new MutableLiveData<>();
    public MutableLiveData<String> noteText = new MutableLiveData<>("");

    public LogisticsViewModel() {
        this.userDurationDatabase = UserDurationDatabase.getInstance();
    }

    public LiveData<ContributorEntryList> getContributorEntriesList() {
        return contributorEntryListLiveData;
    }

    public void readContributorsEntriesList(String userId) {
        userDurationDatabase.getContributorsList(userId, new UserDurationDatabase.DataStatus2() {
            @Override
            public void DataIsLoaded(String userId, ContributorEntryList contributorEntryList) {
                contributorEntryListLiveData.setValue(contributorEntryList);
            }

            @Override
            public void DataNotFound(String userId) {
                Log.w("LogisticsViewModel", "Failed to load entries" + userId);
                contributorEntryListLiveData.setValue(null);
            }

        });

    }


 }
