package com.example.sprintproject.views;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sprintproject.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
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
        viewInvites = findViewById(R.id.viewInvites);

        // Initially hide the pie chart
        pieChart.setVisibility(View.GONE);

        Intent intent = getIntent();
        int plannedDays = intent.getIntExtra("planned_days_key", 0);
        int pastDays = intent.getIntExtra("past_days_key", 0);
        Log.d("Data", "past:" + pastDays);
        Log.d("Data", "planned:" + plannedDays);


        // Set up click listener for Show Graph button
        btnGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the pie chart
                pieChart.setVisibility(View.VISIBLE);
                setupPieChart(plannedDays, pastDays);
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


        viewInvites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogisticsActivity.this, ViewInvitesActivity.class);
                startActivity(intent);
            }
        });

    }

    private void setupPieChart(int plannedDays, int pastDays) {

        // Create dummy data for the pie chart
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(plannedDays, "Planned Days"));
        entries.add(new PieEntry(pastDays, "Past Travel Days"));

        PieDataSet dataSet = new PieDataSet(entries, "Travel Plans");

        // Customize the data set
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS); // Set colors from the Material Design color palette
        dataSet.setValueTextSize(14f); // Set value text size
        dataSet.setValueTextColor(Color.WHITE); // Set value text color to white
        dataSet.setSliceSpace(3f); // Add space between slices for better readability
        dataSet.setSelectionShift(10f); // Highlight size when a slice is selected

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter()); // Display values as percentages

        // Customize the pie chart
        pieChart.setData(data);
        pieChart.setUsePercentValues(true); // Use percentage values
        pieChart.setDrawHoleEnabled(true); // Enable hole in the center
        pieChart.setHoleRadius(40f); // Set radius of the hole
        pieChart.setCenterText("Travel Plan"); // Set center text
        pieChart.setCenterTextSize(16f); // Set center text size
        pieChart.setCenterTextColor(Color.BLACK); // Set center text color
        pieChart.getDescription().setEnabled(false); // Remove description label
        pieChart.getLegend().setEnabled(true); // Enable legend
        pieChart.getLegend().setTextSize(14f); // Set legend text size
        pieChart.getLegend().setTextColor(Color.BLACK); // Set legend text color
        pieChart.getLegend().setOrientation(Legend.LegendOrientation.HORIZONTAL); // Set legend orientation
        pieChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER); // Center legend horizontally

        // Display at the very bottom after the color codes
        pieChart.invalidate(); // Refresh the chart
    }


}
