package com.example.sprintproject.views;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sprintproject.R;
import com.example.sprintproject.viewmodels.ContributorAdapter;

import java.util.Arrays;
import java.util.List;


public class LogisticsActivity extends AppCompatActivity {
    private RecyclerView contributorsRecyclerView;
    private ContributorAdapter adapter;
    private List<String> contributorList;

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
    }



}