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
import androidx.lifecycle.ViewModelProvider;

import com.example.sprintproject.R;
import com.example.sprintproject.model.VacationEntry;
import com.example.sprintproject.viewmodels.DestinationViewModel;

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
    private DestinationViewModel destinationViewModel;

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

        // Initialize ViewModel
        destinationViewModel = new ViewModelProvider(requireActivity()).get(DestinationViewModel.class);

        startDateInput.setOnClickListener(v -> showDatePickerDialog(startDateInput));
        endDateInput.setOnClickListener(v -> showDatePickerDialog(endDateInput));

        // Set up Calculate Button Listener
        calculateButton.setOnClickListener(v -> {
            Date startDate = parseDate(startDateInput.getText().toString());
            Date endDate = parseDate(endDateInput.getText().toString());
            Integer duration = parseInteger(durationInput.getText().toString());

            if (startDate != null && endDate != null) {
                destinationViewModel.calculateVacationTime(startDate, endDate, null);
            } else if (startDate != null && duration != null) {
                destinationViewModel.calculateVacationTime(startDate, null, duration);
            } else if (endDate != null && duration != null) {
                destinationViewModel.calculateVacationTime(null, endDate, duration);
            } else {
                Toast.makeText(getContext(), "Please provide at least two inputs to calculate the third", Toast.LENGTH_SHORT).show();
            }
        });

        // Observe ViewModel for calculated duration
        destinationViewModel.getCalculatedDuration().observe(getViewLifecycleOwner(), calculatedDuration -> {
            if (calculatedDuration != null) {
                if (startDateInput.getText().toString().isEmpty() && !endDateInput.getText().toString().isEmpty()) {
                    startDateInput.setText(formatDate(calculateStartDate(endDateInput.getText().toString(), calculatedDuration)));
                } else if (endDateInput.getText().toString().isEmpty() && !startDateInput.getText().toString().isEmpty()) {
                    endDateInput.setText(formatDate(calculateEndDate(startDateInput.getText().toString(), calculatedDuration)));
                } else if (durationInput.getText().toString().isEmpty()) {
                    durationInput.setText(String.valueOf(calculatedDuration));
                }
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

    private String formatDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return format.format(date);
    }

    private Date calculateStartDate(String endDateString, int duration) {
        Date endDate = parseDate(endDateString);
        if (endDate != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(endDate);
            calendar.add(Calendar.DAY_OF_YEAR, -duration);
            return calendar.getTime();
        }
        return null;
    }

    private Date calculateEndDate(String startDateString, int duration) {
        Date startDate = parseDate(startDateString);
        if (startDate != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            calendar.add(Calendar.DAY_OF_YEAR, duration);
            return calendar.getTime();
        }
        return null;
    }
}
