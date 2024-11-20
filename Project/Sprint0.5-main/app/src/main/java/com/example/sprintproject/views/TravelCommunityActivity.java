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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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

    private void fetchTravelPosts() {
        db.collection("travelCommunity")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            travelPosts.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                travelPosts.add(document.getData());
                                Map<String, Object> post = document.getData();
                                post.putIfAbsent("isBoosted", false); // Default to non-boosted
                                travelPosts.add(post);
                            }
                            TravelPostsAdapter adapter = new TravelPostsAdapter(travelPosts);
                            adapter.notifyDataSetChanged();
                            travelPostsRecyclerView.setAdapter(adapter);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

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
                Toast.makeText(this, "Start Date, End Date, and Destination are required.", Toast.LENGTH_SHORT).show();
                return;
            }

            long duration = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                duration = calculateTripDuration(startDate, endDate);
            }
            if (duration < 0) {
                Toast.makeText(this, "End Date must be after Start Date.", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(TravelCommunityActivity.this, "Travel post created!", Toast.LENGTH_SHORT).show();
                        fetchTravelPosts();
                        dialog.dismiss();
                    })
                    .addOnFailureListener(e -> {
                        Log.w(TAG, "Error adding document", e);
                        Toast.makeText(TravelCommunityActivity.this, "Failed to create post.", Toast.LENGTH_SHORT).show();
                    });
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private long calculateTripDuration(String startDate, String endDate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate start = LocalDate.parse(startDate, formatter);
            LocalDate end = LocalDate.parse(endDate, formatter);
            return ChronoUnit.DAYS.between(start, end);
        } catch (DateTimeParseException e) {
            Toast.makeText(this, "Invalid date format. Use yyyy-MM-dd.", Toast.LENGTH_SHORT).show();
            return -1;
        }
    }
}
