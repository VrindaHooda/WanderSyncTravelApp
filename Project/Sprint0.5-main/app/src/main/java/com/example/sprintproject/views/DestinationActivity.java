package com.example.sprintproject.views;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.DateFormat;
import java.util.ArrayList;
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
    private String username;
    private String password;
    private String finalEmail;
    private TextView plannedDaysTextView;
    private int duration;
    private int totalDays;
    Button logTravelButton;
    Button calculateVacationTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.destination_screen);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.bottomNavigation, NavigationFragment.class, null)
                    .commit();
        }


        try {
            Intent intent = getIntent();
            finalEmail = intent.getStringExtra("username");
            finalUserId = intent.getStringExtra("userId");
            Intent intent1 = new Intent(DestinationActivity.this, LogisticsActivity.class);
            intent1.putExtra("userId", finalUserId);
            intent1.putExtra("username", finalEmail);
            destinationViewModel = new ViewModelProvider(this).get(DestinationViewModel.class);
            userDurationViewModel = new ViewModelProvider(this).get(UserDurationViewModel.class);
            validateViewModel = new ViewModelProvider(this).get(ValidateViewModel.class);
            authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

            destinationListTextView = findViewById(R.id.destinationListTextView);
            totalDaysTextView = findViewById(R.id.totalDaysTextView);
            logTravelButton = findViewById(R.id.btn_log_travel);
            calculateVacationTime = findViewById(R.id.btn_calculate_vacation);
            plannedDaysTextView = findViewById(R.id.plannedDaysTextView);

            fetchDurationForCurrentUser();
            destinationViewModel.prepopulateDatabase(finalUserId);
            destinationViewModel.getDestinationEntries().observe(this,
                    entries -> updateDestinationList(entries));
            destinationViewModel.readEntries(finalUserId);
            if (destinationListTextView == null)
                Log.e("DestinationActivity", "destinationListTextView is null");
            if (logTravelButton == null) Log.e("DestinationActivity",
                    "logTravelButton is null");


            logTravelButton.setOnClickListener(v -> {
                if (finalEmail != null) {
                    openLogTravelDialog(finalUserId);
                } else {
                    Toast.makeText(this, "Email is not available. Please try again.",
                            Toast.LENGTH_SHORT).show();
                }
            });

            calculateVacationTime.setOnClickListener(v -> {
                if (finalEmail != null) {
                    openCalculateVacationDialog(finalUserId, finalEmail);
                } else {
                    Toast.makeText(this, "Email is not available. Please try again.",
                            Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Log.e("DestinationActivity", "Error in onCreate", e);
        }
    }

    //makes a destination list
    private void updateDestinationList(List<DestinationEntry> entries) {
        StringBuilder listBuilder = new StringBuilder();
        this.totalDays = 0; // Instead of declaring a new local variable

        for (DestinationEntry entry : entries) {
            long duration = (entry.getEndDate().getTime() - entry.getStartDate().getTime()) / (1000 * 60 * 60 * 24);
            listBuilder.append(entry.getLocation()).append(": ").append(duration).append(" days\n");
            totalDays += duration;
        }
        destinationListTextView.setText(listBuilder.toString());
        updateTotalDays(totalDays);
        saveTripData();

    }

    public interface RetrieveValuesCallback {
        void onResult(ArrayList<String> values);
        void onError(String error);
    }

    private void updateTotalDays(long totalDays) {
        TextView totalDaysTextView = findViewById(R.id.totalDaysTextView);
        totalDaysTextView.setText("Total Days: " + totalDays + " days");
    }

    private void fetchDurationForCurrentUser() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference durationRef = FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(userId).child("entry").child("duration");

            durationRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        duration = dataSnapshot.getValue(Integer.class);
                        plannedDaysTextView.setText("Planned Days: " + duration + " days");
                        saveDurationData();
                    } else {
                        plannedDaysTextView.setText("Planned Days: N/A");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("DestinationActivity", "Failed to fetch duration",
                            databaseError.toException());
                    Toast.makeText(DestinationActivity.this, "Failed to retrieve duration",
                            Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }
    private void saveTripData() {
        TripData tripData = new TripData(totalDays);

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs",
                MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("TOTAL_DAYS_KEY", tripData.getTotalDays());
        Log.d("DestinationActivity",
                "Saving to SharedPreferences: totalDays = " + tripData.getTotalDays());

        editor.apply();
    }

    private void saveDurationData() {
        DurationData durationData = new DurationData (duration);
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("PLANNED_DAYS_KEY", durationData.getDuration());
        Log.d("DestinationActivity",
                "Saving to SharedPreferences: totalDays = " + durationData.getDuration());

        editor.apply();
    }

    private void openLogTravelDialog(String aUserId) {
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
            String location = locationInput.getText().toString().trim();

            if (location.isEmpty()) {
                Toast.makeText(this, "Location cannot be empty",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (!validateViewModel.validateDate(startDate.getTime(), endDate.getTime())) {
                Toast.makeText(this, "End date must be after start date",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            String destinationId = destinationViewModel.generateDestinationId(location,
                    startDate.getTime());
            DestinationEntry newEntry = new DestinationEntry(
                    destinationId,
                    location,
                    startDate.getTime(),
                    endDate.getTime()
            );
            destinationViewModel.addDestination(aUserId, newEntry);
            Toast.makeText(this, "Travel log added", Toast.LENGTH_SHORT).show();
            locationInput.setText("");
            startDateText.setText("");
            endDateText.setText("");
            dialog.dismiss();
            destinationViewModel.prepopulateDatabase(aUserId);
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

        openStartDatePicker2.setOnClickListener(v -> openDatePickerDialog(startDate,
                startDateText2));
        openEndDatePicker2.setOnClickListener(v -> openDatePickerDialog(endDate, endDateText2));

        submitVacationTimeButton.setOnClickListener(v -> {
            int vacationDuration = calculateValues(vacationInput, startDateText2, endDateText2);
            saveData(username, password, vacationDuration, startDateText2, endDateText2);
            fetchDurationForCurrentUser();
        });

        dialog.show();
    }

    private int calculateValues(EditText vacationInput, TextView startDateText, TextView endDateText) {
        int durationDays = 0;
        String resultMessage = null;
        String durationStr = vacationInput.getText().toString().trim();
        Long durationInDays = durationStr.isEmpty() ? null : Long.parseLong(durationStr);

        try {
            if (startDateText.getText().toString().contains("Selected Date")
                    && endDateText.getText().toString().contains("Selected Date")
                    && durationInDays == null) {


                String calculatedDuration = userDurationViewModel
                        .calculateMissingValue(startDate.getTime(), endDate.getTime(),
                                null);
                vacationInput.setText(calculatedDuration);
                resultMessage = "Calculated Duration: " + calculatedDuration + " days";
                durationDays = Integer.parseInt(calculatedDuration);
                Log.d("calculateValues", "Calculated duration from start and end dates: "
                        + durationDays);

            } else if (startDateText.getText().toString().contains("Selected Date")
                    && durationInDays != null) {
                String calculatedEndDate = userDurationViewModel
                        .calculateMissingValue(startDate.getTime(), null, durationInDays);
                endDateText.setText("Calculated End Date: " + calculatedEndDate);
                resultMessage = "Calculated End Date: " + calculatedEndDate;
                durationDays = durationInDays.intValue();
                Log.d("calculateValues",
                        "End date calculated from start date and duration: " + calculatedEndDate);

            } else if (endDateText.getText().toString().contains("Selected Date") && durationInDays != null) {
                String calculatedStartDate = userDurationViewModel
                        .calculateMissingValue(null,
                                endDate.getTime(), durationInDays);
                startDateText.setText("Calculated Start Date: " + calculatedStartDate);
                resultMessage = "Calculated Start Date: " + calculatedStartDate;
                durationDays = durationInDays.intValue();
                Log.d("calculateValues",
                        "Start date calculated from end date and duration: "
                                + calculatedStartDate);

            } else {
                Toast.makeText(this,
                        "Please ensure that at least two of the fields" +
                                " (Start Date, End Date, Duration) are filled.",
                        Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.e("calculateValues", "Error calculating values", e);
            Toast.makeText(this, "Error calculating values. Please check inputs.",
                    Toast.LENGTH_SHORT).show();
        }

        if (resultMessage != null) {
            new AlertDialog.Builder(this)
                    .setTitle("Calculation Result")
                    .setMessage(resultMessage)
                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                    .create()
                    .show();
        }
        clearVacationForm(startDateText, endDateText, vacationInput);
        return durationDays;
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

    private Date convertToDate(TextView input) {
        Date date = new Date();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String dateString = input.getText().toString();
            date = dateFormat.parse(dateString);
            Log.d("DateConversion", "Parsed Date: " + date);
        } catch (ParseException e) {
            Log.e("DateConversion", "Failed to parse date: " + e.getMessage());
        }
        return date;
    }

    private void saveData(String finalUserId, String finalEmail, int vacationDuration,
                          TextView startDateText, TextView endDateText) {
        String vacationId = userDurationViewModel.generateVacationId(vacationDuration, new Date());
        Date startDateVal = new Date();
        Date endDateVal = new Date();
        if (startDateText.getText().toString().contains("Selected Date")
                && endDateText.getText().toString().contains("Selected Date")) {
            startDateVal = convertToDate(startDateText);
            endDateVal = convertToDate(endDateText);
        } else if (startDateText.getText().toString().contains("Selected Date")
                && vacationDuration != 0) {
            String calculatedEndDate = userDurationViewModel
                    .calculateMissingValue(startDate.getTime(),
                            null, (long)vacationDuration);
            endDateText.setText(calculatedEndDate);
            startDateVal = convertToDate(startDateText);
            endDateVal = convertToDate(endDateText);
        } else if (endDateText.getText().toString()
                .contains("Selected Date") && vacationDuration != 0) {
            String calculatedStartDate = userDurationViewModel.calculateMissingValue(null,
                    endDate.getTime(), (long)vacationDuration);
            startDateText.setText(calculatedStartDate);
            startDateVal = convertToDate(startDateText);
            endDateVal = convertToDate(endDateText);
        }
        userDurationViewModel.saveDurationData(finalUserId, finalEmail,
                new DurationEntry(vacationId, vacationDuration, startDateVal, endDateVal));
        Toast.makeText(DestinationActivity.this, "Data saved successfully",
                Toast.LENGTH_SHORT).show();
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
                    dateTextView.setText("Selected Date: " + selectedDay + "/"
                            + (selectedMonth + 1) + "/" + selectedYear);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void fetchPlannedDaysFromFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users")
                .child("No user signed in").child("entry")
                .child("duration");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    long plannedDays = dataSnapshot.getValue(Long.class);
                    plannedDaysTextView.setText("Planned Days: " + plannedDays + " days");
                } else {
                    plannedDaysTextView.setText("Planned Days: N/A");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("DestinationActivity", "Failed to fetch planned days",
                        databaseError.toException());
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        finalEmail = intent.getStringExtra("username");
        finalUserId = intent.getStringExtra("userId");
        destinationViewModel.readEntries(finalUserId);
        Intent sendintent = new Intent(DestinationActivity.this, LogisticsActivity.class);
        sendintent.putExtra("userId", finalUserId);
        sendintent.putExtra("username", finalEmail);
        fetchDurationForCurrentUser();
        destinationViewModel.prepopulateDatabase(finalUserId);
        if (destinationListTextView == null)
            Log.e("DestinationActivity", "destinationListTextView is null");
        if (logTravelButton == null) Log.e("DestinationActivity", "logTravelButton is null");

        destinationViewModel.getDestinationEntries().observe(this,
                entries -> updateDestinationList(entries));
        destinationViewModel.readEntries(finalUserId);

        logTravelButton.setOnClickListener(v -> {
            if (finalEmail != null) {
                openLogTravelDialog(finalUserId);
            } else {
                Toast.makeText(this, "Email is not available. Please try again.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        calculateVacationTime.setOnClickListener(v -> {
            if (finalEmail != null) {
                openCalculateVacationDialog(finalUserId, finalEmail);
            } else {
                Toast.makeText(this, "Email is not available. Please try again.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
