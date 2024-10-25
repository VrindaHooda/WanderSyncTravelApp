package com.example.sprintproject.views;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sprintproject.R;

public class EnterDestinationActivity extends AppCompatActivity {

    private EditText destinationEditText;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_destination);

        destinationEditText = findViewById(R.id.destinationEditText);
        saveButton = findViewById(R.id.saveDestinationButton);

        saveButton.setOnClickListener(v -> {
            String destination = destinationEditText.getText().toString();
            if (!destination.isEmpty()) {
                // Save the destination (for now, just show a toast)
                Toast.makeText(EnterDestinationActivity.this, "Destination Saved: " + destination, Toast.LENGTH_SHORT).show();
                finish(); // Close the activity
            } else {
                Toast.makeText(EnterDestinationActivity.this, "Please enter a destination", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
