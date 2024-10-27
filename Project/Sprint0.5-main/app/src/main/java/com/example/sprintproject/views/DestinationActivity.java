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
import com.example.sprintproject.viewmodels.DestinationViewModel;

import java.util.Calendar;
import java.util.List;

public class DestinationActivity extends AppCompatActivity {

    private DestinationViewModel destinationViewModel;
    private TextView destinationListTextView;
    private Calendar startDate;
    private Calendar endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.destination_screen);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.bottomNavigation, NavigationFragment.class, null)
                    .commit();
        }

        // Set up ViewModel
        destinationViewModel = new ViewModelProvider(this).get(DestinationViewModel.class);

        // Initialize UI elements
        destinationListTextView = findViewById(R.id.destinationListTextView);
        Button logTravelButton = findViewById(R.id.btn_log_travel);
        Button calculateVacationTimeButton = findViewById(R.id.btn_calculate_vacation);

        // Set up button listeners
        logTravelButton.setOnClickListener(v -> openLogTravelDialog());
        calculateVacationTimeButton.setOnClickListener(v -> openCalculateVacationDialog());

        // Observe database entries to display on screen
        destinationViewModel.getDestinationEntries().observe(this, this::updateDestinationList);

        // Populate the database if empty
        destinationViewModel.prepopulateDatabase();
        destinationViewModel.readEntries();
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
        startDate = Calendar.getInstance();
        endDate = Calendar.getInstance();

        openStartDatePicker.setOnClickListener(v -> openDatePickerDialog(startDate, startDateText));
        openEndDatePicker.setOnClickListener(v -> openDatePickerDialog(endDate, endDateText));

        submitTravelLogButton.setOnClickListener(v -> {
            String location = locationInput.getText().toString();

            if (startDate == null || endDate == null || location.isEmpty()) {
                Toast.makeText(this, "Please complete all fields", Toast.LENGTH_SHORT).show();
                return;
            }

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

    private void openCalculateVacationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.vacation_time_form, null);
        builder.setView(dialogView);

        // Initialize UI components
        EditText vacationInput = dialogView.findViewById(R.id.vacationTimeInput);
        Button openStartDatePicker = dialogView.findViewById(R.id.openStartDatePicker2);
        Button openEndDatePicker = dialogView.findViewById(R.id.openEndDatePicker2);
        TextView startDateText = dialogView.findViewById(R.id.startDateText2);
        TextView endDateText = dialogView.findViewById(R.id.endDateText2);
        Button submitVacationTimeButton = dialogView.findViewById(R.id.submitVacationTimeButton);
        Button displayCalculatedTimeButton = dialogView.findViewById(R.id.display_calculated_time_button);

        AlertDialog dialog = builder.create();
        startDate = Calendar.getInstance();
        endDate = Calendar.getInstance();

        // Set up date pickers for start and end dates
        openStartDatePicker.setOnClickListener(v -> openDatePickerDialog(startDate, startDateText));
        openEndDatePicker.setOnClickListener(v -> openDatePickerDialog(endDate, endDateText));

        // Display Calculated Time Button Logic
        displayCalculatedTimeButton.setOnClickListener(v -> {
            String durationStr = vacationInput.getText().toString().trim();
            Long durationInDays = durationStr.isEmpty() ? null : Long.parseLong(durationStr);

            // Check which fields are set and calculate accordingly
            if (startDateText.getText().toString().contains("Selected Date") && endDateText.getText().toString().contains("Selected Date")) {
                // Both dates selected, calculate duration
                String calculatedResult = destinationViewModel.calculateMissingValue(startDate.getTime(), endDate.getTime(), null);
                vacationInput.setText(calculatedResult); // Auto-fill the duration
            } else if (startDateText.getText().toString().contains("Selected Date") && durationInDays != null) {
                // Start date and duration provided, calculate end date
                String calculatedEndDate = destinationViewModel.calculateMissingValue(startDate.getTime(), null, durationInDays);
                endDateText.setText("Calculated End Date: " + calculatedEndDate);
            } else if (endDateText.getText().toString().contains("Selected Date") && durationInDays != null) {
                // End date and duration provided, calculate start date
                String calculatedStartDate = destinationViewModel.calculateMissingValue(null, endDate.getTime(), durationInDays);
                startDateText.setText("Calculated Start Date: " + calculatedStartDate);
            } else {
                Toast.makeText(this, "Please provide enough information to calculate.", Toast.LENGTH_SHORT).show();
            }
        });

        // Submit Button Logic
        submitVacationTimeButton.setOnClickListener(v -> {
            String durationStr = vacationInput.getText().toString().trim();
            Long durationInDays = durationStr.isEmpty() ? null : Long.parseLong(durationStr);

            boolean isStartDateSet = startDateText.getText().toString().contains("Selected Date");
            boolean isEndDateSet = endDateText.getText().toString().contains("Selected Date");

            if (isStartDateSet && isEndDateSet && durationInDays == null) {
                // Calculate the missing duration
                String calculatedResult = destinationViewModel.calculateMissingValue(startDate.getTime(), endDate.getTime(), null);
                vacationInput.setText(calculatedResult + " days");
                Toast.makeText(this, "Calculated duration has been added.", Toast.LENGTH_SHORT).show();
            } else if (isStartDateSet && durationInDays != null && !isEndDateSet) {
                // Calculate the missing end date
                String calculatedEndDate = destinationViewModel.calculateMissingValue(startDate.getTime(), null, durationInDays);
                endDateText.setText("Calculated End Date: " + calculatedEndDate);
                Toast.makeText(this, "Calculated end date has been added.", Toast.LENGTH_SHORT).show();
            } else if (isEndDateSet && durationInDays != null && !isStartDateSet) {
                // Calculate the missing start date
                String calculatedStartDate = destinationViewModel.calculateMissingValue(null, endDate.getTime(), durationInDays);
                startDateText.setText("Calculated Start Date: " + calculatedStartDate);
                Toast.makeText(this, "Calculated start date has been added.", Toast.LENGTH_SHORT).show();
            } else if (isStartDateSet && isEndDateSet && durationInDays != null) {
                // If everything is complete, proceed with submission logic
                Toast.makeText(this, "All fields are complete", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Please ensure all required fields are filled.", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void openDatePickerDialog(Calendar date, TextView dateTextView) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    date.set(selectedYear, selectedMonth, selectedDay);
                    dateTextView.setText("Selected Date: " + selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
                }, year, month, day);
        datePickerDialog.show();
    }
}
