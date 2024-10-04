package com.example.sprintproject.views;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sprintproject.R;

public class LogisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logistics_screen); // Load the logistics screen layout
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.bottomNavigationView, NavigationFragment.class, null)
                    .commit();
        }
    }
}
