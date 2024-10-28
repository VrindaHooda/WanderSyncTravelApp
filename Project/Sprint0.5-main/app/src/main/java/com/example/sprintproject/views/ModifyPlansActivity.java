package com.example.sprintproject.views;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.sprintproject.R;
import com.example.sprintproject.viewmodels.UserDurationViewModel;
import com.example.sprintproject.viewmodels.ValidateViewModel;

import java.util.Calendar;

public class ModifyPlansActivity extends AppCompatActivity {

    private UserDurationViewModel userDurationViewModel;

    Button open = findViewById(R.id.openStartDatePicker2);
    Button enddate = findViewById(R.id.openEndDatePicker2);
    TextView start = findViewById(R.id.startDateText2);
    TextView dateend = findViewById(R.id.endDateText2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vacation_time_form);

        // Set up click listeners to open date pickers
        open.setOnClickListener(v -> openDatePicker(start));
        enddate.setOnClickListener(v -> openDatePicker(dateend));
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

