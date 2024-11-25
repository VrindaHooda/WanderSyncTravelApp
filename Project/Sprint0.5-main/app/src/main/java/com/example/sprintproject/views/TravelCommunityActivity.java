package com.example.sprintproject.views;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sprintproject.R;
import com.example.sprintproject.viewmodels.TravelPost;
import com.example.sprintproject.viewmodels.TravelPostsAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TravelCommunityActivity extends AppCompatActivity {

    private static final String TAG = "TravelCommunityActivity";
    private RecyclerView travelPostsRecyclerView;
    private Button createPostButton;
    private FirebaseFirestore db;
    private ArrayList<Map<String, Object>> travelPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.travel_community_screen);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.bottomNavigation, NavigationFragment.class, null)
                    .commit();
        }

        db = FirebaseFirestore.getInstance();
        travelPosts = new ArrayList<>();

        travelPostsRecyclerView = findViewById(R.id.travelPostsRecyclerView);
        createPostButton = findViewById(R.id.createPostButton);

        travelPostsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchTravelPosts();

        createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewTravelPost();
            }
        });
    }

    /**
     * Fetches travel posts from Firebase Firestore and displays them in the RecyclerView.
     */
    private void fetchTravelPosts() {
        db.collection("travelCommunity")
                .orderBy("startDate")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            travelPosts.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> post = document.getData();
                                post.putIfAbsent("isBoosted", false); // Default to non-boosted
                                travelPosts.add(post);
                            }
                            travelPosts.sort((post1, post2) -> {
                                try {
                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                    Date date1 = formatter.parse((String)
                                            post1.get("startDate")); // Parse the startDate of post1
                                    Date date2 = formatter.parse((String)
                                            post2.get("startDate")); // Parse the startDate of post2
                                    return date1.compareTo(date2); // Compare the two dates
                                } catch (ParseException e) {
                                    e.printStackTrace(); // Print the stack trace for debugging
                                    return 0; // Return 0 if parsing fails
                                }
                            });
                            TravelPostsAdapter adapter = new TravelPostsAdapter(travelPosts);
                            adapter.notifyDataSetChanged();
                            travelPostsRecyclerView.setAdapter(adapter);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    /**
     * Opens a dialog for creating a new travel post,
     * validates the inputs, and saves it to Firestore.
     */
    private void createNewTravelPost() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_create_travel_posr, null);
        CheckBox boostPostCheckbox = dialogView.findViewById(R.id.boostPostCheckbox);

        EditText startDateInput = dialogView.findViewById(R.id.startDateInput);
        EditText endDateInput = dialogView.findViewById(R.id.endDateInput);
        EditText destinationInput = dialogView.findViewById(R.id.destinationInput);
        EditText accommodationsInput = dialogView.findViewById(R.id.accommodationsInput);
        EditText diningReservationsInput = dialogView.findViewById(R.id.diningReservationsInput);
        EditText notesInput = dialogView.findViewById(R.id.notesInput);
        Button savePostButton = dialogView.findViewById(R.id.savePostButton);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();

        savePostButton.setOnClickListener(v -> {
            String startDate = startDateInput.getText().toString().trim();
            String endDate = endDateInput.getText().toString().trim();
            String destination = destinationInput.getText().toString().trim();
            String accommodations = accommodationsInput.getText().toString().trim();
            String diningReservations = diningReservationsInput.getText().toString().trim();
            String notes = notesInput.getText().toString().trim();

            if (startDate.isEmpty() || endDate.isEmpty() || destination.isEmpty()) {
                Toast.makeText(this, "Start Date, End Date, and Destination are required.",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            long duration = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                duration = calculateTripDuration(startDate, endDate);
            }
            if (duration < 0) {
                Toast.makeText(this, "End Date must be after Start Date.",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, Object> newPost = new HashMap<>();
            newPost.put("startDate", startDate);
            newPost.put("endDate", endDate);
            newPost.put("duration", duration + " days");
            newPost.put("destination", destination);
            newPost.put("accommodations", accommodations);
            newPost.put("diningReservations", diningReservations);
            newPost.put("notes", notes);
            boolean isBoosted = boostPostCheckbox.isChecked();

            TravelPost travelPost = new RegularTravelPost(newPost);
            if (isBoosted) {
                travelPost = new BoostedTravelPost(travelPost);

            }
            newPost.put("isBoosted", travelPost.isBoosted());



            db.collection("travelCommunity")
                    .add(newPost)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(TravelCommunityActivity.this,
                                "Travel post created!", Toast.LENGTH_SHORT).show();
                        fetchTravelPosts();
                        TravelPostsAdapter adapter = (TravelPostsAdapter)
                                travelPostsRecyclerView.getAdapter();
                        if (adapter != null) {
                            adapter.addPost(newPost);
                        }
                        dialog.dismiss();

                    })
                    .addOnFailureListener(e -> {
                        Log.w(TAG, "Error adding document", e);
                        Toast.makeText(TravelCommunityActivity.this,
                                "Failed to create post.", Toast.LENGTH_SHORT).show();
                    });
        });
    }

    /**
     * Calculates the duration of a trip in days based on the start and end dates.
     *
     * @param startDate the start date in "yyyy-MM-dd" format
     * @param endDate   the end date in "yyyy-MM-dd" format
     * @return the number of days between the start and end dates, or -1 if parsing fails
     */
    public long calculateTripDuration(String startDate, String endDate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate start = LocalDate.parse(startDate, formatter);
            LocalDate end = LocalDate.parse(endDate, formatter);
            if (end.isBefore(start)) {
                return -1; // Negative duration
            }
            return ChronoUnit.DAYS.between(start, end);
        } catch (Exception e) {
            return -1; // Handle invalid dates
        }
    }



    public void setTravelPosts(ArrayList<Map<String, Object>> posts) {
        this.travelPosts = posts;
    }

    public ArrayList<Map<String, Object>> getTravelPosts() {
        if (travelPosts == null) {
            travelPosts = new ArrayList<>();
        }
        return travelPosts;
    }

    public void removeTravelPost(Map<String, Object> post) {
        if (travelPosts != null && travelPosts.contains(post)) {
            travelPosts.remove(post);
        }
    }


}
