package com.example.sprintproject.views;
import com.github.mikephil.charting.formatter.ValueFormatter;
import android.content.Intent;

import android.content.DialogInterface;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sprintproject.R;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import java.util.List;


public class LogisticsActivity extends AppCompatActivity {

    private PieChart pieChart;
    private int totalDays; // Example initial value
    private int duration; // Example initial value for testing


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
        pieChart = findViewById(R.id.pieChart);
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        totalDays = sharedPreferences.getInt("PLANNED_DAYS_KEY", 0);  // Should match the key in DestinationActivity
        duration = sharedPreferences.getInt("ALLOTTED_DAYS_KEY", 0);  // Should match the key in DestinationActivity

        Log.d("LogisticsActivity", "Retrieved totalDays: " + totalDays + ", duration: " + duration);


        FloatingActionButton modifyPlansButton = findViewById(R.id.modify_notes);
        modifyPlansButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showModifyPlansDialog();
            }
        });



        Button updateButton = findViewById(R.id.btn_graph);

        // Initial display of the chart
        updatePieChart();

        // Set up button click to refresh the chart with updated values
        updateButton.setOnClickListener(v -> updatePieChart());
        FloatingActionButton inviteButton = findViewById(R.id.invite);
        inviteButton.setOnClickListener(v -> {
            Intent intent = new Intent(LogisticsActivity.this, AddUserActivity.class);
            startActivity(intent);
        });
        FloatingActionButton viewInvitesButton = findViewById(R.id.view_invites);
        viewInvitesButton.setOnClickListener(v -> {
            Intent intent = new Intent(LogisticsActivity.this, ViewInvitesActivity.class);
            startActivity(intent);
        });

        FloatingActionButton viewNotesButton = findViewById(R.id.view_notes);
        viewNotesButton.setOnClickListener(v -> {
            Intent intent = new Intent(LogisticsActivity.this, ViewNotesActivity.class);
            startActivity(intent);
        });

    }
    private void showModifyPlansDialog() {
        // Create an EditText field for user input
        final EditText input = new EditText(this);
        input.setHint("Type in your note here");

        // Create the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add A Note")
                .setView(input)  // Set the EditText field in the dialog
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newPlan = input.getText().toString().trim();
                        if (!newPlan.isEmpty()) {
                            // Save or process the entered plan here
                            Toast.makeText(LogisticsActivity.this, "Note saved: " + newPlan, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LogisticsActivity.this, "Please add a note.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // Dismiss the dialog
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updatePieChart() {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(totalDays, "Total Days: " + totalDays));
        entries.add(new PieEntry(duration, "Planned Days: " + duration));

        PieDataSet dataSet = new PieDataSet(entries, "Trip Days Overview");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData data = new PieData(dataSet);


        data.setDrawValues(true);
        pieChart.setUsePercentValues(true); // Optional: Display values as percentages
        pieChart.setDrawHoleEnabled(false);

        pieChart.setData(data);
        pieChart.setUsePercentValues(false);
        pieChart.invalidate(); // Refreshes the chart
    }

}