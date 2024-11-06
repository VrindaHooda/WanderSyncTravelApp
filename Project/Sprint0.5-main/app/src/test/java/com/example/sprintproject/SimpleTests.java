package com.example.sprintproject;

import com.example.sprintproject.model.AuthRepository;
import com.example.sprintproject.model.ContributorEntry;
import com.example.sprintproject.model.DestinationDatabase;
import com.example.sprintproject.model.DestinationEntry;
import com.example.sprintproject.model.DurationEntry;
import com.example.sprintproject.model.TravelLogEntry;
import com.example.sprintproject.model.User;
import com.example.sprintproject.model.UserDurationDatabase;
import com.example.sprintproject.model.UserEntry;

import org.junit.Test;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.sprintproject.model.Accommodation;


public class SimpleTests {

    @Test
    public void testContributorEntryInit() {
        ContributorEntry entry = new ContributorEntry("user1", "Notes for Paris");
        assertEquals("user1", entry.getUserId());
        assertEquals("Notes for Paris", entry.getNotes()); // Using existing `getNotes()` method
    }

    @Test
    public void testDestinationEntry() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = dateFormat.parse("2024-02-15");
        Date endDate = dateFormat.parse("2024-02-20");

        DestinationEntry entry = new DestinationEntry("1", "Paris", startDate, endDate);

        assertEquals("Paris", entry.getLocation());
        assertEquals(startDate, entry.getStartDate());
        assertEquals(endDate, entry.getEndDate());
    }

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

    @Test
    public void testTravelLogEntryInit() {
        TravelLogEntry entry = new TravelLogEntry("Paris", "2023-12-01", "2023-12-10");
        assertEquals("Paris", entry.getLocation());
        assertEquals("2023-12-01", entry.getStartDate());
    }

    @Test
    public void testUserInit() {
        User user = new User("user1", "password123", "user@example.com");
        assertEquals("user1", user.getUsername());
        assertEquals("user@example.com", user.getUserId());
    }

    @Test
    public void testUserEntryInit() {
        DurationEntry entry = new DurationEntry("vacationId1", 5, new Date(), new Date());
        UserEntry userEntry = new UserEntry("userId1", "test@example.com", entry);
        assertEquals("userId1", userEntry.getUserId());
        assertEquals("test@example.com", userEntry.getEmail());
    }

    // 1. Test ContributorEntry set and get methods for notes
    @Test
    public void testContributorEntryNotes() {
        ContributorEntry entry = new ContributorEntry("user2", "Visited Paris");
        entry.setNotes("Updated notes for Paris");
        assertEquals("Updated notes for Paris", entry.getNotes());
    }

    // 2. Test DestinationEntry location update
    @Test
    public void testDestinationEntryLocationUpdate() {
        DestinationEntry entry = new DestinationEntry("2", "London", new Date(), new Date());
        entry.setLocation("Updated Location");
        assertEquals("Updated Location", entry.getLocation());
    }

    // 3. Test DurationEntry duration calculation
    @Test
    public void testDurationEntryCalculation() {
        // Using the string-based constructor to simplify date input
        DurationEntry entry = new DurationEntry("vacation1", "2023-12-01", "2023-12-10");

        // Validate that the calculated duration is 9 days
        assertEquals(9, entry.getDuration());
    }


    // 4. Test User's password setting and retrieval
    @Test
    public void testUserPassword() {
        User user = new User("username", "initialPassword", "userId123");
        user.setPassword("newPassword");
        assertEquals("newPassword", user.getPassword());
    }

    // 5. Test TravelLogEntry dates setting and retrieval
    @Test
    public void testTravelLogEntryDates() {
        TravelLogEntry entry = new TravelLogEntry("Rome", "2023-06-01", "2023-06-15");
        entry.setStartDate("2023-06-05");
        entry.setEndDate("2023-06-20");
        assertEquals("2023-06-05", entry.getStartDate());
        assertEquals("2023-06-20", entry.getEndDate());
    }

    // 6. Test UserEntry with new DurationEntry set and retrieval
    @Test
    public void testUserEntryNewDurationEntry() {
        DurationEntry oldEntry = new DurationEntry("oldVacation", 7, new Date(), new Date());
        DurationEntry newEntry = new DurationEntry("newVacation", 14, new Date(), new Date());

        UserEntry userEntry = new UserEntry("userId123", "user@example.com", oldEntry);
        userEntry.setEntry(newEntry);
        assertEquals("newVacation", userEntry.getEntry().getVacationId());
    }

    // Test 1: Verify that Accommodation fields are correctly assigned on initialization
    @Test
    public void testAccommodationInitialization() {
        Accommodation accommodation = new Accommodation("Mountain Lodge", "2024-08-01", "2024-08-15", 2, "King Suite");

        assertEquals("Mountain Lodge", accommodation.getLocation());
        assertEquals("2024-08-01", accommodation.getCheckInDate());
        assertEquals("2024-08-15", accommodation.getCheckOutDate());
        assertEquals((Integer) 2, (Integer) accommodation.getNumRooms()); // Explicitly cast to Integer
        assertEquals("King Suite", accommodation.getRoomType());
    }

    // Test 2: Verify that location can be updated
    @Test
    public void testAccommodationLocationUpdate() {
        Accommodation accommodation = new Accommodation("Old Location", "2024-07-01", "2024-07-10", 1, "Queen Room");
        accommodation.setLocation("New Location");

        assertEquals("New Location", accommodation.getLocation());
    }

    // Test 3: Verify that check-in and check-out dates can be updated
    @Test
    public void testAccommodationDateUpdate() {
        Accommodation accommodation = new Accommodation("Beach Resort", "2024-05-01", "2024-05-10", 1, "Single Room");
        accommodation.setCheckInDate("2024-06-01");
        accommodation.setCheckOutDate("2024-06-05");

        assertEquals("2024-06-01", accommodation.getCheckInDate());
        assertEquals("2024-06-05", accommodation.getCheckOutDate());
    }

    // Test 4: Verify that number of rooms can be updated
    @Test
    public void testAccommodationNumRoomsUpdate() {
        Accommodation accommodation = new Accommodation("Mountain Lodge", "2024-08-15", "2024-08-25", 1, "Double Room");
        accommodation.setNumRooms(3);

        assertEquals((Integer) 3, (Integer) accommodation.getNumRooms()); // Explicitly cast to Integer
    }

    // Test 5: Verify that room type can be updated
    @Test
    public void testAccommodationRoomTypeUpdate() {
        Accommodation accommodation = new Accommodation("City Hotel", "2024-03-10", "2024-03-15", 1, "Standard Room");
        accommodation.setRoomType("Suite");

        assertEquals("Suite", accommodation.getRoomType());
    }

}