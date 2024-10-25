package com.example.sprintproject.views;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.destination_screen); // Use the combined layout

        // Initialize ViewModels
        destinationViewModel = new ViewModelProvider(this).get(DestinationViewModel.class);
        validateViewModel = new ViewModelProvider(this).get(ValidateViewModel.class);

        // Find UI components
        destinationListTextView = findViewById(R.id.destinationListTextView);
        Button logTravelButton = findViewById(R.id.btn_log_travel);

        // Observe the LiveData from ViewModel
        destinationViewModel.getDestinationEntries().observe(this, entries -> {
            updateDestinationList(entries);
        });

        // Trigger reading entries from the database
        destinationViewModel.readEntries();

        // Set up button click listener
        logTravelButton.setOnClickListener(v -> openLogTravelDialog());
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

        // Find UI elements in the dialog
        EditText locationInput = dialogView.findViewById(R.id.locationInput);
        DatePicker startDatePicker = dialogView.findViewById(R.id.startDatePicker);
        DatePicker endDatePicker = dialogView.findViewById(R.id.endDatePicker);
        Button submitTravelLogButton = dialogView.findViewById(R.id.submitTravelLogButton);

        AlertDialog dialog = builder.create();

        // Set up button click listener to submit the form
        submitTravelLogButton.setOnClickListener(v -> {
            String location = locationInput.getText().toString();

            // Get selected dates from DatePicker
            Calendar startDate = Calendar.getInstance();
            startDate.set(startDatePicker.getYear(), startDatePicker.getMonth(), startDatePicker.getDayOfMonth());

            Calendar endDate = Calendar.getInstance();
            endDate.set(endDatePicker.getYear(), endDatePicker.getMonth(), endDatePicker.getDayOfMonth());

            // Validate date input using ValidateViewModel
            if (!validateViewModel.validateDate(startDate.getTime(), endDate.getTime())) {
                Toast.makeText(this, "End date must be after start date", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create a new DestinationEntry object
            DestinationEntry newEntry = new DestinationEntry(
                    destinationViewModel.generateDestinationId(location, startDate.getTime()),
                    location,
                    startDate.getTime(),
                    endDate.getTime()
            );

            // Save entry using DestinationViewModel
            destinationViewModel.addDestination(newEntry);
            Toast.makeText(this, "Travel log added", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }
}
