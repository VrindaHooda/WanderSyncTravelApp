package com.example.sprintproject.views;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sprintproject.R;
import java.util.Calendar;

public class LogTravelForm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_travel_form);

        // Initialize UI components
        Button openStartDatePicker = findViewById(R.id.openStartDatePicker);
        Button openEndDatePicker = findViewById(R.id.openEndDatePicker);
        TextView startDateText = findViewById(R.id.startDateText);
        TextView endDateText = findViewById(R.id.endDateText);

        // Set up click listeners to open date pickers
        openStartDatePicker.setOnClickListener(v -> openDatePicker(startDateText));
        openEndDatePicker.setOnClickListener(v -> openDatePicker(endDateText));
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
                    // Set the selected date in the TextView
                    dateTextView.setText("Selected Date: " + selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
                }, year, month, day);
        datePickerDialog.show();
    }
}
