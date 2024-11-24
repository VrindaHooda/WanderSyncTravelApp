package com.example.sprintproject.viewmodels;

import java.util.Map;

public interface TravelPostObserver {

    /**
     * Called when a new travel post is created.
     *
     * @param newPost a {@link Map} containing the details of the newly created post,
     *                where the keys are property names and the values are the corresponding data
     */
    void onTravelPostCreated(Map<String, Object> newPost);
}
