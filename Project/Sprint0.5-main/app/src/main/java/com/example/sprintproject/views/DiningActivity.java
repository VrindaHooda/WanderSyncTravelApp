package com.example.sprintproject.views;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sprintproject.R;
import com.example.sprintproject.databinding.DiningEstablishmentScreenBinding;
import com.example.sprintproject.model.DiningReservation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class DiningActivity extends AppCompatActivity {

    private DiningEstablishmentScreenBinding binding;
    private DatabaseReference databaseRef;
    private List<DiningReservation> reservations = new ArrayList<>();
    private DiningReservationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize data binding
        binding = DiningEstablishmentScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set activity for data binding
        binding.setActivity(this);

        // Initialize Firebase Database reference
        databaseRef = FirebaseDatabase.getInstance().getReference("DiningReservations");

        // Set up the RecyclerView
        binding.reservationList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DiningReservationAdapter(reservations);
        binding.reservationList.setAdapter(adapter);

        // Fetch reservations from the database
        fetchReservationsFromDatabase();
    }

    public void addReservation() {
        String location = binding.locationInput.getText().toString().trim();
        String time = binding.timeInput.getText().toString().trim();
        String website = binding.websiteInput.getText().toString().trim();
        int rating = (int) binding.ratingInput.getRating();

        if (TextUtils.isEmpty(location) || TextUtils.isEmpty(time) || TextUtils.isEmpty(website)) {
            // Show an error message if fields are empty
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate a unique ID for the reservation
        String reservationId = databaseRef.push().getKey();
        if (reservationId != null) {
            DiningReservation reservation = new DiningReservation(location, time, website, rating);
            databaseRef.child(reservationId).setValue(reservation);

            // Clear input fields after adding the reservation
            binding.locationInput.setText("");
            binding.timeInput.setText("");
            binding.websiteInput.setText("");
            binding.ratingInput.setRating(0);
        }
    }

    private void fetchReservationsFromDatabase() {
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                reservations.clear(); // Clear the list to avoid duplicates

                // Loop through all the children in the database
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DiningReservation reservation = snapshot.getValue(DiningReservation.class);

                    if (reservation != null) {
                        reservations.add(reservation);
                    }
                }

                // Sort reservations by date and time if needed
                reservations.sort((r1, r2) -> r1.getTime().compareTo(r2.getTime()));

                // Notify the adapter that data has changed
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("DiningActivity", "Database error: " + databaseError.getMessage());
                Toast.makeText(DiningActivity.this, "Failed to load reservations. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
