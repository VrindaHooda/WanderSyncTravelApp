package com.example.sprintproject.views;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.sprintproject.R;
import com.example.sprintproject.model.DestinationEntry;
import com.example.sprintproject.viewmodels.ValidateViewModel;
import com.example.sprintproject.viewmodels.DestinationViewModel;

import java.util.Calendar;
import java.util.List;

public class DestinationActivity extends AppCompatActivity {

    private DestinationViewModel destinationViewModel;
    private ValidateViewModel validateViewModel;
    private TextView destinationListTextView;

    private Calendar startDate;
    private Calendar endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.destination_screen);

        // Fragment transaction for bottom navigation
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.bottomNavigation, NavigationFragment.class, null)
                    .commit();
        }

        try {
            destinationViewModel = new ViewModelProvider(this).get(DestinationViewModel.class);
            validateViewModel = new ViewModelProvider(this).get(ValidateViewModel.class);

            destinationListTextView = findViewById(R.id.destinationListTextView);
            Button logTravelButton = findViewById(R.id.btn_log_travel);

            // Check if TextView and Button are null
            if (destinationListTextView == null) {
                Log.e("DestinationActivity", "destinationListTextView is null");
            }
            if (logTravelButton == null) {
                Log.e("DestinationActivity", "logTravelButton is null");
            }

            // Observer for destination entries
            destinationViewModel.getDestinationEntries().observe(this, entries -> {
                updateDestinationList(entries);
            });

            // Prepopulate and read entries from the database
            destinationViewModel.prepopulateDatabase();
            destinationViewModel.readEntries();

            // Set up button click listener
            logTravelButton.setOnClickListener(v -> openLogTravelDialog());

        } catch (Exception e) {
            Log.e("DestinationActivity", "Error in onCreate", e);
        }
    }


    private void updateDestinationList(List<DestinationEntry> entries) {
        StringBuilder listBuilder = new StringBuilder();
        for (DestinationEntry entry : entries) {
            long duration = (entry.getEndDate().getTime() - entry.getStartDate().getTime()) / (1000 * 60 * 60 * 24);
            listBuilder.append(entry.getLocation()).append(": ").append(duration).append(" days\n");
        }
        destinationListTextView.setText(listBuilder.toString());
    }

    private void openLogTravelDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.log_travel_form, null);
        builder.setView(dialogView);

        EditText locationInput = dialogView.findViewById(R.id.locationInput);
        Button openStartDatePicker = dialogView.findViewById(R.id.openStartDatePicker);
        Button openEndDatePicker = dialogView.findViewById(R.id.openEndDatePicker);
        TextView startDateText = dialogView.findViewById(R.id.startDateText);
        TextView endDateText = dialogView.findViewById(R.id.endDateText);
        Button submitTravelLogButton = dialogView.findViewById(R.id.submitTravelLogButton);

        AlertDialog dialog = builder.create();

        // Initialize Calendar instances for start and end dates
        startDate = Calendar.getInstance();
        endDate = Calendar.getInstance();

        // Set up click listener for the start date picker button
        openStartDatePicker.setOnClickListener(v -> openDatePickerDialog(startDate, startDateText));

        // Set up click listener for the end date picker button
        openEndDatePicker.setOnClickListener(v -> openDatePickerDialog(endDate, endDateText));

        submitTravelLogButton.setOnClickListener(v -> {
            String location = locationInput.getText().toString();

            // Validate the dates
            if (!validateViewModel.validateDate(startDate.getTime(), endDate.getTime())) {
                Toast.makeText(this, "End date must be after start date", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create and add new destination entry
            DestinationEntry newEntry = new DestinationEntry(
                    destinationViewModel.generateDestinationId(location, startDate.getTime()),
                    location,
                    startDate.getTime(),
                    endDate.getTime()
            );

            destinationViewModel.addDestination(newEntry);
            Toast.makeText(this, "Travel log added", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }

    // Method to open a DatePickerDialog and set the selected date in the TextView
    private void openDatePickerDialog(Calendar date, TextView dateTextView) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Set the selected date in the Calendar object and update the TextView
                    date.set(selectedYear, selectedMonth, selectedDay);
                    dateTextView.setText("Selected Date: " + selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
                }, year, month, day);
        datePickerDialog.show();
    }
}
