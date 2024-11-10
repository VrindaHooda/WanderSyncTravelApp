package com.example.sprintproject.views;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.databinding.DataBindingUtil;
import com.example.sprintproject.R;
import com.example.sprintproject.databinding.LogTravelFormBinding;
import com.example.sprintproject.viewmodels.LogTravelViewModel;

public class LogTravelForm extends AppCompatActivity {

    private LogTravelViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogTravelFormBinding binding = DataBindingUtil.setContentView(this, R.layout.log_travel_form);

        viewModel = new ViewModelProvider(this).get(LogTravelViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        // Setup date pickers
        binding.openStartDatePicker.setOnClickListener(v -> viewModel.onStartDateClick());
        binding.openEndDatePicker.setOnClickListener(v -> viewModel.onEndDateClick());
    }
}

