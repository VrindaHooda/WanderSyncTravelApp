package com.example.sprintproject.viewmodels;

import java.util.Map;

public interface TravelPostObserver {
    void onTravelPostCreated(Map<String, Object> newPost);
}
