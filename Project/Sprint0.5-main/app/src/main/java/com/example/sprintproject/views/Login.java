package com.example.sprintproject.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.sprintproject.R;
import com.example.sprintproject.databinding.ActivityLoginBinding;
import com.example.sprintproject.viewmodels.AuthViewModel;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the ViewModel
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // Bind the layout with ViewModel
        ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setAuthViewModel(authViewModel);
        binding.setLifecycleOwner(this);

        // Setup listeners for the buttons
        setupListeners(binding);

        // Observe login result
        authViewModel.isLoginSuccessful.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSuccessful) {
                if (isSuccessful) {
                    FirebaseUser user = authViewModel.getCurrentUser();
                    Toast.makeText(Login.this, "Login successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, DestinationActivity.class);
                    intent.putExtra("userId", user.getUid());
                    intent.putExtra("username", user.getEmail());
                    startActivity(intent);
                    finish();
                } else {
                    String errorMessage = authViewModel.errorMessage.getValue();
                    Toast.makeText(Login.this, "Login failed: " + (errorMessage != null ? errorMessage : "Unknown error"), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupListeners(ActivityLoginBinding binding) {
        binding.loginButton.setOnClickListener(view -> {
            String username = authViewModel.username.getValue();
            String password = authViewModel.password.getValue();

            if (authViewModel.isInputValid(username, password)) {
                // Call login method in ViewModel
                authViewModel.login(username, password);
            } else {
                Toast.makeText(Login.this, "Invalid input. Please check your email and password.", Toast.LENGTH_SHORT).show();
            }
        });

        binding.createAccountButton.setOnClickListener(view -> {
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
            FirebaseUser user = authViewModel.getCurrentUser();
            Intent intent = new Intent(Login.this, DestinationActivity.class);
            intent.putExtra("userId", user.getUid());
            intent.putExtra("username", user.getEmail());
            startActivity(intent);
            finish();
        }
    }
}