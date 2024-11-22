package com.example.sprintproject.model;

public class Accommodation {
    private String id;
    private String location;
    private String checkInDate;
    private String checkOutDate;
    private Integer numRooms;
    private String roomType;

    /**
     * Default constructor. Required for calls to DataSnapshot.getValue(Accommodation.class).
     */
    public Accommodation() {
        // Default constructor required for calls to DataSnapshot.getValue(Accommodation.class)
    }

    /**
     * Constructs an {@code Accommodation} with the specified details.
     *
     * @param location    the location of the accommodation
     * @param checkInDate the check-in date of the accommodation
     * @param checkOutDate the check-out date of the accommodation
     * @param numRooms    the number of rooms booked
     * @param roomType    the type of room booked
     */
    public Accommodation(String location, String checkInDate, String checkOutDate, int numRooms,
                         String roomType) {
        this.location = location;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numRooms = numRooms;
        this.roomType = roomType;
    }

    /**
     * Returns the unique identifier of the accommodation.
     *
     * @return the accommodation ID
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the location of the accommodation.
     *
     * @return the accommodation location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Returns the check-in date of the accommodation.
     *
     * @return the check-in date
     */
    public String getCheckInDate() {
        return checkInDate;
    }

    /**
     * Returns the check-out date of the accommodation.
     *
     * @return the check-out date
     */
    public String getCheckOutDate() {
        return checkOutDate;
    }

    /**
     * Returns the number of rooms booked for the accommodation.
     *
     * @return the number of rooms
     */
    public Integer getNumRooms() {
        return numRooms;
    }

    /**
     * Returns the type of room booked.
     *
     * @return the room type
     */
    public String getRoomType() {
        return roomType;
    }

    /**
     * Sets the check-in date of the accommodation.
     *
     * @param checkInDate the check-in date to set
     */
    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    /**
     * Sets the check-out date of the accommodation.
     *
     * @param checkOutDate the check-out date to set
     */
    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    /**
     * Sets the location of the accommodation.
     *
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Sets the number of rooms booked for the accommodation.
     *
     * @param numRooms the number of rooms to set
     */
    public void setNumRooms(int numRooms) {
        this.numRooms = numRooms;
    }

    /**
     * Sets the type of room booked.
     *
     * @param roomType the room type to set
     */
    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

}

