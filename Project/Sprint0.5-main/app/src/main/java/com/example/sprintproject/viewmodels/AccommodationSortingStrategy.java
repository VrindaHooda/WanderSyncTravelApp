package com.example.sprintproject.viewmodels;

import java.util.List;
import com.example.sprintproject.model.Accommodation;

public interface AccommodationSortingStrategy {
    void sort(List<Accommodation> accommodations);
}
