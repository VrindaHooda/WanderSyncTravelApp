package com.example.sprintproject.views;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.sprintproject.R;
import com.example.sprintproject.model.VacationEntry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalculateVacationDialog extends DialogFragment {
    private EditText startDateInput;
    private EditText endDateInput;
    private EditText durationInput;
    private Button calculateButton;
    private Button saveButton;
    private OnSaveListener onSaveListener;
    private OnCalculateListener onCalculateListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_calculate_vacation, container, false);

        // Initialize Views
        startDateInput = rootView.findViewById(R.id.startDateInput);
        endDateInput = rootView.findViewById(R.id.endDateInput);
        durationInput = rootView.findViewById(R.id.durationInput);
        calculateButton = rootView.findViewById(R.id.calculateButton);
        saveButton = rootView.findViewById(R.id.saveButton);
        startDateInput.setOnClickListener(v -> showDatePickerDialog(startDateInput));

        // Attach DatePickerDialog to End Date Input
        endDateInput.setOnClickListener(v -> showDatePickerDialog(endDateInput));

        // Set up Calculate Button Listener
        calculateButton.setOnClickListener(v -> {
            Date startDate = parseDate(startDateInput.getText().toString());
            Date endDate = parseDate(endDateInput.getText().toString());
            Integer duration = parseInteger(durationInput.getText().toString());

            if (onCalculateListener != null) {
                onCalculateListener.onCalculate(startDate, endDate, duration);
            }
        });

        // Set up Save Button Listener
        saveButton.setOnClickListener(v -> {
            Date startDate = parseDate(startDateInput.getText().toString());
            Date endDate = parseDate(endDateInput.getText().toString());
            Integer duration = parseInteger(durationInput.getText().toString());

            if (onSaveListener != null && startDate != null && endDate != null && duration != null) {
                VacationEntry vacationEntry = new VacationEntry(startDate, endDate, duration);
                onSaveListener.onSave(vacationEntry);
                dismiss();
            } else {
                Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
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

    public void setOnCalculateListener(OnCalculateListener onCalculateListener) {
        this.onCalculateListener = onCalculateListener;
    }

    public interface OnSaveListener {
        void onSave(VacationEntry vacationEntry);
    }

    public interface OnCalculateListener {
        void onCalculate(Date startDate, Date endDate, Integer duration);
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

    private Integer parseInteger(String intString) {
        try {
            return Integer.parseInt(intString);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }
}

