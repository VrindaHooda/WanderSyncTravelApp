package com.example.sprintproject.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.sprintproject.R;
import com.example.sprintproject.model.VacationEntry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
            }
        });

        return rootView;
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

