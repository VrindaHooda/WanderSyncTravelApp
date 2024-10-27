package com.example.sprintproject.views;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sprintproject.R;
import com.example.sprintproject.viewmodels.ContributorAdapter;

import java.util.Arrays;

import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sprintproject.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import java.util.List;


public class LogisticsActivity extends AppCompatActivity {
  
    private RecyclerView contributorsRecyclerView;
    private ContributorAdapter adapter;
    private List<String> contributorList;


    private PieChart pieChart;
    private long totalDays = 15; // Example initial value
    private long secondDays = 8; // Example initial value for testing


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

        // Initialize the list with some example data
        contributorList = Arrays.asList("Contributor 1", "Contributor 2", "Contributor 3");

        // Set up RecyclerView
        contributorsRecyclerView = findViewById(R.id.contributorsRecyclerView);
        contributorsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ContributorAdapter(this, contributorList);
        contributorsRecyclerView.setAdapter(adapter);

        pieChart = findViewById(R.id.pieChart);
        Button updateButton = findViewById(R.id.btn_graph);

        // Initial display of the chart
        updatePieChart();

        // Set up button click to refresh the chart with updated values
        updateButton.setOnClickListener(v -> updatePieChart());
    }

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

    // A method to update totalDays and secondDays, typically by querying the database
    private void fetchUpdatedDaysFromDatabase() {
        // Example method, replace this with actual database fetching code
        // totalDays = fetchTotalDaysFromDatabase();
        // secondDays = fetchSecondDaysFromDatabase();
        updatePieChart(); // Refresh chart after updating values
    }



}