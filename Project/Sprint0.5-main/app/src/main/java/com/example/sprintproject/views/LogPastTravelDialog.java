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
import com.example.sprintproject.model.TravelLog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LogPastTravelDialog extends DialogFragment {
    private EditText locationInput;
    private EditText startDateInput;
    private EditText endDateInput;
    private Button saveButton;
    private OnSaveListener onSaveListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_log_past_travel, container, false);

        // Initialize Views
        locationInput = rootView.findViewById(R.id.locationInput);
        startDateInput = rootView.findViewById(R.id.startDateInput);
        endDateInput = rootView.findViewById(R.id.endDateInput);
        saveButton = rootView.findViewById(R.id.saveButton);

        // Set up Save Button Listener
        saveButton.setOnClickListener(v -> {
            String location = locationInput.getText().toString();
            Date startDate = parseDate(startDateInput.getText().toString());
            Date endDate = parseDate(endDateInput.getText().toString());

            if (onSaveListener != null && location != null && startDate != null && endDate != null) {
                TravelLog travelLog = new TravelLog(location, startDate, endDate);
                onSaveListener.onSave(travelLog);
                dismiss();
            }
        });

        return rootView;
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


