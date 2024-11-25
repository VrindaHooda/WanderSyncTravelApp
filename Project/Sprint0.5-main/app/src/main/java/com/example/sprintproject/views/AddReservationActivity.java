package com.example.sprintproject.views;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.sprintproject.R;
import com.example.sprintproject.model.DiningReservation;
import com.example.sprintproject.viewmodels.DiningViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddReservationActivity extends AppCompatActivity {

    private EditText restaurantNameEditText;
    private EditText websiteEditText;
    private EditText numberOfGuestsEditText;
    private TextView dateTextView;
    private TextView timeTextView;
    private RatingBar ratingBar;
    private Button saveReservationButton;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private DiningViewModel diningViewModel;
    private Calendar reservationCalendar;
    private Button exitButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reservation);

        firebaseAuth = FirebaseAuth.getInstance();
        // Initialize UI components
        restaurantNameEditText = findViewById(R.id.restaurantNameEditText);
        websiteEditText = findViewById(R.id.websiteEditText);
        numberOfGuestsEditText = findViewById(R.id.numberOfGuestsEditText);
        dateTextView = findViewById(R.id.dateTextView);
        timeTextView = findViewById(R.id.timeTextView);
        ratingBar = findViewById(R.id.ratingBar);
        saveReservationButton = findViewById(R.id.saveReservationButton);
        exitButton = findViewById(R.id.exitButton);
        progressBar = findViewById(R.id.progressBar);

        // Initialize ViewModel
        diningViewModel = new ViewModelProvider(this).get(DiningViewModel.class);

        // Set up Calendar for reservation date and time
        reservationCalendar = Calendar.getInstance();

        // Set up Date Picker
        dateTextView.setOnClickListener(v -> showDatePicker());

        // Set up Time Picker
        timeTextView.setOnClickListener(v -> showTimePicker());

        // Set up Save Reservation Button
        saveReservationButton.setOnClickListener(v -> saveReservation());

        // Set up Exit Button
        exitButton.setOnClickListener(v -> finish()); // Simply finishes the activity

        // Observe ViewModel for loading state
        diningViewModel.getIsLoading().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            saveReservationButton.setEnabled(!isLoading);
        });
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            reservationCalendar.set(Calendar.YEAR, year);
            reservationCalendar.set(Calendar.MONTH, month);
            reservationCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateLabel();
        }, reservationCalendar.get(Calendar.YEAR), reservationCalendar.get(Calendar.MONTH), reservationCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            reservationCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            reservationCalendar.set(Calendar.MINUTE, minute);
            updateTimeLabel();
        }, reservationCalendar.get(Calendar.HOUR_OF_DAY), reservationCalendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    private void updateDateLabel() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        dateTextView.setText(dateFormat.format(reservationCalendar.getTime()));
    }

    private void updateTimeLabel() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        timeTextView.setText(timeFormat.format(reservationCalendar.getTime()));
    }

    private void saveReservation() {
        String restaurantName = restaurantNameEditText.getText().toString().trim();
        String website = websiteEditText.getText().toString().trim();
        String numberOfGuestsStr = numberOfGuestsEditText.getText().toString().trim();
        float rating = ratingBar.getRating();


        if (restaurantName.isEmpty()) {
            restaurantNameEditText.setError("Restaurant name is required");
            return;
        }

        if (website.isEmpty()) {
            websiteEditText.setError("Website is required");
            return;
        }

        if (numberOfGuestsStr.isEmpty()) {
            numberOfGuestsEditText.setError("Number of guests is required");
            return;
        }

        int numberOfGuests;
        try {
            numberOfGuests = Integer.parseInt(numberOfGuestsStr);
        } catch (NumberFormatException e) {
            numberOfGuestsEditText.setError("Please enter a valid number");
            return;
        }

        // Create a new DiningReservation object
        Date reservationDate = reservationCalendar.getTime();
        String reservationId = null; // Generate dynamically

        DiningReservation reservation = new DiningReservation(reservationId, getUserId(), restaurantName, reservationDate, numberOfGuests, "", website, rating);

        // Save the reservation using ViewModel
        diningViewModel.addDiningReservation(getUserId(), reservation);

        // Show a toast message to confirm save
        Toast.makeText(this, "Reservation added successfully", Toast.LENGTH_SHORT).show();

        // Close the activity after saving
        finish();
    }

    private String getUserId() {
        // Replace with your method to retrieve the current user's ID, such as from Firebase Authentication
        return firebaseAuth.getCurrentUser().getUid();
    }
}
