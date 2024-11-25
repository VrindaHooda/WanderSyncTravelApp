package com.example.sprintproject.views;
import com.example.sprintproject.viewmodels.TravelPost;

public class BoostedTravelPost implements TravelPost {
    private final TravelPost decoratedPost;

    /**
     * Constructs a {@code BoostedTravelPost} with the specified {@link TravelPost}.
     *
     * @param decoratedPost the original travel post to be boosted
     */
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
