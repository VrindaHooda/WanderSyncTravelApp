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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;

public class AccommodationActivity extends AppCompatActivity {
    private AccommodationScreenBinding binding;
    private ArrayList<Accommodation> accommodations;
    private AccommodationAdapter adapter;
    private FirebaseFirestore db;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.bottomNavigation, NavigationFragment.class, null)
                    .commit();
        }

        // Set up data binding
        binding = DataBindingUtil.setContentView(this, R.layout.accommodation_screen);
        binding.setActivity(this); // Bind this activity for the onClick handler

        // Initialize Firebase Firestore and get current user ID
        db = FirebaseFirestore.getInstance();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Set up RecyclerView
        accommodations = new ArrayList<>();
        adapter = new AccommodationAdapter(this, accommodations);
        binding.accommodationListView.setLayoutManager(new LinearLayoutManager(this));
        binding.accommodationListView.setAdapter(adapter);

        binding.addNewAccommodationButton.setOnClickListener(view -> showAddAccommodationDialog());

        // Fetch accommodations from Firestore for the current user
        fetchAccommodations();
    }

    /**
     * Displays a dialog for adding a new accommodation. Includes inputs for
     * location, check-in date, check-out date, number of rooms, and room type.
     * The new accommodation is saved to Firestore and added to the RecyclerView.
     */
    public void showAddAccommodationDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.accommodation_form, null);
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

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1,
                                                                            month1, dayOfMonth) -> {
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

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1,
                                                                            month1, dayOfMonth) -> {
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

            if (location.isEmpty() || checkInDate.isEmpty() || checkOutDate.isEmpty()
                    || numRooms.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                // Create a new Accommodation entry and add it to Firestore
                Accommodation accommodation = new Accommodation(location, checkInDate, checkOutDate,
                        Integer.parseInt(numRooms), roomType);

                saveAccommodation(accommodation);
                accommodations.add(accommodation);
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    /**
     * Saves the accommodation to Firestore under the current user's collection.
     *
     * @param accommodation The accommodation to save.
     */
    private void saveAccommodation(Accommodation accommodation) {
        CollectionReference userAccommodationsRef = db.collection("users")
                .document(currentUserId)
                .collection("accommodations");

        userAccommodationsRef.add(accommodation)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Accommodation saved successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to save accommodation", Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * Fetches accommodations from Firestore for the current user.
     */
    private void fetchAccommodations() {
        db.collection("users")
                .document(currentUserId)
                .collection("accommodations")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    accommodations.clear();
                    accommodations.addAll(queryDocumentSnapshots.toObjects(Accommodation.class));
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load accommodations", Toast.LENGTH_SHORT).show();
                });
    }
}
