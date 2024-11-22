package com.example.sprintproject.views;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.sprintproject.R;
import com.example.sprintproject.databinding.DiningEstablishmentScreenBinding;
import com.example.sprintproject.model.DiningReservation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DiningActivity extends AppCompatActivity {

    private DiningEstablishmentScreenBinding binding;
    private DatabaseReference databaseRef;
    private List<DiningReservation> reservations = new ArrayList<>();
    private DiningReservationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.bottomNavigation, NavigationFragment.class, null)
                    .commit();
        }
        binding = DataBindingUtil.setContentView(this, R.layout.dining_establishment_screen);

        // Set the activity for binding
        binding.setActivity(this);

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

    /**
     * Adds a new dining reservation to Firebase Realtime Database.
     * Validates user input and clears the form after successfully adding a reservation.
     */
    public void addReservation() {
        String location = binding.locationInput.getText().toString().trim();
        String time = binding.timeInput.getText().toString().trim();
        String website = binding.websiteInput.getText().toString().trim();
        int rating = (int) binding.ratingInput.getRating();

        int month = Integer.parseInt(binding.monthInput.getText().toString().trim());
        int day = Integer.parseInt(binding.dayInput.getText().toString().trim());
        int year = Integer.parseInt(binding.yearInput.getText().toString().trim());

        if (TextUtils.isEmpty(location) || TextUtils.isEmpty(time) || TextUtils.isEmpty(website)) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidTime(time)) {
            Toast.makeText(this, "Invalid time format. Use HH:mm.", Toast.LENGTH_SHORT).show();
            return;
        }

        String reservationId = databaseRef.push().getKey();
        if (reservationId != null) {
            DiningReservation reservation = new DiningReservation(location, month, day, year, time, 0, website, rating);
            databaseRef.child(reservationId).setValue(reservation);

            binding.locationInput.setText("");
            binding.timeInput.setText("");
            binding.websiteInput.setText("");
            binding.ratingInput.setRating(0);
        }
    }


    /**
     * Validates the time input to ensure it matches the "HH:mm" format.
     *
     * @param time the time string to validate
     * @return {@code true} if the time format is valid; {@code false} otherwise
     */
    private boolean isValidTime(String time) {
        String timeRegex = "^([01]\\d|2[0-3]):([0-5]\\d)$";
        return time.matches(timeRegex);
    }

    /**
     * Checks if the given date is in the past compared to the current date.
     *
     * @param year  the year of the date
     * @param month the month of the date
     * @param day   the day of the date
     * @return {@code true} if the date is in the past; {@code false} otherwise
     */
    private boolean isDateInPast(int year, int month, int day) {
        Calendar today = Calendar.getInstance();
        Calendar reservationDate = Calendar.getInstance();
        reservationDate.set(year, month - 1, day);
        return reservationDate.before(today);
    }

    /**
     * Fetches dining reservations from Firebase Realtime Database and updates the RecyclerView.
     */
    private void fetchReservationsFromDatabase() {
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                reservations.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DiningReservation reservation = snapshot.getValue(DiningReservation.class);
                    if (reservation != null) {
                        reservations.add(reservation);
                    }
                }

                // Sort reservations by year, month, day, and time
                reservations.sort((r1, r2) -> {
                    Calendar cal1 = Calendar.getInstance();
                    cal1.set(r1.getYear(), r1.getMonth() - 1, r1.getDay());
                    Calendar cal2 = Calendar.getInstance();
                    cal2.set(r2.getYear(), r2.getMonth() - 1, r2.getDay());

                    if (cal1.equals(cal2)) {
                        return r1.getTime().compareTo(r2.getTime());
                    }
                    return cal1.compareTo(cal2);
                });

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("DiningActivity", "Database error: " + databaseError.getMessage());
                Toast.makeText(DiningActivity.this, "Failed to load reservations.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
