package com.example.sprintproject.viewmodels;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.example.sprintproject.model.Accommodation;

public class SortByRoomType implements AccommodationSortingStrategy {
    @Override
    public void sort(List<Accommodation> accommodations) {
        Collections.sort(accommodations, Comparator.comparing(Accommodation::getRoomType));
    }
}
