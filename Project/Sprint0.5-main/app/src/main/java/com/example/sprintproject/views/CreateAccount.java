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

    private final ValidateViewModel validateVIEWMODEL = new ValidateViewModel();
    private final AuthViewModel authVIEWMODEL = new AuthViewModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        EditText usernameEditText = findViewById(R.id.usernameEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        Button registerButton = findViewById(R.id.registerButton);
        Button loginButton = findViewById(R.id.loginButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (validateVIEWMODEL.validateRegistration(username, password)) {
                    authVIEWMODEL.createUser(username, password, new AuthViewModel.Callback() {
                        @Override
                        public void onSuccess(FirebaseUser user) {
                            Toast.makeText(CreateAccount.this,
                                    "Account created successfully!",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CreateAccount.this, Login.class);
                            startActivity(intent);
                            finish();
                        }
                        @Override
                        public void onFailure(String error) {
                            Toast.makeText(CreateAccount.this,
                                    "Account creation failed: " + error,
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(CreateAccount.this,
                            "Invalid input. Can't be empty or contain whitespace",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateAccount.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.create_account);

        EditText usernameEditText = findViewById(R.id.usernameEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        Button registerButton = findViewById(R.id.registerButton);
        Button loginButton = findViewById(R.id.loginButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (validateVIEWMODEL.validateRegistration(username, password)) {
                    authVIEWMODEL.createUser(username, password, new AuthViewModel.Callback() {
                        @Override
                        public void onSuccess(FirebaseUser user) {
                            Toast.makeText(CreateAccount.this,
                                    "Account created successfully!",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CreateAccount.this, Login.class);
                            startActivity(intent);
                            finish();
                        }
                        @Override
                        public void onFailure(String error) {
                            Toast.makeText(CreateAccount.this,
                                    "Account creation failed: " + error,
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(CreateAccount.this,
                            "Invalid input. Can't be empty or contain whitespace",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateAccount.this, Login.class);
                startActivity(intent);
                onDestroy();
            }
        });
    }
}
