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

public class CreateAccount extends AppCompatActivity {

    private final ValidateViewModel validateViewModel = new ValidateViewModel();
    private final AuthViewModel authViewModel = new AuthViewModel();

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button registerButton;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);
        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerButton);
        loginButton = findViewById(R.id.loginButton);
    }

    private void setupListeners() {
        registerButton.setOnClickListener(view -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (validateViewModel.validateRegistration(username, password)) {
                authViewModel.createUser(username, password, new AuthViewModel.AuthCallback() {
                    @Override
                    public void onSuccess(FirebaseUser user) {
                        Toast.makeText(CreateAccount.this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(CreateAccount.this, Login.class));
                        finish();
                    }

                    @Override
                    public void onFailure(String error) {
                        Toast.makeText(CreateAccount.this, "Account creation failed: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(CreateAccount.this, "Invalid input. Can't be empty or contain whitespace", Toast.LENGTH_SHORT).show();
            }
        });

        loginButton.setOnClickListener(view -> {
            startActivity(new Intent(CreateAccount.this, Login.class));
            finish();
        });
    }
}
