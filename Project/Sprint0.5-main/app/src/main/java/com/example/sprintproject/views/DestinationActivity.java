package com.example.sprintproject.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.sprintproject.R;
import com.example.sprintproject.model.DestinationEntry;
import com.example.sprintproject.viewmodels.DestinationViewModel;
import com.example.sprintproject.databinding.ActivityDestinationScreenBinding;

import java.util.List;

public class DestinationActivity extends AppCompatActivity {

    private DestinationViewModel destinationViewModel;
    private ActivityDestinationScreenBinding binding; // Updated variable type
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize data binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_destination_screen);

        // Initialize ViewModel and bind it to the layout
        destinationViewModel = new ViewModelProvider(this).get(DestinationViewModel.class);
        binding.setDestinationViewModel(destinationViewModel);
        binding.setLifecycleOwner(this); // To observe LiveData

        // Retrieve user ID from the intent (if needed)
        userId = getIntent().getStringExtra("userId");

        // Prepopulate the database
        if (userId != null) {
            destinationViewModel.prepopulateDatabase(userId);
        }

        // Observe LiveData from ViewModel for destination entries
        destinationViewModel.getDestinationEntries().observe(this, this::updateDestinationList);

        // Fetch the last 5 destination entries
        if (userId != null) {
            destinationViewModel.readEntries(userId);
        }

        // Set button click listeners to open the respective forms
        binding.btnLogTravel.setOnClickListener(v -> openLogTravelForm());
        binding.btnCalculateVacation.setOnClickListener(v -> openVacationTimeForm());
    }

    private void updateDestinationList(List<DestinationEntry> entries) {
        // Display the list of destinations and their durations in the TextView or RecyclerView
        StringBuilder listBuilder = new StringBuilder();
        for (DestinationEntry entry : entries) {
            // Duration is already calculated in the DestinationEntry class
            listBuilder.append(entry.getLocation()).append(": ").append(entry.getDuration()).append(" days\n");
        }
        binding.destinationListTextView.setText(listBuilder.toString());

        // Update Total Days and Planned Days dynamically
        int totalDays = 0;
        int plannedDays = 0;

        for (DestinationEntry entry : entries) {
            totalDays += entry.getDuration();
            if (entry.isPlanned()) {
                plannedDays += entry.getDuration();
            }
        }

        binding.totalDaysTextView.setText("Total Days: " + totalDays + " days");
        binding.plannedDaysTextView.setText("Planned Days: " + plannedDays + " days");
    }

    // Open the Log Travel form
    private void openLogTravelForm() {
        if (userId != null) {
            Intent intent = new Intent(this, LogTravelForm.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        } else {
            Toast.makeText(this, "User ID is missing!", Toast.LENGTH_SHORT).show();
        }
    }

    // Open the Vacation Time form
    private void openVacationTimeForm() {
        if (userId != null) {
            Intent intent = new Intent(this, VacationTimeForm.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        } else {
            Toast.makeText(this, "User ID is missing!", Toast.LENGTH_SHORT).show();
        }
    }
}