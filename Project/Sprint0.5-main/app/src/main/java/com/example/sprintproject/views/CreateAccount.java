package com.example.sprintproject.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.databinding.DataBindingUtil;
import com.example.sprintproject.R;
import com.example.sprintproject.databinding.CreateAccountBinding;
import com.example.sprintproject.viewmodels.AuthViewModel;

public class CreateAccount extends AppCompatActivity {

    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up Data Binding and ViewModel
        CreateAccountBinding binding = DataBindingUtil.setContentView(this, R.layout.create_account);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        binding.setAuthViewModel(authViewModel);
        binding.setLifecycleOwner(this);

        // Set up observers for LiveData in AuthViewModel
        setupObservers();
    }

    private void setupObservers() {
        // Observe account creation status
        authViewModel.isAccountCreated.observe(this, isCreated -> {
            if (isCreated) {
                Toast.makeText(CreateAccount.this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CreateAccount.this, Login.class));
                finish();
            }
        });

        // Observe error messages for validation or creation failure
        authViewModel.errorMessage.observe(this, error -> {
            if (error != null) {
                Toast.makeText(CreateAccount.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}