package com.example.sprintproject.viewmodels;

import com.example.sprintproject.model.Accommodation;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public interface AccommodationSortingStrategy {
    void sort(List<Accommodation> accommodations);
}

class SortByCheckInDate implements AccommodationSortingStrategy {
    @Override
    public void sort(List<Accommodation> accommodations) {
        Collections.sort(accommodations, Comparator.comparing(Accommodation::getCheckInDate));
    }
}

class SortByRoomType implements AccommodationSortingStrategy {
    @Override
    public void sort(List<Accommodation> accommodations) {
        Collections.sort(accommodations, Comparator.comparing(Accommodation::getRoomType));
    }
}
