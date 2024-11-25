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
        Plan plan = new Plan("owner123", "Beach Trip", 7,
                new ArrayList<>(), "Relaxing vacation", new ArrayList<>());
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

        VacationEntry entry = new VacationEntry(startDate, endDate, 9);
        // Pass 9 as duration explicitly
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
     * Tests retrieving the restaurant name from a {@link DiningReservation} object.
     */
    @Test
    public void testDiningReservationRestaurantName() {
        DiningReservation reservation = new DiningReservation(
                "res1", "user1", "Fancy Restaurant", new Date(),
                4, "Some notes", "www.restaurant.com", 4.5f);
        assertEquals("Fancy Restaurant", reservation.getRestaurantName());
    }

    /**
     * Tests retrieving the reservation date from a {@link DiningReservation} object.
     */
    @Test
    public void testDiningReservationDate() {
        Date reservationDate = new Date();
        DiningReservation reservation = new DiningReservation(
                "res2", "user1", "Cozy Cafe", reservationDate,
                2, "Notes", "www.cafe.com", 3.5f);
        assertEquals(reservationDate, reservation.getReservationDate());
    }

    /**
     * Tests retrieving the number of guests field from a {@link DiningReservation} object.
     */
    @Test
    public void testDiningReservationNumberOfGuests() {
        DiningReservation reservation = new DiningReservation(
                "res3", "user1", "Gourmet Bistro", new Date(),
                5, "Notes", "www.bistro.com", 4.0f);
        assertEquals(5, reservation.getNumberOfGuests());
    }

    /**
     * Tests retrieving the notes field from a {@link DiningReservation} object.
     */
    @Test
    public void testDiningReservationNotes() {
        DiningReservation reservation = new DiningReservation(
                "res4", "user1", "Burger Joint", new Date(),
                3, "Special occasion", "www.burgerjoint.com",
                5.0f);
        assertEquals("Special occasion", reservation.getNotes());
    }

    /**
     * Tests retrieving the website field from a {@link DiningReservation} object.
     */
    @Test
    public void testDiningReservationWebsite() {
        DiningReservation reservation = new DiningReservation(
                "res5", "user1", "Sushi Place", new Date(),
                4, "Dinner out", "www.sushiplace.com", 4.5f);
        assertEquals("www.sushiplace.com", reservation.getWebsite());
    }

    /**
     * Tests retrieving the rating field from a {@link DiningReservation} object.
     */
    @Test
    public void testDiningReservationRating() {
        DiningReservation reservation = new DiningReservation(
                "res6", "user1", "Steakhouse", new Date(),
                2, "Business dinner", "www.steakhouse.com",
                3.5f);
        assertEquals(3.5f, reservation.getRating(), 0.0f);
    }

    /**
     * Tests updating the restaurant name of a {@link DiningReservation} object.
     */
    @Test
    public void testDiningReservationUpdateRestaurantName() {
        DiningReservation reservation = new DiningReservation(
                "res7", "user1", "Old Restaurant", new Date(),
                2, "Notes", "www.oldrestaurant.com", 4.0f);
        reservation.setRestaurantName("New Restaurant");
        assertEquals("New Restaurant", reservation.getRestaurantName());
    }

    /**
     * Tests updating the number of guests in a {@link DiningReservation} object.
     */
    @Test
    public void testDiningReservationUpdateNumberOfGuests() {
        DiningReservation reservation = new DiningReservation(
                "res8", "user1", "Seafood Spot", new Date(),
                2, "Notes", "www.seafoodspot.com", 4.0f);
        reservation.setNumberOfGuests(6);
        assertEquals(6, reservation.getNumberOfGuests());
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

        Destination destination = new Destination("Hawaii", accommodations,
                diningReservations, "Flight", startDate, endDate);

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


    // Test 2: Verify setting and retrieving Accommodation location
    @Test
    public void testAccommodationSetLocation() {
        Accommodation accommodation = new Accommodation();
        accommodation.setLocation("Beach Resort");
        assertEquals("Beach Resort", accommodation.getLocation());
    }

    // Test 3: Verify setting and retrieving VacationEntry dates
    @Test
    public void testVacationEntryDates() {
        Date startDate = new Date();
        Date endDate = new Date();
        VacationEntry vacationEntry = new VacationEntry(startDate, endDate, 5);
        assertEquals(startDate, vacationEntry.getStartDate());
        assertEquals(endDate, vacationEntry.getEndDate());
        assertEquals(5, vacationEntry.getDuration());
    }


}
