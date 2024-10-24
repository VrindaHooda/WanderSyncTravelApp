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
        setContentView(R.layout.destination_activity);

        userNameTextView = findViewById(R.id.userNameTextView);
        userAgeTextView = findViewById(R.id.userAgeTextView);

        destinationViewModel = new ViewModelProvider(this).get(DestinationViewModel.class);

        destinationViewModel.getUserLiveData().observe(this, user -> {
            if (user != null) {
                userNameTextView.setText(user.getName());
                userAgeTextView.setText(String.valueOf(user.getAge()));
            }
        });

        String userId = "123";
        destinationViewModel.readUserData(userId);

        User newUser = new User("Alice", 25);
        destinationViewModel.writeUserData(userId, newUser);
    }
}
