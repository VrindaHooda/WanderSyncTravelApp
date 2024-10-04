package com.example.sprintproject.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sprintproject.R;
import com.example.sprintproject.viewmodels.AuthViewModel;
import com.example.sprintproject.viewmodels.ValidateViewModel;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private ValidateViewModel validateViewModel;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        validateViewModel = new ValidateViewModel();
        authViewModel = new AuthViewModel();

        EditText usernameEditText = findViewById(R.id.usernameEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        Button loginButton = findViewById(R.id.loginButton);
        Button createAccountButton = findViewById(R.id.createAccountButton);

        // Observe authentication status from ViewModel
        authViewModel.getAuthenticationStatus().observe(this, isAuthenticated -> {
            if (isAuthenticated) {
                // Navigate to LogisticsActivity if login is successful
                Toast.makeText(Login.this, "Login successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Login.this, LogisticsActivity.class);
                startActivity(intent);
            } else {
                // Show error message if login failed
                Toast.makeText(Login.this, "Login failed. Incorrect credentials.", Toast.LENGTH_SHORT).show();
            }
        });
        // Login button action
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (validateViewModel.validateLogin(username, password)) {
                    authViewModel.checkCurrentUser();

                    // Use a callback to handle the result of the signIn method
                    authViewModel.signIn(username, password, new AuthViewModel.Callback() {
                        @Override
                        public void onSuccess(FirebaseUser user) {
                            // Handle successful sign-in
                            Toast.makeText(Login.this, "Login successful! User ID: " + user.getUid(), Toast.LENGTH_SHORT).show();

                            // Navigate to the next activity or perform any other actions upon success
                            Intent intent = new Intent(Login.this, LogisticsActivity.class);
                            startActivity(intent);
                            finish(); // Close the Login activity
                        }

                        @Override
                        public void onFailure(String error) {
                            // Handle failed sign-in
                            Toast.makeText(Login.this, "Login failed: " + error, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(Login.this, "Invalid input. Can't be empty or contain whitespace", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Create Account button action
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, CreateAccount.class);
                startActivity(intent);
            }
        });

    }
}

