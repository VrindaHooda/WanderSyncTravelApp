package com.example.sprintproject.views;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sprintproject.R;
import java.util.Calendar;

public class ModifyPlansActivity extends AppCompatActivity {

    private EditText locationInput;
    private Button openStartDatePicker;
    private Button openEndDatePicker;
    private Button submitTravelLogButton;
    private TextView startDateText;
    private TextView endDateText;
    private Calendar endDate;
    private Calendar startDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_trip_plans); // Ensure this matches your XML file name

        // Initialize UI components
        locationInput = findViewById(R.id.locationInput1);
        openStartDatePicker = findViewById(R.id.openStartDatePicker1);
        openEndDatePicker = findViewById(R.id.openEndDatePicker1);
        submitTravelLogButton = findViewById(R.id.submitTravelLogButton1);
        startDateText = findViewById(R.id.startDateText1);
        endDateText = findViewById(R.id.endDateText1);

        // Set up date picker dialogs
        openStartDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialog(true);
            }
        });

        openEndDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialog(false);
            }
        });

        // Set up submit button click event
        submitTravelLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitTravelLog();
            }
        });

        Button exitButton = findViewById(R.id.exitbtn2);
        exitButton.setOnClickListener(v -> {
            finish();
        });
    }

    private void openDatePickerDialog(final boolean isStartDate) {
        final Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                ModifyPlansActivity.this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);

                    if (isStartDate) {
                        startDate = calendar;
                        startDateText.setText("Start Date: " + (month + 1)
                                + "/" + dayOfMonth + "/" + year);
                    } else {
                        endDate = calendar;
                        endDateText.setText("End Date: " + (month + 1) + "/"
                                + dayOfMonth + "/" + year);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    private void submitTravelLog() {
        String location = locationInput.getText().toString();
        if (location.isEmpty()) {
            Toast.makeText(this, "Please enter a location", Toast.LENGTH_SHORT).show();
            return;
        }
        if (startDate == null || endDate == null) {
            Toast.makeText(this, "Please select both start and end dates",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Process data (e.g., save to database)
        // Example: saveTripData(location, startDate, endDate);

        Toast.makeText(this, "Travel log submitted!", Toast.LENGTH_SHORT).show();
    }
}
