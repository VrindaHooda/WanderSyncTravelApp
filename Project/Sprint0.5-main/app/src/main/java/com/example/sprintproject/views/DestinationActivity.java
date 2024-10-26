package com.example.sprintproject.views;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.sprintproject.R;
import com.example.sprintproject.model.DestinationEntry;
import com.example.sprintproject.viewmodels.ValidateViewModel;
import com.example.sprintproject.viewmodels.DestinationViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DestinationActivity extends AppCompatActivity {

    private DestinationViewModel destinationViewModel;
    private ValidateViewModel validateViewModel;
    private EditText durationEditText;
    private Button startDateButton, endDateButton, calculateButton, calculateVacationButton;
    private TextView destinationListTextView, totalVacationDaysTextView;
    private LinearLayout vacationForm;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private Calendar calendar = Calendar.getInstance();
    private String startDate, endDate;
    private String userId = "user123";  // Example user ID; replace with actual logic
    private Calendar startCalendar, endCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.destination_screen);

        // Initialize ViewModels
        destinationViewModel = new ViewModelProvider(this).get(DestinationViewModel.class);
        validateViewModel = new ViewModelProvider(this).get(ValidateViewModel.class);

        // Initialize UI Elements
        durationEditText = findViewById(R.id.durationInput);
        startDateButton = findViewById(R.id.btn_start_date);
        endDateButton = findViewById(R.id.btn_end_date);
        calculateButton = findViewById(R.id.btn_calculate);
        calculateVacationButton = findViewById(R.id.btn_calculate_vacation);
        vacationForm = findViewById(R.id.vacation_form);
        totalVacationDaysTextView = findViewById(R.id.total_vacation_days);
        destinationListTextView = findViewById(R.id.destinationListTextView);

        // Show form when Calculate Vacation Time button is clicked
        calculateVacationButton.setOnClickListener(v -> vacationForm.setVisibility(View.VISIBLE));

        // Set up DatePicker for Start Date
        startDateButton.setOnClickListener(v -> openDatePickerDialog(calendar, date -> {
            startDate = dateFormat.format(date.getTime());
            startDateButton.setText(startDate);
        }));

        // Set up DatePicker for End Date
        endDateButton.setOnClickListener(v -> openDatePickerDialog(calendar, date -> {
            endDate = dateFormat.format(date.getTime());
            endDateButton.setText(endDate);
        }));

        // Set up button listener for calculating the vacation time
        calculateButton.setOnClickListener(v -> {
            String duration = durationEditText.getText().toString();
            if (isValidInput(duration, startDate, endDate)) {
                calculateMissingValue(duration, startDate, endDate);
                vacationForm.setVisibility(View.GONE); // Hide form after calculation

                // Save vacation data to user database
                DestinationEntry destinationEntry = new DestinationEntry(userId, startDate, endDate, duration);
                destinationViewModel.writeDestinationEntryData(userId, "uniqueDestinationId", destinationEntry);  // Replace with actual destination ID logic
                Toast.makeText(this, "Vacation details saved!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please fill in at least two fields", Toast.LENGTH_SHORT).show();
            }
        });

        // Display total vacation days on the screen
        destinationViewModel.calculateTotalVacationDays(userId, new DestinationViewModel.OnVacationDaysCalculatedListener() {
            @Override
            public void onTotalVacationDaysCalculated(int totalDays) {
                totalVacationDaysTextView.setText("Total Vacation Days: " + totalDays);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(DestinationActivity.this, "Error calculating total vacation days: " + error, Toast.LENGTH_SHORT).show();
            }
        });

        // Restore fragment for bottom navigation
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.bottomNavigation, NavigationFragment.class, null)
                    .commit();
        }

        // Prepopulate and read entries from the database
        destinationViewModel.prepopulateDatabase();
        destinationViewModel.readEntries();

        // Observer for destination entries
        destinationViewModel.getDestinationEntries().observe(this, entries -> updateDestinationList(entries));
    }

    private void updateDestinationList(List<DestinationEntry> entries) {
        StringBuilder listBuilder = new StringBuilder();
        for (DestinationEntry entry : entries) {
            long duration = (entry.getEndDate().getTime() - entry.getStartDate().getTime()) / (1000 * 60 * 60 * 24);
            listBuilder.append(entry.getLocation()).append(": ").append(duration).append(" days\n");
        }
        destinationListTextView.setText(listBuilder.toString());
    }

    private boolean isValidInput(String duration, String startDate, String endDate) {
        return (!duration.isEmpty() && startDate != null) || (startDate != null && endDate != null) || (!duration.isEmpty() && endDate != null);
    }

    private void calculateMissingValue(String duration, String startDate, String endDate) {
        try {
            if (duration.isEmpty()) {
                int calculatedDuration = calculateDuration(startDate, endDate);
                durationEditText.setText(String.valueOf(calculatedDuration));
            } else if (startDate.isEmpty()) {
                String calculatedStartDate = calculateStartDate(duration, endDate);
                startDateButton.setText(calculatedStartDate);
            } else if (endDate.isEmpty()) {
                String calculatedEndDate = calculateEndDate(duration, startDate);
                endDateButton.setText(calculatedEndDate);
            }
        } catch (ParseException e) {
            Toast.makeText(this, "Invalid date format. Please use MM/dd/yyyy.", Toast.LENGTH_SHORT).show();
        }
    }

    private int calculateDuration(String startDate, String endDate) throws ParseException {
        Date start = dateFormat.parse(startDate);
        Date end = dateFormat.parse(endDate);
        long difference = end.getTime() - start.getTime();
        return (int) (difference / (1000 * 60 * 60 * 24));
    }

    private String calculateStartDate(String duration, String endDate) throws ParseException {
        int days = Integer.parseInt(duration);
        Date end = dateFormat.parse(endDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(end);
        calendar.add(Calendar.DAY_OF_YEAR, -days);
        return dateFormat.format(calendar.getTime());
    }

    private String calculateEndDate(String duration, String startDate) throws ParseException {
        int days = Integer.parseInt(duration);
        Date start = dateFormat.parse(startDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        calendar.add(Calendar.DAY_OF_YEAR, days);
        return dateFormat.format(calendar.getTime());
    }

    private void openDatePickerDialog(Calendar calendar, DatePickerListener listener) {
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            listener.onDateSet(calendar);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    interface DatePickerListener {
        void onDateSet(Calendar date);
    }
}
