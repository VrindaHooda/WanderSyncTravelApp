package com.example.sprintproject.views;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sprintproject.R;

public class TravelCommunityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.travel_community_screen);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.bottomNavigationView, NavigationFragment.class, null)
                    .commit();
        }
    }
}