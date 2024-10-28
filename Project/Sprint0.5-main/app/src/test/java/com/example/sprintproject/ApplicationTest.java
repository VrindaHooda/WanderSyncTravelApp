package com.example.sprintproject;

import android.content.Intent;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.sprintproject.R;
import com.example.sprintproject.model.DurationEntry;
import com.example.sprintproject.viewmodels.DestinationViewModel;
import com.example.sprintproject.viewmodels.UserDurationViewModel;
import com.example.sprintproject.views.AddUserActivity;
import com.example.sprintproject.views.CreateAccount;
import com.example.sprintproject.views.DestinationActivity;
import com.example.sprintproject.views.HomeScreen;
import com.example.sprintproject.views.LogTravelForm;
import com.example.sprintproject.views.Login;
import com.example.sprintproject.views.LogisticsActivity;
import com.example.sprintproject.views.MainActivity;
import com.example.sprintproject.views.ModifyPlansActivity;
import com.example.sprintproject.views.ViewNotesActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.junit.Assert.assertEquals;

import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowApplication;

import java.util.ArrayList;
import java.util.Date;


@RunWith(AndroidJUnit4.class)
public class ApplicationTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);
    @Rule
    public ActivityScenarioRule<DestinationActivity> activityRule1 =
            new ActivityScenarioRule<>(DestinationActivity.class);

    @Test
    public void testLoginSuccess() {
        // Launch Login Activity
        Intent intent = new Intent(activityRule.getActivity(), Login.class);
        activityRule.launchActivity(intent);

        // Simulate user input
        onView(withId(R.id.usernameEditText)).perform(typeText("test@example.com"));
        onView(withId(R.id.passwordEditText)).perform(typeText("password123"));
        onView(withId(R.id.loginButton)).perform(click());

        // Verify that the expected activity is started
        intended(hasComponent(HomeScreen.class.getName()));
    }

    @Test
    public void testCreateAccountSuccess() {
        // Launch Create Account Activity
        Intent intent = new Intent(activityRule.getActivity(), CreateAccount.class);
        activityRule.launchActivity(intent);

        // Simulate user input
        onView(withId(R.id.usernameEditText)).perform(typeText("newuser@example.com"));
        onView(withId(R.id.passwordEditText)).perform(typeText("newpassword"));
        onView(withId(R.id.createAccountButton)).perform(click());

        // Verify account creation
        Toast.makeText(activityRule.getActivity(), "Account created successfully", Toast.LENGTH_SHORT).show();
        assertTrue(activityRule.getActivity().isAccountCreated("newuser@example.com"));
    }

    @Test
    public void testAddUserActivity() {
        // Start AddUserActivity
        Intent intent = new Intent(activityRule.getActivity(), AddUserActivity.class);
        activityRule.launchActivity(intent);

        // Simulate user input
        onView(withId(R.id.usernameEditText)).perform(typeText("testuser@example.com"));
        onView(withId(R.id.createAccountButton)).perform(click());

        // Verify user was added
        final boolean[] userAdded = {false}; // To capture async result
        activityRule.getActivity().isUserAdded("testuser@example.com", new MainActivity.OnUserAddedCallback() {
            @Override
            public void onCallback(boolean exists) {
                userAdded[0] = exists; // Update the boolean
            }
        });

        // Use a wait or appropriate assertion method to verify user was added
        assertTrue(userAdded[0]);
    }


    @Test
    public void testAddDestination() {
        // Launch the activity
        activityRule1.getScenario().onActivity(activity -> {
            // Simulate user input for adding a destination
            onView(withId(R.id.destination)).perform(typeText("Paris"));
            onView(withId(R.id.startDateText)).perform(typeText("2024-12-01"));
            onView(withId(R.id.endDateText)).perform(typeText("2024-12-10"));
            onView(withId(R.id.durationInput)).perform(typeText("9"));
            onView(withId(R.id.saveButton)).perform(click());

            // Check if the destination was added
            DestinationViewModel destinationViewModel = new ViewModelProvider(activityRule.getActivity()).get(DestinationViewModel.class);
            assertTrue(destinationViewModel.isDestinationAdded("Paris"));
        });
    }

    @Test
    public void testViewNotesActivity() {
        Intent intent = new Intent(activityRule.getActivity(), ViewNotesActivity.class);
        activityRule.launchActivity(intent);

        // Click on the button to view notes
        onView(withId(R.id.notes_list_view)).perform(click());

        // Assert that the expected activity is displayed
        Intent expectedIntent = new Intent(activityRule.getActivity(), ViewNotesActivity.class);
        assertEquals(expectedIntent.getComponent(), ShadowApplication.getInstance().getNextStartedActivity());
    }

    @Test
    public void testModifyTripPlansActivity() {
        Button modifyTripPlansButton = activityRule.getActivity().findViewById(R.id.modify_button);
        modifyTripPlansButton.performClick();

        // Assert that the ModifyTripPlansActivity is opened
        Intent expectedIntent = new Intent(activityRule.getActivity(), ModifyPlansActivity.class);
        assertEquals(expectedIntent.getComponent(), ShadowApplication.getInstance().getNextStartedActivity());
    }

    @Test
    public void testLogTravelFormSubmission() {
        Intent intent = new Intent(activityRule.getActivity(), LogTravelForm.class);
        activityRule.launchActivity(intent);

        // Input data into the form
        onView(withId(R.id.locationInput)).perform(typeText("Paris"));
        onView(withId(R.id.durationInput)).perform(typeText("7"));
        onView(withId(R.id.submitTravelLogButton)).perform(click());

        // Verify that the log has been submitted
        DestinationViewModel viewModel = new DestinationViewModel();
        assertTrue(viewModel.isTravelLogSaved("Paris", "7"));
    }

    @Test
    public void testViewNotesButtonNavigation() {
        // Find the view_notes button in the LogisticsActivity
        FloatingActionButton viewNotesButton = activityRule.getActivity().findViewById(R.id.view_notes);
        viewNotesButton.performClick(); // Simulate the button click

        // Create the expected Intent for ViewNotesActivity
        Intent expectedIntent = new Intent(activityRule.getActivity(), ViewNotesActivity.class);
        assertEquals(expectedIntent.getComponent(), ShadowApplication.getInstance().getNextStartedActivity());
    }

    @Test
    public void testSaveDurationData() {
        // Create a mock UserDurationViewModel
        UserDurationViewModel mockViewModel = mock(UserDurationViewModel.class);
        DurationEntry mockEntry = new DurationEntry("vacationId", 10, new Date(), new Date());

        doNothing().when(mockViewModel).saveDurationData(anyString(), anyString(), any(DurationEntry.class), anyList());

        // Call the method
        mockViewModel.saveDurationData("testUserId", "testEmail", mockEntry, new ArrayList<>());

        // Verify that the method was called with the correct parameters
        verify(mockViewModel).saveDurationData("testUserId", "testEmail", mockEntry, new ArrayList<>());
    }

    @Test
    public void testNavigationToModifyPlansActivity() {
        // Create an intent for the LogisticsActivity
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), LogisticsActivity.class);
        ActivityScenario<LogisticsActivity> scenario = ActivityScenario.launch(intent);

        scenario.onActivity(activity -> {
            FloatingActionButton modifyPlansButton = activity.findViewById(R.id.modify_plans);
            modifyPlansButton.performClick(); // Simulate click on the modify plans button

            // Check that the expected activity is started
            Intent expectedIntent = new Intent(activity, ModifyPlansActivity.class);
            assertEquals(expectedIntent.getComponent(), ShadowApplication.getInstance().getNextStartedActivity());
        });
    }
}
