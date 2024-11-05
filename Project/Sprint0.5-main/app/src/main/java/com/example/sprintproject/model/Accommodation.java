package com.example.sprintproject.model;

public class Accommodation {
    private String id;
    private String location;
    private String checkInDate;
    private String checkOutDate;
    private String numRooms;
    private String roomType;

    public Accommodation() {
        // Default constructor required for calls to DataSnapshot.getValue(Accommodation.class)
    }

    // Constructor to initialize all fields
    public Accommodation(String location, String checkInDate, String checkOutDate, int numRooms,
                         String roomType) {
        this.location = location;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numRooms = String.valueOf(numRooms);
        this.roomType = roomType;
    }


    public Accommodation(String id, String location, String checkInDate, String checkOutDate,
                         String numRooms, String roomType) {
        this.id = id;
        this.location = location;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numRooms = numRooms;
        this.roomType = roomType;
    }

    public String getId() {
        return id;
    }
    public String getLocation() {
        return location;
    }
    public String getCheckInDate() {
        return checkInDate;
    }
    public String getCheckOutDate() {
        return checkOutDate;
    }
    public String getNumRooms() {
        return numRooms;
    }
    public String getRoomType() {
        return roomType;
    }
}

