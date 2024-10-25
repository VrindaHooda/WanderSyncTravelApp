package com.example.sprintproject.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
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
    private LinearLayout datePickerButtonsLayout;
    private Button btnSelectStartDate, btnSelectEndDate;
    private Calendar startDate;
    private Calendar endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.destination_screen);

        destinationViewModel = new ViewModelProvider(this).get(DestinationViewModel.class);
        validateViewModel = new ViewModelProvider(this).get(ValidateViewModel.class);

        destinationListTextView = findViewById(R.id.destinationListTextView);
        datePickerButtonsLayout = findViewById(R.id.date_picker_buttons);
        btnSelectStartDate = findViewById(R.id.btn_select_start_date);
        btnSelectEndDate = findViewById(R.id.btn_select_end_date);
        Button logTravelButton = findViewById(R.id.btn_log_travel);
        Button enterDestinationButton = findViewById(R.id.btn_enter_destination);

        // Initially hide the date picker buttons
        datePickerButtonsLayout.setVisibility(View.GONE);

        // When Log Travel button is clicked, show date picker buttons
        logTravelButton.setOnClickListener(v -> {
            datePickerButtonsLayout.setVisibility(View.VISIBLE);
        });

        // Start date picker button
        btnSelectStartDate.setOnClickListener(v -> openDatePicker(true));

        // End date picker button
        btnSelectEndDate.setOnClickListener(v -> openDatePicker(false));

        // Enter destination button click listener
        enterDestinationButton.setOnClickListener(v -> {
            Intent intent = new Intent(DestinationActivity.this, EnterDestinationActivity.class);
            startActivity(intent);
        });

        destinationViewModel.getDestinationEntries().observe(this, this::updateDestinationList);

        destinationViewModel.readEntries();
    }

    // Helper method to update the destination list
    private void updateDestinationList(List<DestinationEntry> entries) {
        StringBuilder listBuilder = new StringBuilder();
        for (DestinationEntry entry : entries) {
            long duration = (entry.getEndDate().getTime() - entry.getStartDate().getTime()) / (1000 * 60 * 60 * 24);
            listBuilder.append(entry.getLocation()).append(": ").append(duration).append(" days\n");
        }
        destinationListTextView.setText(listBuilder.toString());
    }

    // Helper method to open a date picker
    private void openDatePicker(boolean isStartDate) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View datePickerView = getLayoutInflater().inflate(R.layout.date_picker_dialog, null);
        DatePicker datePicker = datePickerView.findViewById(R.id.datePicker);
        Button confirmButton = datePickerView.findViewById(R.id.confirmDateButton);

        builder.setView(datePickerView);
        AlertDialog dialog = builder.create();

        confirmButton.setOnClickListener(v -> {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());

            if (isStartDate) {
                startDate = selectedDate;
                Toast.makeText(this, "Start Date Selected", Toast.LENGTH_SHORT).show();
            } else {
                endDate = selectedDate;
                Toast.makeText(this, "End Date Selected", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });

        dialog.show();
    }
}
