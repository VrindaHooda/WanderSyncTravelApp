package com.example.sprintproject.views;

import android.content.Intent;



import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.sprintproject.R;

import androidx.lifecycle.ViewModelProvider;


import android.util.Log;

import android.widget.Button;

import com.example.sprintproject.model.ContributorEntry;
import android.widget.ListView;

import com.example.sprintproject.viewmodels.AuthViewModel;
import com.example.sprintproject.viewmodels.DestinationViewModel;
import com.example.sprintproject.viewmodels.UserDurationViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import java.util.List;


public class LogisticsActivity extends AppCompatActivity {

    private PieChart pieChart;
    private long totalDays = 15; // Example initial value
    private long secondDays = 8; // Example initial value for testing
    private AuthViewModel authViewModel;
    private DestinationViewModel destinationViewModel;
    private UserDurationViewModel userDurationViewModel;


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


        //        FloatingActionButton addANote = findViewById(R.id.modify_notes);
        //        addANote.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                addANote();
        //            }
        //        });


        pieChart = findViewById(R.id.pieChart);
        Button updateButton = findViewById(R.id.btn_graph);

        // Initial display of the chart
        updatePieChart();
        //        // Set up button click to refresh the chart with updated values
        //        updateButton.setOnClickListener(v -> updatePieChart());
        //        FloatingActionButton inviteButton = findViewById(R.id.invite);
        //        inviteButton.setOnClickListener(v -> {
        //            Intent intent = new Intent(LogisticsActivity.this, AddUserActivity.class);
        //            startActivity(intent);
        //        });
        //        FloatingActionButton viewInvitesButton = findViewById(R.id.view_invites);
        //        viewInvitesButton.setOnClickListener(v -> {
        //            Intent intent = new Intent(LogisticsActivity.this, ViewInvitesActivity.class);
        //            startActivity(intent);
        //        });
        //
        //        FloatingActionButton viewNotesButton = findViewById(R.id.view_notes);
        //        viewNotesButton.setOnClickListener(v -> {
        //            Intent intent = new Intent(LogisticsActivity.this, ViewNotesActivity.class);
        //            startActivity(intent);
        //        });

        // Method to get LiveData from UI

        Intent intent = new Intent(LogisticsActivity.this, DestinationActivity.class);
        intent.putParcelableArrayListExtra("contributorsList", getAllContributors());
        startActivity(intent);

        //        Intent intent = getIntent();
        try {
            destinationViewModel = new ViewModelProvider(this).get(DestinationViewModel.class);
            userDurationViewModel = new ViewModelProvider(this).get(UserDurationViewModel.class);
            authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);



            Button modifyNotes = findViewById(R.id.modify_notes);
            Button viewNotes = findViewById(R.id.view_notes);
            Button modifyPlans = findViewById(R.id.modify_plans);
            Button viewInvites = findViewById(R.id.view_invites);
            Button invite = findViewById(R.id.invite);

            ListView contributorsListView = findViewById(R.id.contributorsListView);

            if (contributorsListView == null) {
                Log.e("LogisticsActivity", "contributorsListView is null");

            }
            //            destinationViewModel.getDestinationEntries().observe(this,
            //            entries -> updateContributorsList(entries));
            destinationViewModel.prepopulateDatabase();
            destinationViewModel.readEntries();

        //            //bruh
        //            viewNotes.setOnClickListener(v -> viewTheNotes());
        //            modifyPlans.setOnClickListener(v -> modifyTrip());
        //            viewInvites.setOnClickListener(v -> viewTheInvites());
        //            invite.setOnClickListener(v -> inviteContributor(contributor));


        } catch (Exception e) {
            Log.e("LogisticsActivity", "Error in onCreate", e);
        }
    }

    //    private void addANote() {
    //        // Create an EditText field for user input
    //        final EditText input = new EditText(this);
    //        input.setHint("Type in your note here");
    //
    //        // Create the dialog
    //        AlertDialog.Builder builder = new AlertDialog.Builder(this);
    //        builder.setTitle("Add A Note")
    //                .setView(input)  // Set the EditText field in the dialog
    //                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
    //                    @Override
    //                    public void onClick(DialogInterface dialog, int which) {
    //                        String newPlan = input.getText().toString().trim();
    //                        if (!newPlan.isEmpty()) {
    //                            // Save or process the entered plan here
    //                            Toast.makeText(LogisticsActivity.this, "Note
    //                            saved: " + newPlan, Toast.LENGTH_SHORT).show();
    //                        } else {
    //                            Toast.makeText(LogisticsActivity.this, "Please a
    //                            dd a note.", Toast.LENGTH_SHORT).show();
    //                        }
    //                    }
    //                })
    //                .setNegativeButton("Cancel", new DialogInterface.OnClickLis
    //                tener() {
    //                    @Override
    //                    public void onClick(DialogInterface dialog, int which) {
    //                        dialog.dismiss(); // Dismiss the dialog
    //                    }
    //                });
    //
    //        AlertDialog dialog = builder.create();
    //        dialog.show();
    //    }

    private void updatePieChart() {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(totalDays, "Total Days"));
        entries.add(new PieEntry(secondDays, "Second Days"));

        PieDataSet dataSet = new PieDataSet(entries, "Days");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);

        pieChart.setData(data);
        pieChart.invalidate(); // Refreshes the chart
    }
    private LiveData<ContributorEntry> getContributorLiveDataFromUI() {
        // Logic to get the LiveData from your UI (for example, from a form or input fields)
        // You might have a MutableLiveData that you update based on user actions
        MutableLiveData<ContributorEntry> liveData = new MutableLiveData<>();
        // Example: Update liveData based on user input
        ContributorEntry entry = new ContributorEntry("", "exampleNote");
        // Create a ContributorEntry based on user input
        liveData.setValue(entry); // Update LiveData with the new entry
        return liveData;
    }
    public ContributorEntry convertLiveData(LiveData<ContributorEntry>
                                                    contributorEntryLiveData) {
        ContributorEntry contributorEntry = contributorEntryLiveData.getValue();
        return contributorEntry;
    }
    public ArrayList<ContributorEntry> getAllContributors() {
        ArrayList<ContributorEntry> listOfContributors = new ArrayList<>();
        listOfContributors.add(convertLiveData(getContributorLiveDataFromUI()));
        return listOfContributors;
    }

}