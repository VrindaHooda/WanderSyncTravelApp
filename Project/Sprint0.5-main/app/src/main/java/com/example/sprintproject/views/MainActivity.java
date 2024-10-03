package com.example.sprintproject.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sprintproject.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Set the layout for the splash screen

        // Handler to introduce a delay before transitioning to the HomeScreen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Intent to start the HomeScreen activity after the delay
                Intent intent = new Intent(MainActivity.this, HomeScreen.class);
                startActivity(intent);
                finish(); // Close the MainActivity so it won't appear again when pressing back
            }
        }, 3000); // 3 seconds delay for the splash screen
    }
}