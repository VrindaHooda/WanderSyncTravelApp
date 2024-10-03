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
import com.example.sprintproject.viewmodels.NewAccountViewModel;
import com.example.sprintproject.viewmodels.ValidateViewModel;
import com.google.firebase.auth.FirebaseUser;

public class CreateAccount extends AppCompatActivity {

    private ValidateViewModel validateViewModel;
    private AuthViewModel authViewModel;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        authViewModel = new AuthViewModel();
        validateViewModel = new ValidateViewModel();
        firebaseUser = new FirebaseUser();

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
                    authViewModel.checkCurrentUser();
                    authViewModel.createUser(username,password);
                    Toast.makeText(CreateAccount.this, "Account created!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CreateAccount.this, "Invalid input. Can't be empty or contain whitespace", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Change UI according to user data.
        public void updateUI(FirebaseUser account) {

            if(account != null){
                Toast.makeText(this,"You signed in successfully",Toast.LENGTH_LONG).show();
                startActivity(new Intent(this,Login.class));

            }else {
                Toast.makeText(this,"You didn't sign in",Toast.LENGTH_LONG).show();
            }

        }
    }
}
