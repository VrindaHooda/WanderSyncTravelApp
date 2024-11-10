package com.example.sprintproject.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import com.example.sprintproject.R;
import com.example.sprintproject.viewmodels.LogTravelViewModel;

public class LogTravelForm extends AppCompatActivity {

    private LogTravelViewModel logTravelViewModel;

    private EditText locationEditText;
    private TextView startDateTextView;
    private TextView endDateTextView;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_travel_form);

        logTravelViewModel = new LogTravelViewModel();

        // Find views
        locationEditText = findViewById(R.id.locationInput);
        startDateTextView = findViewById(R.id.startDateText);
        endDateTextView = findViewById(R.id.endDateText);
        saveButton = findViewById(R.id.submitTravelLogButton);

        // Set observers to update the UI
        logTravelViewModel.getStartDate().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String startDate) {
                startDateTextView.setText(startDate);
            }
        });

        logTravelViewModel.getEndDate().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String endDate) {
                endDateTextView.setText(endDate);
            }
        });

        // Set click listeners for date pickers
        startDateTextView.setOnClickListener(view -> logTravelViewModel.onStartDateClick());
        endDateTextView.setOnClickListener(view -> logTravelViewModel.onEndDateClick());

        // Save travel log when the save button is clicked
        saveButton.setOnClickListener(view -> {
            String location = locationEditText.getText().toString().trim();

            if (location.isEmpty()) {
                Toast.makeText(LogTravelForm.this, "Please enter a location", Toast.LENGTH_SHORT).show();
            } else {
                // Pass userId and email (you can replace these with real values from your app context)
                Intent intent = getIntent();
                String userId = intent.getStringExtra("userId") ;
                String email = intent.getStringExtra("username");

                logTravelViewModel.setLocation(location);
                logTravelViewModel.saveTravelLog(userId, email);
            }
        });
    }
}
