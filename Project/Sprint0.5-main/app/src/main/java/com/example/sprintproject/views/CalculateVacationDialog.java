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
    public View onCreateView(
            @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
                Toast.makeText(getContext(),
                        "Please provide at least two inputs to calculate the third", Toast.LENGTH_SHORT).show();
            }
        });

        // Observe ViewModel for calculated duration
        destinationViewModel.getCalculatedDuration().observe(getViewLifecycleOwner(), calculatedDuration -> {
            if (calculatedDuration != null) {
                if (startDateInput.getText().toString().isEmpty() && !endDateInput.getText().toString().isEmpty()) {
                    startDateInput.setText(formatDate(calculateStartDate(
                            endDateInput.getText().toString(), calculatedDuration)));
                } else if (endDateInput.getText().toString().isEmpty()
                        && !startDateInput.getText().toString().isEmpty()) {
                    endDateInput.setText(formatDate(calculateEndDate(
                            startDateInput.getText().toString(), calculatedDuration)));
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

    /**
     * Shows a DatePicker dialog for selecting a date and sets the selected date in the input field.
     *
     * @param dateInputField the {@link EditText} field to populate with the selected date
     */
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
                    String formattedDate = String.format(Locale.getDefault(),
                            "%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear);
                    dateInputField.setText(formattedDate);
                },
                year,
                month,
                day
        );

        datePickerDialog.show();
    }

    /**
     * Parses a date string in the format "yyyy-MM-dd" into a {@link Date} object.
     *
     * @param dateString the date string to parse
     * @return the parsed {@link Date} object, or {@code null} if parsing fails
     */
    private Date parseDate(String dateString) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            return format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Parses a string into an {@link Integer}.
     *
     * @param intString the string to parse
     * @return the parsed {@link Integer}, or {@code null} if parsing fails
     */
    private Integer parseInteger(String intString) {
        try {
            return Integer.parseInt(intString);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Formats a {@link Date} object into a string in the format "yyyy-MM-dd".
     *
     * @param date the {@link Date} object to format
     * @return the formatted date string
     */
    private String formatDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return format.format(date);
    }

    /**
     * Calculates the start date given an end date and duration in days.
     *
     * @param endDateString the end date as a string in the format "yyyy-MM-dd"
     * @param duration      the duration in days
     * @return the calculated start {@link Date}, or {@code null} if the end date is invalid
     */
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


    /**
     * Calculates the end date given a start date and duration in days.
     *
     * @param startDateString the start date as a string in the format "yyyy-MM-dd"
     * @param duration        the duration in days
     * @return the calculated end {@link Date}, or {@code null} if the start date is invalid
     */
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

    /**
     * Sets the listener for save actions.
     *
     * @param onSaveListener the {@link OnSaveListener} to set
     */
    public void setOnSaveListener(OnSaveListener onSaveListener) {
        this.onSaveListener = onSaveListener;
    }

    /**
     * Sets the listener for calculate actions.
     *
     * @param onCalculateListener the {@link OnCalculateListener} to set
     */
    public void setOnCalculateListener(OnCalculateListener onCalculateListener) {
        this.onCalculateListener = onCalculateListener;
    }

    public interface OnSaveListener {

        /**
         * Called when a vacation entry is saved.
         *
         * @param vacationEntry the saved {@link VacationEntry}
         */
        void onSave(VacationEntry vacationEntry);
    }

    public interface OnCalculateListener {

        /**
         * Called when vacation details are calculated.
         *
         * @param startDate the calculated start {@link Date}
         * @param endDate   the calculated end {@link Date}
         * @param duration  the calculated duration in days
         */
        void onCalculate(Date startDate, Date endDate, Integer duration);
    }
}
