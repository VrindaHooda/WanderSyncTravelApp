package com.example.sprintproject.views;// Import necessary packages
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sprintproject.R;

public class HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);

        // Find the Quit button by its ID
        Button quitButton = findViewById(R.id.quit_button);

        // Set an OnClickListener for the Quit button
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to return to the home screen of the device
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); // Optionally close the current activity
            }
        });

        // You can add more functionality here for other buttons or actions in the HomeScreen
        // Example: Start button functionality (if it exists)
        // Button startButton = findViewById(R.id.start_button);
        // startButton.setOnClickListener(new View.OnClickListener() {
        //     @Override
        //     public void onClick(View v) {
        //         // Start a new activity or any other action
        //     }
        // });
    }

    // Other methods related to the HomeScreen activity can go here
}