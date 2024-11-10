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

import java.util.List;

public class DestinationActivity extends AppCompatActivity {

    private DestinationViewModel destinationViewModel;
    private DestinationActivityBinding binding;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize data binding
        binding = DataBindingUtil.setContentView(this, R.layout.destination_screen);

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

        // Set button click listeners to open the respective forms
        binding.btnLogTravel.setOnClickListener(v -> openLogTravelForm());
        binding.btnCalculateVacation.setOnClickListener(v -> openVacationTimeForm());

        // Fetch the last 5 destination entries
        if (userId != null) {
            destinationViewModel.readEntries(userId);
        }
    }

    private void updateDestinationList(List<DestinationEntry> entries) {
        // Display the last 5 entries in the TextView or RecyclerView
        StringBuilder listBuilder = new StringBuilder();
        for (DestinationEntry entry : entries) {
            long duration = (entry.getEndDate().getTime() - entry.getStartDate().getTime()) / (1000 * 60 * 60 * 24);
            listBuilder.append(entry.getLocation()).append(": ").append(duration).append(" days\n");
        }
        binding.destinationListTextView.setText(listBuilder.toString());
    }

    // Open the Log Travel form
    private void openLogTravelForm(String userId) {
        Intent intent = new Intent(this, LogTravelForm.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    // Open the Vacation Time form
    private void openVacationTimeForm(String userId) {
        Intent intent = new Intent(this, VacationTimeForm.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }
}
