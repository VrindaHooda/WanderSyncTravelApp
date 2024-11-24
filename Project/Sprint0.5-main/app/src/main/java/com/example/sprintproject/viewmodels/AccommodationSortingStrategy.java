package com.example.sprintproject.viewmodels;

import java.util.List;
import com.example.sprintproject.model.Accommodation;

/**
 * The {@code AccommodationSortingStrategy} interface defines a strategy for sorting
 * a list of {@link Accommodation} objects. This allows for different sorting criteria
 * to be implemented by classes that implement this interface.
 */
public interface AccommodationSortingStrategy {

    /**
     * Sorts the given list of accommodations based on a specific criterion.
     *
     * @param accommodations the list of {@link Accommodation} objects to be sorted
     */
    void sort(List<Accommodation> accommodations);
}
