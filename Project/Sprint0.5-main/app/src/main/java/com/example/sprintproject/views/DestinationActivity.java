package com.example.sprintproject.views;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.sprintproject.R;
import com.example.sprintproject.model.DestinationEntry;
import com.example.sprintproject.viewmodels.DestinationViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DestinationActivity extends AppCompatActivity {

    private DestinationViewModel destinationViewModel;
    private EditText durationEditText;
    private Button startDateButton, endDateButton, calculateButton, calculateVacationButton;
    private LinearLayout vacationForm;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private Calendar calendar = Calendar.getInstance();
    private String startDate, endDate;
    private String userId = "user123";  // This is an example. Replace with actual user ID logic.
    private TextView totalVacationDaysTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.destination_screen);

        // Initialize ViewModel
        destinationViewModel = new ViewModelProvider(this).get(DestinationViewModel.class);

        // Initialize UI Elements
        durationEditText = findViewById(R.id.durationInput);
        startDateButton = findViewById(R.id.btn_start_date);
        endDateButton = findViewById(R.id.btn_end_date);
        calculateButton = findViewById(R.id.btn_calculate);
        calculateVacationButton = findViewById(R.id.btn_calculate_vacation);
        vacationForm = findViewById(R.id.vacation_form);
        totalVacationDaysTextView = findViewById(R.id.total_vacation_days);

        // Show form when Calculate Vacation Time button is clicked
        calculateVacationButton.setOnClickListener(v -> vacationForm.setVisibility(View.VISIBLE));

        // Set up DatePicker for Start Date
        startDateButton.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(year, month, dayOfMonth);
                        startDate = dateFormat.format(calendar.getTime());
                        startDateButton.setText(startDate);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });

        // Set up DatePicker for End Date
        endDateButton.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(year, month, dayOfMonth);
                        endDate = dateFormat.format(calendar.getTime());
                        endDateButton.setText(endDate);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });

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
    }

    private boolean isValidInput(String duration, String startDate, String endDate) {
        return (!duration.isEmpty() && startDate != null) || (startDate != null && endDate != null) || (!duration.isEmpty() && endDate != null);
    }

    // The calculateMissingValue, calculateDuration, calculateStartDate, calculateEndDate methods remain the same...
}
