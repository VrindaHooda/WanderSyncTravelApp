package com.example.sprintproject.views;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sprintproject.R;
import com.example.sprintproject.viewmodels.UserDurationViewModel;

import java.util.Calendar;

public class VacationTimeForm extends AppCompatActivity {

    private UserDurationViewModel userDurationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vacation_time_form);

        Button openStartDatePicker = findViewById(R.id.openStartDatePicker2);
        Button openEndDatePicker = findViewById(R.id.openEndDatePicker2);
        TextView startDateText = findViewById(R.id.startDateText2);
        TextView endDateText = findViewById(R.id.endDateText2);

        openStartDatePicker.setOnClickListener(v -> openDatePicker(startDateText));
        openEndDatePicker.setOnClickListener(v -> openDatePicker(endDateText));
    }

    private void openDatePicker(TextView dateTextView) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {

                    dateTextView.setText("Selected Date: " + selectedDay + "/"
                            + (selectedMonth + 1) + "/" + selectedYear);
                }, year, month, day);
        datePickerDialog.show();
    }
}

