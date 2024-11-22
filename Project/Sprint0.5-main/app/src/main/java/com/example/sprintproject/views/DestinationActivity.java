package com.example.sprintproject.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sprintproject.R;

public class DestinationActivity extends AppCompatActivity implements DestinationFragment.OnDataPassListener {

    private int plannedDays;
    private int pastDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.bottomNavigation, NavigationFragment.class, null)
                    .commit();
        }

        // Add DestinationFragment to this activity
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new DestinationFragment())
                    .commit();
        }
    }

    @Override
    public void onPastDataPass(int data) {
        pastDays = data;
        Log.d("Data Passed", "Past Days: " + data);
    }

    @Override
    public void onPlannedDataPass(int data) {
        plannedDays = data;
        Log.d("Data Passed", "Planned Days: " + data);
    }

    // Method to be called when the fragment navigates to the Logistics screen
    public void navigateToLogisticsActivity() {
        Intent intent = new Intent(DestinationActivity.this, LogisticsActivity.class);
        intent.putExtra("planned_days_key", plannedDays);
        intent.putExtra("past_days_key", pastDays);
        startActivity(intent);
    }
}
