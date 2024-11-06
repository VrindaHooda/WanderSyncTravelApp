package com.example.sprintproject.model;

public class Accommodation {
    private String id;
    private String location;
    private String checkInDate;
    private String checkOutDate;
    private Integer numRooms;
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
    public Integer getNumRooms() {
        return numRooms;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setNumRooms(int numRooms) {
        this.numRooms = numRooms;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

}

