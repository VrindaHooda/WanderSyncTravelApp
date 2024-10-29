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

}
