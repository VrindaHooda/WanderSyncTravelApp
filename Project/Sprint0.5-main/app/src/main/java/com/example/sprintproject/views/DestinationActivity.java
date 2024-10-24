package com.example.sprintproject.views;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.sprintproject.R;
import com.example.sprintproject.model.User;
import com.example.sprintproject.viewmodels.DestinationViewModel;

public class DestinationActivity extends AppCompatActivity {

    private DestinationViewModel destinationViewModel;
    private TextView userNameTextView, userAgeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.destination_screen);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.bottomNavigation, NavigationFragment.class, null)
                    .commit();
        }
    }

}