package com.example.sprintproject.views;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import com.example.sprintproject.R;
import com.example.sprintproject.model.DestinationEntry;
import com.example.sprintproject.model.DurationEntry;
import com.example.sprintproject.viewmodels.AuthViewModel;
import com.example.sprintproject.viewmodels.UserDurationViewModel;
import com.example.sprintproject.viewmodels.ValidateViewModel;
import com.example.sprintproject.viewmodels.DestinationViewModel;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DestinationActivity extends AppCompatActivity {

    private AuthViewModel authViewModel;
    private DestinationViewModel destinationViewModel;
    private UserDurationViewModel userDurationViewModel;
    private ValidateViewModel validateViewModel;
    private TextView destinationListTextView;
    private Calendar startDate;
    private Calendar endDate;
    private TextView totalDaysTextView;
    private String finalUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.destination_screen);

        //adds fragment bar
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.bottomNavigation, NavigationFragment.class, null)
                    .commit();
        }

        // Getting the intent
        Intent intent = getIntent();

        String username = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");

        try {
            destinationViewModel = new ViewModelProvider(this).get(DestinationViewModel.class);
            userDurationViewModel = new ViewModelProvider(this).get(UserDurationViewModel.class);
            validateViewModel = new ViewModelProvider(this).get(ValidateViewModel.class);
            authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

            destinationListTextView = findViewById(R.id.destinationListTextView);
            totalDaysTextView = findViewById(R.id.totalDaysTextView); // Initialize the total days TextView
            Button logTravelButton = findViewById(R.id.btn_log_travel);
            Button calculateVacationTime = findViewById(R.id.btn_calculate_vacation);

            if (destinationListTextView == null)
                Log.e("DestinationActivity", "destinationListTextView is null");
            if (logTravelButton == null) Log.e("DestinationActivity", "logTravelButton is null");

            destinationViewModel.getDestinationEntries().observe(this, entries -> updateDestinationList(entries));
            destinationViewModel.prepopulateDatabase();
            destinationViewModel.readEntries();

            logTravelButton.setOnClickListener(v -> openLogTravelDialog());
            calculateVacationTime.setOnClickListener(v -> openCalculateVacationDialog(username, password));

        } catch (Exception e) {
            Log.e("DestinationActivity", "Error in onCreate", e);
        }
    }

    //makes a destination list
    private void updateDestinationList(List<DestinationEntry> entries) {
        StringBuilder listBuilder = new StringBuilder();
        long totalDays = 0;
        for (DestinationEntry entry : entries) {
            long duration = (entry.getEndDate().getTime() - entry.getStartDate().getTime()) / (1000 * 60 * 60 * 24);
            listBuilder.append(entry.getLocation()).append(": ").append(duration).append(" days\n");
            totalDays += duration;
        }
        destinationListTextView.setText(listBuilder.toString());
        updateTotalDays(totalDays);
    }
    private void updateTotalDays(long totalDays) {
        TextView totalDaysTextView = findViewById(R.id.totalDaysTextView);
        totalDaysTextView.setText("Total Days: " + totalDays + " days");
    }


    private void openLogTravelDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.log_travel_form, null);
        builder.setView(dialogView);

        EditText locationInput = dialogView.findViewById(R.id.locationInput);
        Button openStartDatePicker = dialogView.findViewById(R.id.openStartDatePicker);
        Button openEndDatePicker = dialogView.findViewById(R.id.openEndDatePicker);
        TextView startDateText = dialogView.findViewById(R.id.startDateText);
        TextView endDateText = dialogView.findViewById(R.id.endDateText);
        Button submitTravelLogButton = dialogView.findViewById(R.id.submitTravelLogButton);

        AlertDialog dialog = builder.create();

        startDate = Calendar.getInstance();
        endDate = Calendar.getInstance();

        openStartDatePicker.setOnClickListener(v -> openDatePickerDialog(startDate, startDateText));
        openEndDatePicker.setOnClickListener(v -> openDatePickerDialog(endDate, endDateText));

        submitTravelLogButton.setOnClickListener(v -> {
            String location = locationInput.getText().toString();

            if (!validateViewModel.validateDate(startDate.getTime(), endDate.getTime())) {
                Toast.makeText(this, "End date must be after start date", Toast.LENGTH_SHORT).show();
                return;
            }

            DestinationEntry newEntry = new DestinationEntry(
                    destinationViewModel.generateDestinationId(location, startDate.getTime()),
                    location,
                    startDate.getTime(),
                    endDate.getTime()
            );

            destinationViewModel.addDestination(newEntry);
            Toast.makeText(this, "Travel log added", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }

    private void openCalculateVacationDialog(String username, String password) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.vacation_time_form, null);
        builder.setView(dialogView);

        EditText vacationInput = dialogView.findViewById(R.id.vacationTimeInput);
        Button openStartDatePicker2 = dialogView.findViewById(R.id.openStartDatePicker2);
        Button openEndDatePicker2 = dialogView.findViewById(R.id.openEndDatePicker2);
        TextView startDateText2 = dialogView.findViewById(R.id.startDateText2);
        TextView endDateText2 = dialogView.findViewById(R.id.endDateText2);
        Button submitVacationTimeButton = dialogView.findViewById(R.id.submitVacationTimeButton);

        AlertDialog dialog = builder.create();

        startDate = Calendar.getInstance();
        endDate = Calendar.getInstance();

        openStartDatePicker2.setOnClickListener(v -> openDatePickerDialog(startDate, startDateText2));
        openEndDatePicker2.setOnClickListener(v -> openDatePickerDialog(endDate, endDateText2));

        submitVacationTimeButton.setOnClickListener(v -> {
            int vacationDuration = calculateValues(vacationInput, startDateText2, endDateText2);
            saveData(username, password, vacationDuration, startDateText2, endDateText2);
        });

        dialog.show();
    }

    private int calculateValues(EditText vacationInput, TextView startDateText, TextView endDateText) {
        String durationStr = vacationInput.getText().toString();

        String calculatedResult = null;
        String resultMessage = null;

        if (!startDateText.getText().toString().isEmpty() && !durationStr.isEmpty()) {
            calculatedResult = userDurationViewModel.calculateMissingValue(startDate.getTime(), null,Long.parseLong(durationStr) );
            resultMessage = "Calculated End Date: " + calculatedResult;
        } else if (!startDateText.getText().toString().isEmpty() && !endDateText.getText().toString().isEmpty()) {
            calculatedResult = userDurationViewModel.calculateMissingValue(startDate.getTime(), endDate.getTime(), null);
            resultMessage = "Calculated Duration: " + calculatedResult;
        } else if (!endDateText.getText().toString().isEmpty() && !durationStr.isEmpty()) {
            calculatedResult = userDurationViewModel.calculateMissingValue(null, endDate.getTime(), Long.parseLong(durationStr));
            resultMessage = "Calculated Start Date: " + calculatedResult;
        } else {
            Toast.makeText(this, "Please provide at least two inputs", Toast.LENGTH_SHORT).show();
        }

        AlertDialog.Builder resultDialogBuilder = new AlertDialog.Builder(this);
        resultDialogBuilder.setTitle("Calculation Result");
        resultDialogBuilder.setMessage(resultMessage);
        resultDialogBuilder.setPositiveButton("OK", (resultDialogButton, which) -> resultDialogButton.dismiss());
        resultDialogBuilder.create().show();

        clearVacationForm(startDateText, endDateText, vacationInput);
        return dateConversion(calculatedResult);
    }

    private void getUserId(LiveData<String> liveData, StringCallback callback) {
        liveData.observe(this, newUserId -> {
            if (newUserId != null) {
                Log.d("UserId", "The UserId is: " + newUserId);
                callback.onResult(newUserId); // Return the value through the callback
            } else {
                Log.d("UserId", "UserId not found");
                callback.onResult(null); // Return null if not found
            }
        });
    }

    public interface StringCallback {
        void onResult(String value);
    }

    private void saveData(String username, String password, int vacationDuration, Calendar startDate, Calendar endDate) {
        // Assuming authViewModel is already initialized and user has signed in or registered
        authViewModel.signIn(username, password, new AuthViewModel.AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                // Call getUserId after successful sign-in
                getUserId(authViewModel.getUserIdLiveData(), new StringCallback() {
                    @Override
                    public void onResult(String userId) {
                        if (userId != null) {
                            finalUserId = userId;
                            Log.d("UserId", "Successfully retrieved UserId: " + finalUserId);

                            // Generate vacationId only after successful sign-in and userId retrieval
                            String vacationId = userDurationViewModel.generateVacationId(vacationDuration, new Date());
                            Date startDateVal = startDate.getTime(); // Convert startDate Calendar to Date
                            Date endDateVal = endDate.getTime();

                            // Now save the data with finalUserId
                            userDurationViewModel.saveDurationData(finalUserId, username, new DurationEntry(vacationId, vacationDuration, startDateVal, endDateVal));
                            Toast.makeText(DestinationActivity.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            // Handle the case when userId is null
                            Log.d("UserId", "Failed to retrieve UserId.");
                        }
                    }
                });
            }

            @Override
            public void onFailure(String error) {
                Log.e("Auth", "Sign-in failed: " + error);
            }
        });
    }


    private void clearVacationForm(TextView startDateText, TextView endDateText, EditText vacationInput) {
        startDateText.setText("");
        endDateText.setText("");
        vacationInput.setText("");
        startDate = Calendar.getInstance();
        endDate = Calendar.getInstance();
    }

    private void openDatePickerDialog(Calendar date, TextView dateTextView) {
        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH);
        int day = date.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    date.set(selectedYear, selectedMonth, selectedDay);
                    dateTextView.setText("Selected Date: " + selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
                }, year, month, day);
        datePickerDialog.show();
    }
}