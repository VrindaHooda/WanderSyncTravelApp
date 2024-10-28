package com.example.sprintproject.views;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sprintproject.R;
import com.example.sprintproject.model.TravelLogEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class LogTravelForm extends AppCompatActivity {

    private TextView startDateText;
    private TextView endDateText;
    private List<TravelLogEntry> travelLogs; // List to store travel logs

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_travel_form);

        // Initialize UI components
        Button openStartDatePicker = findViewById(R.id.openStartDatePicker);
        Button openEndDatePicker = findViewById(R.id.openEndDatePicker);
        startDateText = findViewById(R.id.startDateText);
        endDateText = findViewById(R.id.endDateText);

        // Initialize the travelLogs list
        travelLogs = new ArrayList<>();

        // Set up click listeners to open date pickers
        openStartDatePicker.setOnClickListener(v -> openDatePicker(startDateText));
        openEndDatePicker.setOnClickListener(v -> openDatePicker(endDateText));

        // Optional: Add a save button to handle travel log saving logic
        Button saveLogButton = findViewById(R.id.submitTravelLogButton);
        saveLogButton.setOnClickListener(v -> saveTravelLog());
    }

    // Method to open the DatePickerDialog
    private void openDatePicker(TextView dateTextView) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create and show the DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(selectedYear, selectedMonth, selectedDay);
                    String formattedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate.getTime());
                    dateTextView.setText(formattedDate); // Set formatted date
                }, year, month, day);
        datePickerDialog.show();
    }

    // Method to save the travel log (placeholder for future implementation)
    private void saveTravelLog() {
        String startDate = startDateText.getText().toString();
        String endDate = endDateText.getText().toString();

        // Validate that both start date and end date are selected
        if (startDate.isEmpty() || endDate.isEmpty()) {
            Toast.makeText(this, "Please select both start date and end date", Toast.LENGTH_SHORT).show();
            return;
        }

        // Optional: Validate that the end date is after the start date
        if (startDate.compareTo(endDate) >= 0) {
            Toast.makeText(this, "End date must be after start date", Toast.LENGTH_SHORT).show();
            return;
        }

        // Implement the logic to save the travel log using your ViewModel or Repository here
        Toast.makeText(this, "Travel log saved successfully!", Toast.LENGTH_SHORT).show();
    }

}
