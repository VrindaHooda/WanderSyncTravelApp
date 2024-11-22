package com.example.sprintproject;

import com.example.sprintproject.model.ContributorEntry;
import com.example.sprintproject.model.Destination;
import com.example.sprintproject.model.User;
import org.junit.Test;
import static org.junit.Assert.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.example.sprintproject.model.Accommodation;
import com.example.sprintproject.model.DiningReservation;

public class SimpleTests {

    /**
     * Tests the initialization of a {@link ContributorEntry} object.
     */
    @Test
    public void testContributorEntryInit() {
        ContributorEntry entry = new ContributorEntry("user1", "Notes for Paris");
        assertEquals("user1", entry.getUserId());
        assertEquals("Notes for Paris", entry.getNotes()); // Using existing `getNotes()` method
    }

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
        assertEquals(endDate, entry.getEndDate());
    }


    /**
     * Tests initializing a {@link DurationEntry} object.
     */
    @Test
    public void testDurationEntryInit() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse("2023-12-01");
        Date endDate = sdf.parse("2023-12-10");

        DurationEntry entry = new DurationEntry("vacation1", 9, startDate, endDate);
        assertEquals("vacation1", entry.getVacationId());
        assertEquals(startDate, entry.getStartDate());
        assertEquals(endDate, entry.getEndDate());
    }

    /**
     * Tests the initialization of a {@link TravelLogEntry} object.
     */
    @Test
    public void testTravelLogEntryInit() {
        TravelLogEntry entry = new TravelLogEntry("Paris", "2023-12-01", "2023-12-10");
        assertEquals("Paris", entry.getLocation());
        assertEquals("2023-12-01", entry.getStartDate());
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
     * Tests the initialization of a {@link UserEntry} object with a {@link DurationEntry}.
     */
    @Test
    public void testUserEntryInit() {
        DurationEntry entry = new DurationEntry("vacationId1", 5, new Date(), new Date());
        UserEntry userEntry = new UserEntry("userId1", "test@example.com", entry);
        assertEquals("userId1", userEntry.getUserId());
        assertEquals("test@example.com", userEntry.getEmail());
    }

    /**
     * Tests setting and updating the notes field of a {@link ContributorEntry}.
     */
    @Test
    public void testContributorEntryNotes() {
        ContributorEntry entry = new ContributorEntry("user2", "Visited Paris");
        entry.setNotes("Updated notes for Paris");
        assertEquals("Updated notes for Paris", entry.getNotes());
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
     * Tests the calculation of the duration in days for a {@link DurationEntry}.
     */
    @Test
    public void testDurationEntryCalculation() {
        DurationEntry entry = new DurationEntry("vacation1", "2023-12-01", "2023-12-10");
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
     * Tests updating the start and end dates of a {@link TravelLogEntry}.
     */
    @Test
    public void testTravelLogEntryDates() {
        TravelLogEntry entry = new TravelLogEntry("Rome", "2023-06-01", "2023-06-15");
        entry.setStartDate("2023-06-05");
        entry.setEndDate("2023-06-20");
        assertEquals("2023-06-05", entry.getStartDate());
        assertEquals("2023-06-20", entry.getEndDate());
    }

    /**
     * Tests replacing an old {@link DurationEntry} with a new one in a {@link UserEntry}.
     */
    @Test
    public void testUserEntryNewDurationEntry() {
        DurationEntry oldEntry = new DurationEntry("oldVacation", 7, new Date(), new Date());
        DurationEntry newEntry = new DurationEntry("newVacation", 14, new Date(), new Date());

        UserEntry userEntry = new UserEntry("userId123", "user@example.com", oldEntry);
        userEntry.setEntry(newEntry);
        assertEquals("newVacation", userEntry.getEntry().getVacationId());
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
        DiningReservation reservation = new DiningReservation("Fancy Restaurant",
                "2024-12-15", "7:00 PM", 4, "www.restaurant.com");
        assertEquals("Fancy Restaurant", reservation.getLocation());
    }

    /**
     * Tests retrieving the date field from a {@link DiningReservation} object.
     */
    @Test
    public void testDiningReservationDate() {
        DiningReservation reservation = new DiningReservation("Cozy Cafe",
                "2024-11-10", "6:00 PM", 2, "www.cafe.com");
        assertEquals("2024-11-10", reservation.getDate());
    }

    /**
     * Tests retrieving the time field from a {@link DiningReservation} object.
     */
    @Test
    public void testDiningReservationTime() {
        DiningReservation reservation = new DiningReservation("Gourmet Bistro",
                "2024-11-20", "8:30 PM", 2, "www.bistro.com");
        assertEquals("8:30 PM", reservation.getTime());
    }

    /**
     * Tests retrieving the number of people field from a {@link DiningReservation} object.
     */
    @Test
    public void testDiningReservationNumberOfPeople() {
        DiningReservation reservation = new DiningReservation("Sushi Place",
                "2024-11-18", "7:00 PM", 3, "www.sushiplace.com");
        assertEquals(3, reservation.getNumPeople());
    }

    /**
     * Tests retrieving the website field from a {@link DiningReservation} object.
     */
    @Test
    public void testDiningReservationWebsite() {
        DiningReservation reservation = new DiningReservation("Burger Joint",
                "2024-12-01", "5:00 PM", 5, "www.burgerjoint.com");
        assertEquals("www.burgerjoint.com", reservation.getWebsite());
    }

    /**
     * Tests updating the location field of a {@link DiningReservation} object.
     */
    @Test
    public void testDiningReservationUpdateLocation() {
        DiningReservation reservation = new DiningReservation("Old Restaurant",
                "2024-12-01", "6:00 PM", 2, "www.oldrestaurant.com");
        reservation.setLocation("New Restaurant");
        assertEquals("New Restaurant", reservation.getLocation());
    }

    /**
     * Tests updating the number of people field of a {@link DiningReservation} object.
     */
    @Test
    public void testDiningReservationUpdateNumPeople() {
        DiningReservation reservation = new DiningReservation("Steakhouse",
                "2024-11-25", "7:30 PM", 2, "www.steakhouse.com");
        reservation.setNumPeople(4);
        assertEquals(4, reservation.getNumPeople());
    }
}
