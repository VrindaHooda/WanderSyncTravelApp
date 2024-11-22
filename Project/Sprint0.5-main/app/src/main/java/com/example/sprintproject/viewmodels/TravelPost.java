package com.example.sprintproject.viewmodels;
public interface TravelPost {

    /**
     * Retrieves the details of the travel post.
     *
     * @return a {@link String} containing the details of the travel post
     */
    String getDetails();

    /**
     * Checks if the travel post is boosted.
     *
     * @return {@code true} if the post is boosted; {@code false} otherwise
     */
    boolean isBoosted();
}
