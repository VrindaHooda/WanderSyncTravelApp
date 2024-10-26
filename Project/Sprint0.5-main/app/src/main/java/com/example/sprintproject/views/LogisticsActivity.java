package com.example.sprintproject.views;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.sprintproject.R;
import com.example.sprintproject.model.DestinationEntry;
import com.example.sprintproject.viewmodels.DestinationViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import java.util.ArrayList;
import java.util.List;
import com.github.mikephil.charting.data.BarEntry;

public class LogisticsActivity extends AppCompatActivity {
    private BarChart barChart;
    private DestinationViewModel destinationViewModel;
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

        destinationViewModel = new ViewModelProvider(this).get(DestinationViewModel.class);
        barChart = findViewById(R.id.barChart);
        Button graphButton = findViewById(R.id.btn_graph);

        // Set a click listener on the button to create the graph when clicked
        graphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Read entries from the database and update the chart when the button is clicked
                destinationViewModel.getDestinationEntries().observe(LogisticsActivity.this, new Observer<List<DestinationEntry>>() {
                    @Override
                    public void onChanged(List<DestinationEntry> entries) {
                        populateBarGraph(entries); // Populate the graph with the data
                    }
                });
                destinationViewModel.readEntries(); // Load entries from the database
            }
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
    }

    /**
     * Populate the BarChart with data from the list of DestinationEntries.
     * @param entries List of DestinationEntry objects fetched from the database.
     */
    private void populateBarGraph(List<DestinationEntry> entries) {
        float totalDuration = 0f; // Variable to store the total duration

        // Loop through the entries to sum up the durations
        for (DestinationEntry entry : entries) {
            totalDuration += entry.getDurationInDays(); // Sum up the durations
        }

        // Create a single BarEntry for the total duration
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(0, totalDuration)); // Add a single bar with the total duration

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
        rightAxis.setEnabled(false); // Disable the right Y-axis for a cleaner look

        // Disable chart description
        barChart.getDescription().setEnabled(false);

        // Redraw the chart
        barChart.invalidate(); // Refresh the chart to display the data
    }
}
