package com.example.sprintproject.views;

import android.annotation.SuppressLint;
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        validateViewModel = new ValidateViewModel();
        authViewModel = new AuthViewModel();
        EditText usernameEditText = findViewById(R.id.usernameEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        Button loginButton = findViewById(R.id.loginButton);
        Button createAccountButton = findViewById(R.id.createAccountButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                if (validateViewModel.validateLogin(username, password)) {
                    authViewModel.checkCurrentUser();
                    authViewModel.signIn(username, password, new AuthViewModel.Callback() {
                        @Override
                        public void onSuccess(FirebaseUser user) {
                            Toast.makeText(Login.this, "Login successful!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login.this, LogisticsActivity.class);
                            startActivity(intent);
                        }
                        @Override
                        public void onFailure(String error) {
                            Toast.makeText(Login.this, "Login failed: " + error, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(Login.this, "Invalid input. Can't be empty or contain whitespace", Toast.LENGTH_SHORT).show();
                }
            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, CreateAccount.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onResume(){
        super.onResume();
        authViewModel.getAuthenticationStatus().observe(this, isAuthenticated -> {
            if (isAuthenticated) {
                Toast.makeText(Login.this, "Already logged in!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Login.this, LogisticsActivity.class);
                startActivity(intent);
            }else {
                Toast.makeText(Login.this, "Invalid Credentials!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        authViewModel.signOut();
    }
}

