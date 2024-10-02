package com.example.sprintproject.views;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sprintproject.R;
import com.example.sprintproject.viewmodels.ValidateViewModel;

public class CreateAccount extends AppCompatActivity {

    private ValidateViewModel validateViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        validateViewModel = new ValidateViewModel();

        EditText usernameEditText = findViewById(R.id.usernameEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        Button registerButton = findViewById(R.id.registerButton);

        // Register button action
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (validateViewModel.validateRegistration(username, password)) {
                    // Proceed to account creation logic
                    Toast.makeText(CreateAccount.this, "Account created!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CreateAccount.this, "Invalid input. Can't be empty or contain whitespace", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
