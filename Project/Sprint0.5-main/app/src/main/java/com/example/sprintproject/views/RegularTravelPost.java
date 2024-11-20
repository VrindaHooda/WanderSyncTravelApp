package com.example.sprintproject.views;
import com.example.sprintproject.viewmodels.TravelPost;
import java.util.Map;

public class RegularTravelPost implements TravelPost {
    private final Map<String, Object> postDetails;

    public RegularTravelPost(Map<String, Object> postDetails) {
        this.postDetails = postDetails;
    }

    @Override
    public String getDetails() {
        return "Destination: " + postDetails.get("destination") + "\n"
                + "Duration: " + postDetails.get("duration") + "\n"
                + "Notes: " + postDetails.get("notes");
    }

    @Override
    public boolean isBoosted() {
        return false;
    }
}
