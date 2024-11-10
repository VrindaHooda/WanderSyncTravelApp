package com.example.sprintproject.views;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.sprintproject.R;
import com.example.sprintproject.databinding.VacationTimeFormBinding;
import com.example.sprintproject.viewmodels.VacationTimeFormViewModel;

public class VacationTimeForm extends AppCompatActivity {

    private VacationTimeFormViewModel viewModel;
    private String userId;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up data binding
        VacationTimeFormBinding binding = DataBindingUtil.setContentView(this, R.layout.vacation_time_form);

        userId = getIntent().getStringExtra("userId");
        email = getIntent().getStringExtra("username");
        viewModel = new VacationTimeFormViewModel();
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        // Set up observers
        viewModel.getResultText().observe(this, result -> {
            // Update the result display (e.g., show the result in a TextView)
        });

        viewModel.getErrorMessage().observe(this, error -> {
            // Display any error messages
        });
    }

    public void onCalculateClick(View view) {
        viewModel.calculateVacationTime(userId, email);
    }
}
