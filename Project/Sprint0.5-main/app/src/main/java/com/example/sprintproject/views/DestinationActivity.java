package com.example.sprintproject.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import com.example.sprintproject.databinding.DestinationScreenBinding;
import com.example.sprintproject.R;
import com.example.sprintproject.model.DestinationEntry;
import com.example.sprintproject.viewmodels.AuthViewModel;
import com.example.sprintproject.viewmodels.DestinationViewModel;
import com.example.sprintproject.viewmodels.UserDurationViewModel;
import com.example.sprintproject.viewmodels.ValidateViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.List;

public class DestinationActivity extends AppCompatActivity {

    private AuthViewModel authViewModel;
    private DestinationViewModel destinationViewModel;
    private UserDurationViewModel userDurationViewModel;
    private ValidateViewModel validateViewModel;
    private DestinationScreenBinding binding; // Data binding object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize data binding
        binding = DataBindingUtil.setContentView(this, R.layout.destination_screen);

        // Initialize ViewModels and bind them to the layout
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        destinationViewModel = new ViewModelProvider(this).get(DestinationViewModel.class);
        userDurationViewModel = new ViewModelProvider(this).get(UserDurationViewModel.class);
        validateViewModel = new ViewModelProvider(this).get(ValidateViewModel.class);

        // Set ViewModel bindings in layout
        binding.setAuthViewModel(authViewModel);
        binding.setDestinationViewModel(destinationViewModel);
        binding.setUserDurationViewModel(userDurationViewModel);
        binding.setValidateViewModel(validateViewModel);
        binding.setLifecycleOwner(this); // Observe LiveData in layout

        // Retrieve intent extras
        Intent intent = getIntent();
        String finalEmail = intent.getStringExtra("username");
        String finalUserId = intent.getStringExtra("userId");

        // Use binding to access views directly
        destinationViewModel.prepopulateDatabase(finalUserId);
        destinationViewModel.getDestinationEntries().observe(this, entries -> updateDestinationList(entries));
        destinationViewModel.readEntries(finalUserId);

        // Set button click listeners using binding
        binding.btnLogTravel.setOnClickListener(v -> openLogTravelDialog(finalUserId));
        binding.btnCalculateVacation.setOnClickListener(v -> openCalculateVacationDialog(finalUserId, finalEmail));

        // Fetch and display current user's duration
        fetchDurationForCurrentUser();
    }

    private void updateDestinationList(List<DestinationEntry> entries) {
        StringBuilder listBuilder = new StringBuilder();
        int totalDays = 0;

        for (DestinationEntry entry : entries) {
            long duration = (entry.getEndDate().getTime() - entry.getStartDate().getTime()) / (1000 * 60 * 60 * 24);
            listBuilder.append(entry.getLocation()).append(": ").append(duration).append(" days\n");
            totalDays += duration;
        }
        binding.destinationListTextView.setText(listBuilder.toString());
        updateTotalDays(totalDays);
        saveTripData();
    }

    private void updateTotalDays(int totalDays) {
        binding.totalDaysTextView.setText("Total Days: " + totalDays + " days");
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
                        int duration = dataSnapshot.getValue(Integer.class);
                        binding.plannedDaysTextView.setText("Planned Days: " + duration + " days");
                        saveDurationData(duration);
                    } else {
                        binding.plannedDaysTextView.setText("Planned Days: N/A");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("DestinationActivity", "Failed to fetch duration", databaseError.toException());
                    Toast.makeText(DestinationActivity.this, "Failed to retrieve duration", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveTripData() {
    }

    private void saveDurationData(int duration) {
        // Save duration data logic as before
    }

    private void openLogTravelDialog(String aUserId) {
        // Existing logic for opening log travel dialog
    }

    private void openCalculateVacationDialog(String username, String password) {
        // Existing logic for opening calculate vacation dialog
    }
}
