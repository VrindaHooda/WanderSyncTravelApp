package com.example.sprintproject.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
//import android.widget.Toast;
// Commented out this unused import to comply with Checkstyle
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sprintproject.R;
import com.example.sprintproject.model.DiningReservation;
import com.example.sprintproject.viewmodels.DiningViewModel;
//import com.example.sprintproject.views.DiningReservationAdapter;
// Commented out this unused import to comply with Checkstyle

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class DiningActivity extends AppCompatActivity {

    private DiningViewModel diningViewModel;
    private ProgressBar progressBar;
    private TextView errorTextView;
    private RecyclerView upcomingRecyclerView;
    private RecyclerView pastRecyclerView;
    private DiningReservationAdapter upcomingAdapter;
    private DiningReservationAdapter pastAdapter;
    private Button addReservationButton;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.bottomNavigation, NavigationFragment.class, null)
                    .commit();
        }
        // Set your layout resource
        setContentView(R.layout.activity_dining);
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize UI components
        progressBar = findViewById(R.id.progressBar);
        errorTextView = findViewById(R.id.errorTextView);
        upcomingRecyclerView = findViewById(R.id.upcomingRecyclerView);
        pastRecyclerView = findViewById(R.id.pastRecyclerView);
        addReservationButton = findViewById(R.id.addReservationButton);

        // Setup RecyclerViews
        setupRecyclerViews();

        // Get the ViewModel
        diningViewModel = new ViewModelProvider(this).get(DiningViewModel.class);

        // Observe LiveData from the ViewModel
        observeViewModel();

        // Fetch the reservations for the user
        String userId = firebaseAuth.getCurrentUser().getUid();
        // Implement this method to get the current user's ID

        diningViewModel.fetchCategorizedDiningReservations(userId);

        addReservationButton.setOnClickListener(v -> {
            // Intent to Add Reservation Activity or dialog to add reservation
            Intent intent = new Intent(DiningActivity.this, AddReservationActivity.class);
            startActivity(intent);
        });
    }

    private void setupRecyclerViews() {
        // Upcoming Reservations RecyclerView
        upcomingAdapter = new DiningReservationAdapter();
        upcomingRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        upcomingRecyclerView.setAdapter(upcomingAdapter);

        // Past Reservations RecyclerView
        pastAdapter = new DiningReservationAdapter();
        pastRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        pastRecyclerView.setAdapter(pastAdapter);
    }

    private void observeViewModel() {
        diningViewModel.getIsLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
        });

        diningViewModel.getErrorMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMsg) {
                if (errorMsg != null) {
                    errorTextView.setText(errorMsg);
                    errorTextView.setVisibility(View.VISIBLE);
                } else {
                    errorTextView.setVisibility(View.GONE);
                }
            }
        });

        diningViewModel.getUpcomingReservations().observe(
                this, new Observer<List<DiningReservation>>() {
                    @Override
                    public void onChanged(List<DiningReservation> reservations) {
                        for (DiningReservation reservation : reservations) {
                            if (reservation.getWebsite() == null
                                    || reservation.getWebsite().isEmpty()) {
                                reservation.setWebsite("No website available");
                            }
                            if (reservation.getRating() == 0) {
                                reservation.setRating(3.0f); // Default rating if none provided
                            }
                        }
                        upcomingAdapter.setReservations(reservations);
                    }
                });

        diningViewModel.getPastReservations().observe(
                this, new Observer<List<DiningReservation>>() {
                    @Override
                    public void onChanged(List<DiningReservation> reservations) {
                        for (DiningReservation reservation : reservations) {
                            if (reservation.getWebsite() == null
                                    || reservation.getWebsite().isEmpty()) {
                                reservation.setWebsite("No website available");
                            }
                            if (reservation.getRating() == 0) {
                                reservation.setRating(3.0f); // Default rating if none provided
                            }
                        }
                        pastAdapter.setReservations(reservations);
                    }
                });
    }

    // Add methods to handle user interactions, like deleting a reservation
}
