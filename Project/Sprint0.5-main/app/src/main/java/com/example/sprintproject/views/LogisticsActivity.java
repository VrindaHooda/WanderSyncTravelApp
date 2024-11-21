package com.example.sprintproject.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sprintproject.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.widget.Button;
import java.util.ArrayList;

public class LogisticsActivity extends AppCompatActivity {

    private PieChart pieChart;
    private Button btnGraph;
    private FloatingActionButton makePlan, modifyPlan, viewInvites;

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

        // Initialize views
        pieChart = findViewById(R.id.pieChart);
        btnGraph = findViewById(R.id.btn_graph);
        makePlan = findViewById(R.id.makePlan);
        modifyPlan = findViewById(R.id.modifyPlan);
        viewInvites = findViewById(R.id.viewInvites);

        // Initially hide the pie chart
        pieChart.setVisibility(View.GONE);

        // Set up click listener for Show Graph button
        btnGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the pie chart
                pieChart.setVisibility(View.VISIBLE);
                setupPieChart();
            }
        });

        // Set up click listeners for floating action buttons
        makePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Make Plan screen
                Intent intent = new Intent(LogisticsActivity.this, MakePlansActivity.class);
                startActivity(intent);
            }
        });

        modifyPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Implement functionality for modifying a plan
            }
        });

        viewInvites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Implement functionality for viewing invites
            }
        });
    }

    private void setupPieChart() {
        // Create dummy data for the pie chart
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(40f, "Logistics"));
        entries.add(new PieEntry(30f, "Planning"));
        entries.add(new PieEntry(30f, "Invites"));

        PieDataSet dataSet = new PieDataSet(entries, "Logistics Data");
        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.invalidate(); // refresh the chart
    }
}
