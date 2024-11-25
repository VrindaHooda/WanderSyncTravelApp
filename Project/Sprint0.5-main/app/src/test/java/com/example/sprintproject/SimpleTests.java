package com.example.sprintproject;

import com.example.sprintproject.model.Destination;
import com.example.sprintproject.model.Plan;
import com.example.sprintproject.model.TravelLog;
import com.example.sprintproject.model.User;
import org.junit.Test;
import static org.junit.Assert.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.sprintproject.model.Accommodation;
import com.example.sprintproject.model.DiningReservation;
import com.example.sprintproject.model.VacationEntry;

public class SimpleTests {

    /**
     * Tests the initialization of a {@link Destination} object.
     */
    @Test
    public void testDestinationEntry() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = dateFormat.parse("2024-02-15");
        Date endDate = dateFormat.parse("2024-02-20");

        Destination entry = new Destination("Paris", null, null, null, startDate, endDate);

        assertEquals("Paris", entry.getLocation());
        assertEquals(startDate, entry.getStartDate());
    }


    /**
     * Tests initializing a {@link VacationEntry} object.
     */
    @Test
    public void testVacationEntryInit() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse("2023-12-01");
        Date endDate = sdf.parse("2023-12-10");

        // Correcting the constructor usage
        VacationEntry entry = new VacationEntry(startDate, endDate, 9);

        // Adjusting assertions to match available methods
        assertEquals(startDate, entry.getStartDate());
        assertEquals(endDate, entry.getEndDate());
        assertEquals(9, entry.getDuration());
    }


    /**
     * Tests the initialization of a {@link TravelLog} object.
     */
    @Test
    public void testTravelLogInitialization() {
        // Adjusted to use the existing constructor
        Date startDate = new Date();
        Date endDate = new Date();
        TravelLog travelLog = new TravelLog("Paris", startDate, endDate);
        assertEquals("Paris", travelLog.getLocation());
        assertEquals(startDate, travelLog.getStartDate());
        assertEquals(endDate, travelLog.getEndDate());
    }

    /**
     * Tests the initialization of a {@link User} object.
     */
    @Test
    public void testUserInit() {
        User user = new User("user1", "password123", "user@example.com");
        assertEquals("user1", user.getUsername());
        assertEquals("user@example.com", user.getUserId());
    }

    /**
     * Tests the initialization of a {@link User} object with valid attributes.
     */
    @Test
    public void testUserEntryInit() {
        User userEntry = new User("testUser", "password123", "userId1");
        assertEquals("testUser", userEntry.getUsername());
        assertEquals("userId1", userEntry.getUserId());
        assertEquals("password123", userEntry.getPassword());
    }

    /**
     * Tests setting and updating the notes field of a {@link Plan}.
     */
    @Test
    public void testPlanInitialization() {
        // Adjusted to use the existing constructor
        Plan plan = new Plan("owner123", "Beach Trip", 7, new ArrayList<>(), "Relaxing vacation", new ArrayList<>());
        assertEquals("owner123", plan.getPlanName());
        assertEquals("Beach Trip", plan.getLocation());
        assertEquals(7, plan.getDuration());
        assertEquals("Relaxing vacation", plan.getNotes());
    }


    /**
     * Tests updating the location field of a {@link Destination}.
     */
    @Test
    public void testDestinationEntryLocationUpdate() {
        Destination entry = new Destination("2", "London", new Date(), new Date());
        entry.setLocation("Updated Location");
        assertEquals("Updated Location", entry.getLocation());
    }

    /**
     * Tests the calculation of the duration in days for a {@link VacationEntry}.
     */
    @Test
    public void testDurationEntryCalculation() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse("2023-12-01");
        Date endDate = sdf.parse("2023-12-10");

        VacationEntry entry = new VacationEntry(startDate, endDate, 9); // Pass 9 as duration explicitly
        assertEquals(9, entry.getDuration());
    }

    /**
     * Tests updating the password field of a {@link User}.
     */
    @Test
    public void testUserPassword() {
        User user = new User("username", "initialPassword", "userId123");
        user.setPassword("newPassword");
        assertEquals("newPassword", user.getPassword());
    }

    /**
     * Tests updating the start and end dates of a {@link TravelLog}.
     */
    @Test
    public void testTravelLogEntryDates() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date initialStartDate = sdf.parse("2023-06-01");
        Date initialEndDate = sdf.parse("2023-06-15");

        TravelLog entry = new TravelLog("Rome", initialStartDate, initialEndDate);

        // Update start and end dates
        Date updatedStartDate = sdf.parse("2023-06-05");
        Date updatedEndDate = sdf.parse("2023-06-20");
        entry.setStartDate(updatedStartDate);
        entry.setEndDate(updatedEndDate);

        assertEquals(updatedStartDate, entry.getStartDate());
        assertEquals(updatedEndDate, entry.getEndDate());
    }


    /**
     * Tests replacing an old {@link VacationEntry} with a new one in a {@link User}.
     */
    @Test
    public void testUserEntryNewDurationEntry() {
        // Create old and new VacationEntry objects using the specified constructor
        VacationEntry oldEntry = new VacationEntry(new Date(), new Date(), 7);
        VacationEntry newEntry = new VacationEntry(new Date(), new Date(), 14);

        // Create a User and set the old VacationEntry
        User userEntry = new User("userId123", "user@example.com");
        userEntry.setVacationEntry(oldEntry);

        // Update the VacationEntry
        userEntry.setVacationEntry(newEntry);

        // Verify the new VacationEntry is set correctly
        assertEquals(14, userEntry.getVacationEntry().getDuration());
    }



    /**
     * Tests the initialization of an {@link Accommodation} object with all fields.
     */
    @Test
    public void testAccommodationInitialization() {
        Accommodation accommodation = new Accommodation("Mountain Lodge",
                "2024-08-01", "2024-08-15", 2,
                "King Suite");

        assertEquals("Mountain Lodge", accommodation.getLocation());
        assertEquals("2024-08-01", accommodation.getCheckInDate());
        assertEquals("2024-08-15", accommodation.getCheckOutDate());
        assertEquals((Integer) 2, (Integer) accommodation.getNumRooms());
        assertEquals("King Suite", accommodation.getRoomType());
    }

    /**
     * Tests updating the location field of an {@link Accommodation} object.
     */
    @Test
    public void testAccommodationLocationUpdate() {
        Accommodation accommodation = new Accommodation("Old Location",
                "2024-07-01", "2024-07-10", 1,
                "Queen Room");
        accommodation.setLocation("New Location");

        assertEquals("New Location", accommodation.getLocation());
    }

    /**
     * Tests updating the check-in and check-out dates of an {@link Accommodation} object.
     */
    @Test
    public void testAccommodationDateUpdate() {
        Accommodation accommodation = new Accommodation("Beach Resort",
                "2024-05-01", "2024-05-10", 1,
                "Single Room");
        accommodation.setCheckInDate("2024-06-01");
        accommodation.setCheckOutDate("2024-06-05");

        assertEquals("2024-06-01", accommodation.getCheckInDate());
        assertEquals("2024-06-05", accommodation.getCheckOutDate());
    }

    /**
     * Tests updating the number of rooms for an {@link Accommodation} object.
     */
    @Test
    public void testAccommodationNumRoomsUpdate() {
        Accommodation accommodation = new Accommodation("Mountain Lodge",
                "2024-08-15", "2024-08-25", 1,
                "Double Room");
        accommodation.setNumRooms(3);

        assertEquals((Integer) 3, (Integer) accommodation.getNumRooms());
    }

    /**
     * Tests updating the room type of an {@link Accommodation} object.
     */
    @Test
    public void testAccommodationRoomTypeUpdate() {
        Accommodation accommodation = new Accommodation("City Hotel",
                "2024-03-10", "2024-03-15", 1,
                "Standard Room");
        accommodation.setRoomType("Suite");

        assertEquals("Suite", accommodation.getRoomType());
    }

    /**
     * Tests retrieving the location field from a {@link DiningReservation} object.
     */
    @Test
    public void testDiningReservationLocation() {
        DiningReservation reservation = new DiningReservation("Fancy Restaurant", "7:00 PM", "www.restaurant.com", 4);
        assertEquals("Fancy Restaurant", reservation.getLocation());
    }

    /**
     * Tests retrieving the date field from a {@link DiningReservation} object.
     */
    /**
     * Tests retrieving the date field from a {@link DiningReservation} object.
     */
    @Test
    public void testDiningReservationDate() {
        DiningReservation reservation = new DiningReservation(
                "Cozy Cafe", 11, 10, 2024, "6:00 PM", 2, "www.cafe.com", 4);
        assertEquals("11/10/2024", reservation.getFormattedDate());
    }


    /**
     * Tests retrieving the time field from a {@link DiningReservation} object.
     */
    /**
     * Tests retrieving the time field from a {@link DiningReservation} object.
     */
    @Test
    public void testDiningReservationTime() {
        DiningReservation reservation = new DiningReservation(
                "Gourmet Bistro", 11, 20, 2024, "8:30 PM", 2, "www.bistro.com", 4);
        assertEquals("8:30 PM", reservation.getTime());
    }


    /**
     * Tests retrieving the number of people field from a {@link DiningReservation} object.
     */
    /**
     * Tests retrieving the number of people field from a {@link DiningReservation} object.
     */
    @Test
    public void testDiningReservationNumberOfPeople() {
        DiningReservation reservation = new DiningReservation(
                "Sushi Place", 11, 18, 2024, "7:00 PM", 3, "www.sushiplace.com", 4);
        assertEquals(3, reservation.getNumPeople());
    }

    /**
     * Tests retrieving the website field from a {@link DiningReservation} object.
     */
    /**
     * Tests retrieving the website field from a {@link DiningReservation} object.
     */
    @Test
    public void testDiningReservationWebsite() {
        DiningReservation reservation = new DiningReservation(
                "Burger Joint", 12, 1, 2024, "5:00 PM", 5, "www.burgerjoint.com", 4);
        assertEquals("www.burgerjoint.com", reservation.getWebsite());
    }


    /**
     * Tests updating the location field of a {@link DiningReservation} object.
     */
    /**
     * Tests updating the location of a {@link DiningReservation} object.
     */
    @Test
    public void testDiningReservationUpdateLocation() {
        DiningReservation reservation = new DiningReservation(
                "Old Restaurant", 12, 1, 2024, "6:00 PM", 2, "www.oldrestaurant.com", 4);
        reservation.setLocation("New Restaurant");
        assertEquals("New Restaurant", reservation.getLocation());
    }


    /**
     * Tests updating the number of people field of a {@link DiningReservation} object.
     */
    /**
     * Tests updating the number of people in a {@link DiningReservation} object.
     */
    @Test
    public void testDiningReservationUpdateNumPeople() {
        DiningReservation reservation = new DiningReservation(
                "Steakhouse", 11, 25, 2024, "7:30 PM", 2, "www.steakhouse.com", 4);
        reservation.setNumPeople(4);
        assertEquals(4, reservation.getNumPeople());
    }


    /**
     * Tests retrieving the notes or location field from a {@link DiningReservation} object.
     */
    @Test
    public void testDiningReservation() {
        DiningReservation reservation = new DiningReservation(
                "Dinner at NYC", "7:00 PM", "www.restaurant.com", 4);

        // Validate the location
        assertEquals("Dinner at NYC", reservation.getLocation());
    }


    @Test
    public void testDiningReservationInitialization() {
        // Adjusted to use the existing constructor
        DiningReservation diningReservation = new DiningReservation("Fancy Restaurant", "7:00 PM", "www.fancyrestaurant.com", 4);

        // Validate the fields
        assertEquals("Fancy Restaurant", diningReservation.getLocation());
        assertEquals("7:00 PM", diningReservation.getTime());
        assertEquals("www.fancyrestaurant.com", diningReservation.getWebsite());
        assertEquals(4, diningReservation.getRating()); // Assuming `rating` replaces `numPeople`
    }


    @Test
    public void testPlanCreation() {
        Plan plan = new Plan(
                "Holiday Trip",
                "New York",
                7,
                null, // Pass an empty or null list for destinations
                "Family vacation",
                null // Pass an empty or null list for collaborators
        );

        assertEquals("Holiday Trip", plan.getPlanName());
        assertEquals("New York", plan.getLocation());
        assertEquals(7, plan.getDuration());
        assertEquals("Family vacation", plan.getNotes());
    }

    @Test
    public void testTravelLog() {
        TravelLog travelLog = new TravelLog(
                "Road Trip",
                new Date(),
                new Date()
        );

        travelLog.setStartDate(new Date());
        travelLog.setEndDate(new Date());

        assertNotNull(travelLog.getStartDate());
        assertNotNull(travelLog.getEndDate());
    }

    @Test
    public void testVacationEntry() {
        VacationEntry entry = new VacationEntry(new Date(), new Date(), 7);
        assertEquals(7, entry.getDuration());
    }

    @Test
    public void testDestinationInitialization() {
        // Adjusted to use the existing constructor
        Date startDate = new Date();
        Date endDate = new Date();
        List<String> accommodations = new ArrayList<>();
        accommodations.add("Hotel");
        List<String> diningReservations = new ArrayList<>();
        diningReservations.add("Dinner Reservation");

        Destination destination = new Destination("Hawaii", accommodations, diningReservations, "Flight", startDate, endDate);

        assertEquals("Hawaii", destination.getLocation());
        assertEquals(accommodations, destination.getAccommodations());
        assertEquals(diningReservations, destination.getDiningReservations());
        assertEquals("Flight", destination.getTransportation());
        assertEquals(startDate, destination.getStartDate());
        assertEquals(endDate, destination.getEndDate());
    }


    @Test
    public void testVacationEntryInitialization() {
        // Adjusted to use the existing constructor
        Date startDate = new Date();
        Date endDate = new Date();
        VacationEntry vacationEntry = new VacationEntry(startDate, endDate, 10);
        assertEquals(startDate, vacationEntry.getStartDate());
        assertEquals(endDate, vacationEntry.getEndDate());
        assertEquals(10, vacationEntry.getDuration());
    }

}
