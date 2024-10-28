package com.example.sprintproject.views;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sprintproject.R;

public class AddUserActivity extends AppCompatActivity {

    private EditText emailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_users_form); // Replace 'your_layout_name'
        // with your actual XML layout file name

        // Initialize the EditText and Button
        emailEditText = findViewById(R.id.emailenterable);
        Button addUserButton = findViewById(R.id.adduserbtn);

        // Set an onClickListener on the button to validate email when clicked
        addUserButton.setOnClickListener(v -> validateEmail());
        Button exitButton = findViewById(R.id.exitbtn);

        // Set up the onClickListener for the exit button
        exitButton.setOnClickListener(v -> {
            // Close the current activity and go back to LogisticsActivity
            finish(); // This will go back to the previous activity in the stack
        });

    }

    // Method to validate email format
    private void validateEmail() {
        String email = emailEditText.getText().toString().trim();

        // Check if email field is empty
        if (email.isEmpty()) {
            emailEditText.setError("Email is required");
            return;
        }

        // Check if the email follows the correct format
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Enter a valid email address");
            return;
        }

        // If email is valid, show a success message
        Toast.makeText(this, "Valid email entered!", Toast.LENGTH_SHORT).show();

        // Add further logic here to save the user email if necessary
    }
}
