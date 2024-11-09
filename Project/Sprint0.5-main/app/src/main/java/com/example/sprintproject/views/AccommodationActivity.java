package com.example.sprintproject.views;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.sprintproject.R;
import com.example.sprintproject.databinding.AccommodationScreenBinding;
import com.example.sprintproject.model.Accommodation;
import com.example.sprintproject.viewmodels.AccommodationAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class AccommodationActivity extends AppCompatActivity {
    private AccommodationScreenBinding binding;
    private ArrayList<Accommodation> accommodations;
    private AccommodationAdapter adapter;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = AccommodationScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.setActivity(this);

        databaseReference = FirebaseDatabase.getInstance().getReference("Accommodations");
        accommodations = new ArrayList<>();
        adapter = new AccommodationAdapter(this, accommodations);
        binding.accommodationListView.setLayoutManager(new LinearLayoutManager(this));
        binding.accommodationListView.setAdapter(adapter);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.bottomNavigation, NavigationFragment.class, null)
                    .commit();
        }

        binding.addNewAccommodationButton.setOnClickListener(view -> showAddAccommodationDialog());

        // Set up sorting buttons
        Button sortCheckInButton = findViewById(R.id.sort_checkin);
        Button sortCheckOutButton = findViewById(R.id.sort_checkout);

        sortCheckInButton.setOnClickListener(v -> sortByCheckInDate());
        sortCheckOutButton.setOnClickListener(v -> sortByCheckOutDate());
    }

    public void showAddAccommodationDialog() {
        View dialogView = LayoutInflater.from(AccommodationActivity.this).inflate(R.layout.accommodation_form, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        EditText locationInput = dialogView.findViewById(R.id.form_locationInput);
        EditText checkInDateInput = dialogView.findViewById(R.id.form_checkInDateInput);
        EditText checkOutDateInput = dialogView.findViewById(R.id.form_checkOutDateInput);
        EditText numRoomsInput = dialogView.findViewById(R.id.form_numRoomsInput);
        Spinner roomTypeSpinner = dialogView.findViewById(R.id.form_roomTypeSpinner);
        Button addAccommodationButton = dialogView.findViewById(R.id.form_addAccommodationButton);

        // Set DatePickerDialog for check-in date
        checkInDateInput.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(AccommodationActivity.this, (view, year1, month1, dayOfMonth) -> {
                checkInDateInput.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1);
            }, year, month, day);
            datePickerDialog.show();
        });

        // Set DatePickerDialog for check-out date
        checkOutDateInput.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(AccommodationActivity.this, (view, year1, month1, dayOfMonth) -> {
                checkOutDateInput.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1);
            }, year, month, day);
            datePickerDialog.show();
        });

        addAccommodationButton.setOnClickListener(v -> {
            String location = locationInput.getText().toString();
            String checkInDate = checkInDateInput.getText().toString();
            String checkOutDate = checkOutDateInput.getText().toString();
            String numRooms = numRoomsInput.getText().toString();
            String roomType = roomTypeSpinner.getSelectedItem().toString();

            if (location.isEmpty() || checkInDate.isEmpty() || checkOutDate.isEmpty() || numRooms.isEmpty()) {
                Toast.makeText(AccommodationActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                Accommodation accommodation = new Accommodation(location, checkInDate, checkOutDate, Integer.parseInt(numRooms), roomType);
                databaseReference.push().setValue(accommodation);
                accommodations.add(accommodation);
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void sortByCheckInDate() {
        Collections.sort(accommodations, (a1, a2) -> {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Date date1 = sdf.parse(a1.getCheckInDate());
                Date date2 = sdf.parse(a2.getCheckInDate());
                return date1.compareTo(date2);
            } catch (ParseException e) {
                e.printStackTrace();
                return 0; // In case of error, leave order unchanged
            }
        });
        adapter.notifyDataSetChanged();
    }

    private void sortByCheckOutDate() {
        Collections.sort(accommodations, (a1, a2) -> {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Date date1 = sdf.parse(a1.getCheckOutDate());
                Date date2 = sdf.parse(a2.getCheckOutDate());
                return date1.compareTo(date2);
            } catch (ParseException e) {
                e.printStackTrace();
                return 0; // In case of error, leave order unchanged
            }
        });
        adapter.notifyDataSetChanged();
    }
}
