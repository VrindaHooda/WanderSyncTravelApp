package com.example.sprintproject.views;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.sprintproject.R;
import com.example.sprintproject.model.DestinationEntry;
import com.example.sprintproject.viewmodels.AuthViewModel;
import com.example.sprintproject.viewmodels.DestinationViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import java.util.ArrayList;
import java.util.List;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class LogisticsActivity extends AppCompatActivity {
    private BarChart barChart;
    private DestinationViewModel destinationViewModel;
    private RecyclerView contributorsRecyclerView;
    private RecyclerView notesRecyclerView;
    private AuthViewModel authViewModel;
    private List<String> contributorsList = new ArrayList<>();
    private List<String> notesList = new ArrayList<>();
    private FloatingActionButton inviteButton;
    private NotesAdapter notesAdapter = new NotesAdapter(notesList);
    private FloatingActionButton notesButton;
    private String destinationId; // Added: To uniquely identify the trip for Firebase operations

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logistics_screen);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.bottomNavigation, NavigationFragment.class, null)
                    .commit();
        }

        // Initialize ViewModels for data handling
        destinationViewModel = new ViewModelProvider(this).get(DestinationViewModel.class);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class); // Handles user authentication

        // Initialize UI elements
        barChart = findViewById(R.id.barChart); // BarChart to visualize allotted vs planned days
        Button graphButton = findViewById(R.id.btn_graph); // Button to trigger data visualization
        inviteButton = findViewById(R.id.invite); // Invite button for inviting contributors
        notesButton = findViewById(R.id.notes); // Notes button for viewing and adding notes

        // Set up RecyclerViews
        contributorsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        notesRecyclerView.setAdapter(notesAdapter);

        // Set up observers for LiveData in DestinationViewModel
        destinationViewModel.getDestinationEntries().observe(this, this::populateBarGraph); // Updates chart based on entries

        // Handle click on "Graph" button to visualize allotted vs planned days
        graphButton.setOnClickListener(v -> {
            // Observing total planned days to update BarChart when button is clicked
            destinationViewModel.getTotalPlannedDays().observe(this, plannedDays -> {
                ArrayList<BarEntry> barEntries = new ArrayList<>();
                barEntries.add(new BarEntry(0, destinationViewModel.getTotalAllottedDays())); // Allotted days bar
                barEntries.add(new BarEntry(1, plannedDays)); // Planned days bar

                // Set up and display BarChart data
                BarDataSet dataSet = new BarDataSet(barEntries, "Days");
                BarData barData = new BarData(dataSet);
                barChart.setData(barData);

                // Customize X-axis and refresh chart
                XAxis xAxis = barChart.getXAxis();
                xAxis.setGranularity(1f);
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                barChart.invalidate(); // Refresh chart
            });

            // Ensure data is loaded
            destinationViewModel.readEntries(); // Load entries from Firebase
        });

        // Observe changes to the destination entries LiveData
        destinationViewModel.getDestinationEntries().observe(this, new Observer<List<DestinationEntry>>() {
            @Override
            public void onChanged(List<DestinationEntry> entries) {
                // When data changes, update the bar graph
                populateBarGraph(entries);
            }
        });
        // Read entries from the database to initialize the data
        destinationViewModel.readEntries();

        // Set up Invite functionality to invite other users to collaborate
        inviteButton.setOnClickListener(v -> openInviteDialog());

        // Set up Notes functionality for users to view and add notes
        notesButton.setOnClickListener(v -> openNotesDialog());

        // Observe contributors and notes lists to update UI dynamically
        destinationViewModel.getContributors().observe(this, contributors -> updateContributorsList(contributors));
        destinationViewModel.getNotes().observe(this, notes -> updateNotesList(notes));
    }

    /**
     * Populate the BarChart with data from the list of DestinationEntries.
     * @param entries List of DestinationEntry objects fetched from the database.
     */
    private void populateBarGraph(List<DestinationEntry> entries) {
        ArrayList<BarEntry> barEntries = new ArrayList<>();

        // Calculate total planned days from entries
        long totalPlannedDays = 0;
        for (DestinationEntry entry : entries) {
            totalPlannedDays += entry.getDurationInDays();
        }

        // Create bar entries for allotted and planned days
        barEntries.add(new BarEntry(0, destinationViewModel.getTotalAllottedDays()));
        barEntries.add(new BarEntry(1, totalPlannedDays));

        // Create a BarDataSet from the bar entries and set the label for the dataset
        BarDataSet dataSet = new BarDataSet(barEntries, "Total Duration");
        // Create BarData from the dataset
        BarData barData = new BarData(dataSet);
        barChart.setData(barData); // Set the BarData to the BarChart

        // Customize X-Axis (optional)
        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f); // Ensure labels are spaced correctly
        xAxis.setLabelCount(1); // Set only one label
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Position labels at the bottom
        xAxis.setDrawGridLines(false); // Remove grid lines for clarity

        // Customize Y-Axis (optional)
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setGranularity(1f); // Set the minimum difference between values
        YAxis rightAxis = barChart.getAxisRight();
        barChart.getAxisRight().setEnabled(false);
        barChart.getDescription().setEnabled(false);
        rightAxis.setEnabled(false); // Disable the right Y-axis for a cleaner look

        // Disable chart description
        barChart.getDescription().setEnabled(false);

        // Redraw the chart
        barChart.invalidate(); // Refresh the chart to display the data
    }

    /**
     * Opens a dialog to invite a user by email to contribute to the trip.
     */
    private void openInviteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Invite a User to Join the Trip");

        final EditText input = new EditText(this);
        input.setHint("Enter user's email");
        builder.setView(input);

        builder.setPositiveButton("Invite", (dialog, which) -> {
            String email = input.getText().toString().trim();
            if (!email.isEmpty()) {
                FirebaseUser currentUser = authViewModel.getCurrentUser();
                if (currentUser != null) {
                    String userId = currentUser.getUid();
                    destinationViewModel.addContributor(destinationId, userId, email);
                    Toast.makeText(this, "Invitation sent to " + email, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Please log in to invite users", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    // Method to simulate sending an invitation (would typically involve Firebase database)
    private void sendInvitation(String email) {
        // For now, add to contributors list and show a confirmation
        if (!contributorsList.contains(email)) {
            contributorsList.add(email);
            Toast.makeText(this, "Invitation sent to " + email, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "User is already a contributor", Toast.LENGTH_SHORT).show();
        }
    }

    private void openNotesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Trip Notes");
        builder.setView(notesRecyclerView); // Use notes RecyclerView to display notes
        // Set up RecyclerView for notes
        RecyclerView recyclerView = new RecyclerView(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(notesAdapter);

        builder.setView(recyclerView);

        builder.setPositiveButton("Add Note", (dialog, which) -> {
            openAddNoteDialog();
        });

        builder.setNegativeButton("Close", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void openAddNoteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add a New Note");

        final EditText noteInput = new EditText(this);
        noteInput.setHint("Enter your note");
        builder.setView(noteInput);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String noteText = noteInput.getText().toString().trim();
            if (!noteText.isEmpty()) {
                destinationViewModel.addNoteToTrip(destinationId, noteText); // Use destinationId to add note
                addNoteToDatabase(noteText);
            } else {
                Toast.makeText(this, "Note cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    // Method to add a note (could be saved to Firebase or local storage)
    private void addNoteToDatabase(String noteText) {
        // For now, just add to the notes list and refresh adapter
        notesList.add(noteText);
        notesAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Note added successfully", Toast.LENGTH_SHORT).show();
    }
}
