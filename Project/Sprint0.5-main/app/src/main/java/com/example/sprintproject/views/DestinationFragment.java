package com.example.sprintproject.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sprintproject.R;
import com.example.sprintproject.viewmodels.DestinationViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class DestinationFragment extends Fragment {
    private DestinationViewModel destinationViewModel;
    private RecyclerView destinationRecyclerView;
    private DestinationAdapter destinationAdapter;
    private Button logPastTravelButton;
    private Button calculateVacationButton;
    private TextView totalTravelDaysTextView;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_destination, container, false);

        // Initialize Firebase Auth and Firestore
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Initialize Views
        destinationRecyclerView = rootView.findViewById(R.id.destinationRecyclerView);
        logPastTravelButton = rootView.findViewById(R.id.logPastTravelButton);
        calculateVacationButton = rootView.findViewById(R.id.calculateVacationButton);
        totalTravelDaysTextView = rootView.findViewById(R.id.totalTravelDaysTextView);

        // Set up RecyclerView
        destinationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        destinationAdapter = new DestinationAdapter();
        destinationRecyclerView.setAdapter(destinationAdapter);

        // Set up ViewModel
        destinationViewModel = new ViewModelProvider(this).get(DestinationViewModel.class);
        observeViewModel();

        // Set up Button Click Listeners
        logPastTravelButton.setOnClickListener(v -> openLogPastTravelForm());
        calculateVacationButton.setOnClickListener(v -> openCalculateVacationForm());

        return rootView;
    }

    private void observeViewModel() {
        String userId = firebaseAuth.getCurrentUser().getUid();
        destinationViewModel.getLastFiveTravelLogs(userId).observe(getViewLifecycleOwner(), travelLogs -> {
            // Update RecyclerView with the last five travel logs
            if (travelLogs != null) {
                destinationAdapter.setTravelLogs(travelLogs);
                destinationAdapter.notifyDataSetChanged();
            }
        });

        destinationViewModel.getTotalTravelDays(userId).observe(getViewLifecycleOwner(), totalDays -> {
            // Update TextView with the total travel days
            totalTravelDaysTextView.setText(getString(R.string.total_travel_days, totalDays));
        });
    }

    private void openLogPastTravelForm() {
        // Open a form dialog to log past travel
        LogPastTravelDialog logPastTravelDialog = new LogPastTravelDialog();
        logPastTravelDialog.setOnSaveListener(travelLog -> {
            String userId = firebaseAuth.getCurrentUser().getUid();
            destinationViewModel.logPastTravel(userId, travelLog);
            // Refresh the list immediately after saving
            refreshTravelLogs(userId);
        });
        logPastTravelDialog.show(getChildFragmentManager(), "LogPastTravelDialog");
    }

    private void openCalculateVacationForm() {
        // Open a form dialog to calculate vacation time
        CalculateVacationDialog calculateVacationDialog = new CalculateVacationDialog();
        calculateVacationDialog.setOnSaveListener(vacationEntry -> {
            String userId = firebaseAuth.getCurrentUser().getUid();
            destinationViewModel.saveVacationEntry(userId, vacationEntry);
        });
        calculateVacationDialog.setOnCalculateListener((startDate, endDate, duration) -> {
            // Calculate the missing value
            destinationViewModel.calculateVacationTime(startDate, endDate, duration);
        });
        calculateVacationDialog.show(getChildFragmentManager(), "CalculateVacationDialog");
    }

    private void refreshTravelLogs(String userId) {
        destinationViewModel.getLastFiveTravelLogs(userId).observe(getViewLifecycleOwner(), travelLogs -> {
            // Update RecyclerView with the refreshed travel logs
            if (travelLogs != null) {
                destinationAdapter.setTravelLogs(travelLogs);
                destinationAdapter.notifyDataSetChanged();
            }
        });
    }
}
