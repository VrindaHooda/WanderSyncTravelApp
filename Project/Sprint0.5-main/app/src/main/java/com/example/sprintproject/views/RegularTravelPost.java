package com.example.sprintproject.views;

import com.example.sprintproject.viewmodels.TravelPost;

import java.util.Map;

public class RegularTravelPost implements TravelPost {
    private final Map<String, Object> postDetails;

    /**
     * Constructs a new {@code RegularTravelPost} instance with the specified post details.
     *
     * @param postDetails a map containing the details of the travel post,
     *                    such as destination, duration, and other related data
     */
    public RegularTravelPost(Map<String, Object> postDetails) {
        this.postDetails = postDetails;
    }

    @Override
    public String getDetails() {
        return "User Email: " + postDetails.get("userEmail") + "\n"
                + "Destination: " + postDetails.get("destination") + "\n"
                + "Duration: " + postDetails.get("duration") + "\n"
                + "Notes: " + postDetails.get("notes");
    }

    @Override
    public boolean isBoosted() {
        return false;
    }
}
