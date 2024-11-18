package com.example.sprintproject.views;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.sprintproject.R;
import com.example.sprintproject.model.TravelLog;
import com.example.sprintproject.viewmodels.DestinationViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class LogPastTravelDialog extends DialogFragment {
    private EditText locationInput;
    private EditText startDateInput;
    private EditText endDateInput;
    private Button saveButton;
    private TextView durationTextView;
    private OnSaveListener onSaveListener;
    private DestinationViewModel destinationViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_log_past_travel, container, false);

        // Initialize Views
        locationInput = rootView.findViewById(R.id.locationInput);
        startDateInput = rootView.findViewById(R.id.startDateInput);
        endDateInput = rootView.findViewById(R.id.endDateInput);
        saveButton = rootView.findViewById(R.id.saveButton);
        durationTextView = rootView.findViewById(R.id.durationTextView);

        // Initialize ViewModel
        destinationViewModel = new ViewModelProvider(requireActivity()).get(DestinationViewModel.class);

        startDateInput.setOnClickListener(v -> showDatePickerDialog(startDateInput));
        endDateInput.setOnClickListener(v -> showDatePickerDialog(endDateInput));

        // Set up Save Button Listener
        saveButton.setOnClickListener(v -> {
            String location = locationInput.getText().toString();
            Date startDate = parseDate(startDateInput.getText().toString());
            Date endDate = parseDate(endDateInput.getText().toString());

            if (onSaveListener != null && !location.isEmpty() && startDate != null && endDate != null) {
                TravelLog travelLog = new TravelLog(location, startDate, endDate);
                onSaveListener.onSave(travelLog);
                dismiss();
            } else {
                Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
        });

        // Observe calculated duration
        destinationViewModel.getCalculatedDuration().observe(getViewLifecycleOwner(), duration -> {
            if (duration != null) {
                durationTextView.setText(getString(R.string.travel_duration, duration));
            }
        });

        startDateInput.setOnFocusChangeListener((v, hasFocus) -> calculateDurationIfNeeded());
        endDateInput.setOnFocusChangeListener((v, hasFocus) -> calculateDurationIfNeeded());

        return rootView;
    }

    private void calculateDurationIfNeeded() {
        Date startDate = parseDate(startDateInput.getText().toString());
        Date endDate = parseDate(endDateInput.getText().toString());

        if (startDate != null && endDate != null) {
            destinationViewModel.calculateVacationTime(startDate, endDate, null);
        }
    }

    private void showDatePickerDialog(EditText dateInputField) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create and display the DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Format the selected date and set it to the EditText field
                    String formattedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear);
                    dateInputField.setText(formattedDate);
                    calculateDurationIfNeeded();
                },
                year,
                month,
                day
        );

        datePickerDialog.show();
    }

    public void setOnSaveListener(OnSaveListener onSaveListener) {
        this.onSaveListener = onSaveListener;
    }

    public interface OnSaveListener {
        void onSave(TravelLog travelLog);
    }

    private Date parseDate(String dateString) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            return format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
