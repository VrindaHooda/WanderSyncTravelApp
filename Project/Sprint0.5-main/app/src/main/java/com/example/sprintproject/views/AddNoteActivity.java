package com.example.sprintproject.views;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.sprintproject.R;
import com.example.sprintproject.databinding.ActivityAddNoteBinding;
import com.example.sprintproject.viewmodels.AddNoteViewModel;

public class AddNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAddNoteBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_add_note);
        binding.setAddNoteViewModel(new AddNoteViewModel());
        binding.executePendingBindings();
    }

    @BindingAdapter({"toastMessage"})
    public static void run(View view, String message) {
        if (message != null) {
            Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}

