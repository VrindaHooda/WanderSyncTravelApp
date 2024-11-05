package com.example.sprintproject.views;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
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
    private EditText locationInput;
    private List<TravelLogEntry> travelLogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_travel_form);

        Button openStartDatePicker = findViewById(R.id.openStartDatePicker);
        Button openEndDatePicker = findViewById(R.id.openEndDatePicker);
        startDateText = findViewById(R.id.startDateText);
        endDateText = findViewById(R.id.endDateText);
        locationInput = findViewById(R.id.locationInput);

        travelLogs = new ArrayList<>();

        openStartDatePicker.setOnClickListener(v -> openDatePicker(startDateText));
        openEndDatePicker.setOnClickListener(v -> openDatePicker(endDateText));

        Button saveLogButton = findViewById(R.id.submitTravelLogButton);
        saveLogButton.setOnClickListener(v -> saveTravelLog());
    }

    public void setLocationInput(String location) {
        locationInput.setText(location);
    }

    public boolean isFormValid() {
        String location = locationInput.getText().toString();
        return !TextUtils.isEmpty(location) && !location.trim().isEmpty();
    }

    private void openDatePicker(TextView dateTextView) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(selectedYear, selectedMonth, selectedDay);
                    String formattedDate = new SimpleDateFormat("yyyy-MM-dd",
                            Locale.getDefault()).format(selectedDate.getTime());
                    dateTextView.setText(formattedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void saveTravelLog() {
        String startDate = startDateText.getText().toString();
        String endDate = endDateText.getText().toString();
        String location = locationInput.getText().toString();

        if (startDate.isEmpty() || endDate.isEmpty()) {
            Toast.makeText(this, "Please select both start date and end date",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (startDate.compareTo(endDate) >= 0) {
            Toast.makeText(this, "End date must be after start date",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        travelLogs.add(new TravelLogEntry(location, startDate, endDate));
        Toast.makeText(this, "Travel log saved successfully!",
                Toast.LENGTH_SHORT).show();
    }
}
