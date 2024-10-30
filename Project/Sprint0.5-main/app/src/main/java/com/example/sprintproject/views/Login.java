package com.example.sprintproject.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sprintproject.R;
import com.example.sprintproject.viewmodels.AuthViewModel;
import com.example.sprintproject.viewmodels.ValidateViewModel;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private final ValidateViewModel validateViewModel = new ValidateViewModel();
    private final AuthViewModel authViewModel = new AuthViewModel();

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button createAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        createAccountButton = findViewById(R.id.createAccountButton);
    }

    private void setupListeners() {
        loginButton.setOnClickListener(view -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (validateViewModel.validateLogin(username, password)) {
                authViewModel.signIn(username, password, new AuthViewModel.AuthCallback() {
                    @Override
                    public void onSuccess(FirebaseUser user) {
                        Toast.makeText(Login.this, "Login successful!", Toast.LENGTH_SHORT).show();
                        String userId = authViewModel.getUser().getUid();
                        String username = authViewModel.getUser().getEmail();
                        Intent intent1 = new Intent(Login.this, DestinationActivity.class);
                        intent1.putExtra("userId", userId);
                        intent1.putExtra("username", username);
                        startActivity(intent1);
                        finish();
                    }

                    @Override
                    public void onFailure(String error) {
                        Toast.makeText(Login.this, "Login failed: " + error,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(Login.this, "Invalid input. Can't be empty or contain "
                        + "whitespace", Toast.LENGTH_SHORT).show();
            }
        });

        createAccountButton.setOnClickListener(view -> {
            Intent intent = new Intent(Login.this, CreateAccount.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (authViewModel.isAuthenticated()) {
            Toast.makeText(Login.this, "Already logged in!", Toast.LENGTH_SHORT).show();
            String userId = authViewModel.getUser().getUid();
            String username = authViewModel.getUser().getEmail();
            Intent intent = new Intent(Login.this, DestinationActivity.class);
            intent.putExtra("userId", userId);
            intent.putExtra("username", username);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove signOut unless you want users to be logged out when this activity is destroyed
    }
}
