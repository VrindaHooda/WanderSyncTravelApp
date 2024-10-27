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
    private TextView totalDaysTextView;

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
        try {
            destinationViewModel = new ViewModelProvider(this).get(DestinationViewModel.class);
            validateViewModel = new ViewModelProvider(this).get(ValidateViewModel.class);

            destinationListTextView = findViewById(R.id.destinationListTextView);
            totalDaysTextView = findViewById(R.id.totalDaysTextView); // Initialize the total days TextView
            Button logTravelButton = findViewById(R.id.btn_log_travel);
            Button calculateVacationTime = findViewById(R.id.btn_calculate_vacation);

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

            calculateVacationTime.setOnClickListener(v -> openCalculateVacationDialog());


        } catch (Exception e) {
            Log.e("DestinationActivity", "Error in onCreate", e);
        }
    }


    private void updateDestinationList(List<DestinationEntry> entries) {
        StringBuilder listBuilder = new StringBuilder();
        long totalDays = 0;
        for (DestinationEntry entry : entries) {
            long duration = (entry.getEndDate().getTime() - entry.getStartDate().getTime()) / (1000 * 60 * 60 * 24);
            listBuilder.append(entry.getLocation()).append(": ").append(duration).append(" days\n");
            totalDays += duration;
        }
        destinationListTextView.setText(listBuilder.toString());
        updateTotalDays(totalDays);
    }
    private void updateTotalDays(long totalDays) {
        TextView totalDaysTextView = findViewById(R.id.totalDaysTextView);
        totalDaysTextView.setText("Total Days: " + totalDays + " days");
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

    private void openCalculateVacationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.vacation_time_form, null);
        builder.setView(dialogView);

        EditText vacationInput = dialogView.findViewById(R.id.vacationTimeInput);
        Button openStartDatePicker2 = dialogView.findViewById(R.id.openStartDatePicker2);
        Button openEndDatePicker2 = dialogView.findViewById(R.id.openEndDatePicker2);
        TextView startDateText2 = dialogView.findViewById(R.id.startDateText2);
        TextView endDateText2 = dialogView.findViewById(R.id.endDateText2);
        Button submitVacationTimeButton = dialogView.findViewById(R.id.submitVacationTimeButton);

        AlertDialog dialog = builder.create();

        // Initialize Calendar instances for start and end dates
        startDate = Calendar.getInstance();
        endDate = Calendar.getInstance();

        // Set up click listener for the start date picker button
        openStartDatePicker2.setOnClickListener(v -> openDatePickerDialog(startDate, startDateText2));

        // Set up click listener for the end date picker button
        openEndDatePicker2.setOnClickListener(v -> openDatePickerDialog(endDate, endDateText2));

        submitVacationTimeButton.setOnClickListener(v -> {
            // Retrieve the inputted duration (if provided)
            String durationStr = vacationInput.getText().toString();
            Long durationInDays = durationStr.isEmpty() ? null : Long.parseLong(durationStr);

            // Variables to store the calculated result and the message for the result dialog
            String calculatedResult = null;
            String resultMessage = null;

            // Check for each scenario in order:
            if (!startDateText2.getText().toString().isEmpty() && durationInDays != null) {
                calculatedResult = destinationViewModel.calculateMissingValue(startDate.getTime(), null, durationInDays);
                resultMessage = "Calculated End Date: " + calculatedResult;
            } else if (!startDateText2.getText().toString().isEmpty() && !endDateText2.getText().toString().isEmpty()) {
                calculatedResult = destinationViewModel.calculateMissingValue(startDate.getTime(), endDate.getTime(), null);
                resultMessage = "Calculated Duration: " + calculatedResult + " days";
            } else if (!endDateText2.getText().toString().isEmpty() && durationInDays != null) {
                calculatedResult = destinationViewModel.calculateMissingValue(null, endDate.getTime(), durationInDays);
                resultMessage = "Calculated Start Date: " + calculatedResult;
            } else {
                Toast.makeText(this, "Please provide at least two inputs", Toast.LENGTH_SHORT).show();
                return;
            }

            // Show the calculated result in a separate dialog box
            AlertDialog.Builder resultDialogBuilder = new AlertDialog.Builder(this);
            resultDialogBuilder.setTitle("Calculation Result");
            resultDialogBuilder.setMessage(resultMessage);
            resultDialogBuilder.setPositiveButton("OK", (resultDialogButton, which) -> resultDialogButton.dismiss());

            AlertDialog resultDialog = resultDialogBuilder.create();
            resultDialog.show();

            // Clear input fields after showing the result dialog
            startDateText2.setText(""); // Clear start date text view
            endDateText2.setText("");   // Clear end date text view
            vacationInput.setText("");  // Clear duration input field

            // Reset Calendar objects to today's date
            startDate = Calendar.getInstance();
            endDate = Calendar.getInstance();
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
