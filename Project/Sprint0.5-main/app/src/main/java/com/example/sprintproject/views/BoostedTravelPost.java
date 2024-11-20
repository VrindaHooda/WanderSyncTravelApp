package com.example.sprintproject.views;
import com.example.sprintproject.viewmodels.TravelPost;

public class BoostedTravelPost implements TravelPost {
    private final TravelPost decoratedPost;

    public BoostedTravelPost(TravelPost decoratedPost) {
        this.decoratedPost = decoratedPost;
    }

    @Override
    public String getDetails() {
        return "[BOOSTED]\n" + decoratedPost.getDetails();
    }

    @Override
    public boolean isBoosted() {
        return true;
    }
}
