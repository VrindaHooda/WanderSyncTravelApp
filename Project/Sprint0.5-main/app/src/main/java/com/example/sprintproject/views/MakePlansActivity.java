package com.example.sprintproject.views;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sprintproject.R;
import com.example.sprintproject.model.Destination;
import com.example.sprintproject.model.FirebaseRepository;
import com.example.sprintproject.model.Id;
import com.example.sprintproject.model.Plan;
import com.example.sprintproject.viewmodels.LogisticsViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


public class MakePlansActivity extends AppCompatActivity {
    private LogisticsViewModel logisticsViewModel;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private PlansAdapter plansAdapter;
    private EditText editTextDuration;
    private EditText editTextNotes;
    private EditText editTextLocation;
    private EditText editTextTransportation;
    private EditText editTextDiningReservations;
    private EditText editTextAccommodations;
    private EditText editTextCollaborators;
    private Button buttonAddPlan;
    private Button buttonAddDestination;
    private Button buttonExit;
    private EditText editTextTripName;
    private String userId; // Replace with actual user ID logic
    private List<Destination> destinations = new ArrayList<>();
    private List<String> collaborators = new ArrayList<>();
    private FirebaseRepository firebaseRepository;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private Id id;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_plans);
        firebaseRepository = new FirebaseRepository();
        // Initialize Firebase Auth and Firestore
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        id = Id.getInstance();
        // Initialize UI components
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        editTextTripName = findViewById(R.id.editTextTripName);
        editTextDuration = findViewById(R.id.editTextDuration);
        editTextNotes = findViewById(R.id.editTextNotes);
        editTextLocation = findViewById(R.id.editTextLocation);
        editTextTransportation = findViewById(R.id.editTextTransportation);
        editTextDiningReservations = findViewById(R.id.editTextDiningReservations);
        editTextAccommodations = findViewById(R.id.editTextAccommodations);
        editTextCollaborators = findViewById(R.id.editTextCollaborators);
        buttonAddPlan = findViewById(R.id.buttonAddPlan);
        buttonAddDestination = findViewById(R.id.buttonAddDestination);
        buttonExit = findViewById(R.id.buttonExit);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        plansAdapter = new PlansAdapter();
        recyclerView.setAdapter(plansAdapter);
        logisticsViewModel = new ViewModelProvider(this).get(LogisticsViewModel.class);
        userId = firebaseAuth.getCurrentUser().getUid();
        logisticsViewModel.getPlansLiveData().observe(this, new Observer<List<Plan>>() {
            @Override
            public void onChanged(List<Plan> plans) {
                plansAdapter.setPlans(plans);
            }
        });
        Button buttonSharePlan = findViewById(R.id.buttonSharePlan);
        buttonSharePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharePlanToTravelCommunity();
            }
        });

        logisticsViewModel.getIsLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
        });
        logisticsViewModel.getErrorMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                if (errorMessage != null) {
                    Toast.makeText(MakePlansActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
        logisticsViewModel.getPlansLiveData().observe(this, new Observer<List<Plan>>() {
            @Override
            public void onChanged(List<Plan> plans) {
                plansAdapter.setPlans(plans);
            }
        });
        logisticsViewModel.fetchPlans(userId);
        buttonAddDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = editTextLocation.getText().toString();
                String transportation = editTextTransportation.getText().toString();
                String diningReservations = editTextDiningReservations.getText().toString();
                String accommodations = editTextAccommodations.getText().toString();
                if (!location.isEmpty() && !transportation.isEmpty()
                        && !diningReservations.isEmpty() && !accommodations.isEmpty()) {
                    Destination destination = new Destination(location, parseList(accommodations),
                            parseList(diningReservations), transportation);
                    destinations.add(destination);
                    Toast.makeText(MakePlansActivity.this,
                            "Destination added", Toast.LENGTH_SHORT).show();
                    clearDestinationFields();
                } else {
                    Toast.makeText(MakePlansActivity.this,
                            "All destination fields are required", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Set up Add Plan button click listener
        buttonAddPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String durationText = editTextDuration.getText().toString();
                String notes = editTextNotes.getText().toString();
                String collaboratorsText = editTextCollaborators.getText().toString();
                String tripName = editTextTripName.getText().toString(); // Add a field for trip name

                if (!durationText.isEmpty() && !notes.isEmpty() && !destinations.isEmpty() && !tripName.isEmpty()) {
                    int duration;
                    try {
                        duration = Integer.parseInt(durationText);
                    } catch (NumberFormatException e) {
                        Toast.makeText(MakePlansActivity.this, "Invalid duration", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        return;
                    }

                    List<String> collaboratorsEmailList = parseCollaboratorsList(collaboratorsText);
                    List<String> collaboratorsIdList = new ArrayList<>();
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    // Initialize collaborators list and add the owner
                    collaborators.add(userId);

                    Plan newPlan = new Plan(duration, destinations, notes, collaborators, userId);

                    firebaseRepository.addPlan(userId, newPlan, new FirebaseRepository.PlanCallback() {
                        @Override
                        public void onPlanAdded(String planId) {
                            // Fetch all collaborator IDs
                            AtomicInteger counter = new AtomicInteger(collaboratorsEmailList.size());

                            for (String collaboratorEmail : collaboratorsEmailList) {
                                getUidByEmail(collaboratorEmail, uid -> {
                                    if (uid != null) {
                                        collaboratorsIdList.add(uid);
                                    }
                                    if (counter.decrementAndGet() == 0) {
                                        // All UIDs fetched, send invites
                                        for (String collaboratorId : collaboratorsIdList) {
                                            firebaseRepository.sendInvite(
                                                    collaboratorId, planId, userId, notes, tripName);
                                        }
                                        Toast.makeText(MakePlansActivity.this,
                                                "Plan added and invites sent",
                                                Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                        clearInputFields();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(String error) {
                            Toast.makeText(MakePlansActivity.this,
                                    "Failed to add plan: " + error, Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                } else {
                    Toast.makeText(MakePlansActivity.this,
                            "All fields are required and at least one destination must be added",
                            Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });



        // Set up Exit button click listener
        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close the activity
            }
        });
    }

    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    private List<String> parseCollaboratorsList(String input) {
        List<String> collaboratorsList = new ArrayList<>();

        if (input != null && !input.trim().isEmpty()) {
            String[] collaboratorsArray = input.split(",[ \t]*");
            for (String collaborator : collaboratorsArray) {
                if (!collaborator.isEmpty()) {
                    collaboratorsList.add(collaborator.trim());
                }
            }
        }
        Log.d("Data", "Collaborators List: " + collaboratorsList);
        return collaboratorsList;
    }

    private void clearInputFields() {
        editTextDuration.setText("");
        editTextNotes.setText("");
        editTextCollaborators.setText("");
        destinations.clear();
    }
    private void sharePlanToTravelCommunity() {
        if (destinations.isEmpty()) {
            Toast.makeText(this, "No destinations to share.", Toast.LENGTH_SHORT).show();
            return;
        }

        String durationText = editTextDuration.getText().toString();
        String notes = editTextNotes.getText().toString();
        String tripName = editTextTripName.getText().toString();

        if (durationText.isEmpty() || notes.isEmpty() || tripName.isEmpty()) {
            Toast.makeText(this, "All fields must be filled before sharing.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prepare the data to share
        Map<String, Object> travelCommunityPost = new HashMap<>();
        travelCommunityPost.put("tripName", tripName);
        travelCommunityPost.put("duration", durationText + " days");
        travelCommunityPost.put("notes", notes);
        travelCommunityPost.put("destinations", destinations);
        travelCommunityPost.put("userEmail", FirebaseAuth.getInstance().getCurrentUser().getEmail());
        travelCommunityPost.put("isBoosted", false); // Default value

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("travelCommunity")
                .add(travelCommunityPost)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(MakePlansActivity.this, "Plan shared successfully!", Toast.LENGTH_SHORT).show();

                    // Refresh the travel posts to include the new one
                    fetchTravelPosts(); // Call the fetch method to update the UI
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MakePlansActivity.this, "Failed to share plan: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void fetchTravelPosts() {
        TravelCommunityHelper.fetchTravelPosts(db, new TravelCommunityHelper.TravelPostsCallback() {
            @Override
            public void onSuccess(ArrayList<Map<String, Object>> travelPosts) {
                // Handle the fetched posts
                for (Map<String, Object> post : travelPosts) {
                    Log.d("TravelPost", post.toString());
                }
                Toast.makeText(MakePlansActivity.this, "Fetched travel posts!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(MakePlansActivity.this, "Failed to fetch travel posts.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * Clears the input fields for the destination form.
     * This method resets the text fields for location, transportation,
     * dining reservations, and accommodations.
     */
    private void clearDestinationFields() {
        editTextLocation.setText("");
        editTextTransportation.setText("");
        editTextDiningReservations.setText("");
        editTextAccommodations.setText("");
    }

    /**
     * Parses a comma-separated input string into a list of strings.
     *
     * @param input the input string containing items separated by commas
     * @return a list of trimmed strings parsed from the input,
     * or an empty list if the input is empty
     */
    private List<String> parseList(String input) {
        List<String> list = new ArrayList<>();
        if (!input.isEmpty()) {
            String[] items = input.split(",");
            for (String item : items) {
                list.add(item.trim());
            }
        }
        return list;
    }

    public void getUidByEmail(String email, FirestoreCallback callback) {
        db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (!querySnapshot.isEmpty()) {
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                String uid = document.getString("uid");
                                Log.d("Data", "User UID: " + uid);

                                // Use the singleton instance
                                Id id = Id.getInstance();
                                id.setId(uid);
                                System.out.println("User Id: " + id.getId());

                                // Pass the UID back to the caller via the callback
                                callback.onCallback(uid);
                                return; // Assuming only one match needed
                            }
                        } else {
                            System.out.println("No user found with this email");
                            callback.onCallback(null);
                        }
                    } else {
                        System.out.println("Error fetching user: " + task.getException());
                        callback.onCallback(null);
                    }
                });
    }

    public FirebaseFirestore getDb() {
        return db;
    }

    public interface FirestoreCallback {
        void onCallback(String uid);
    }

    // PlansAdapter class for RecyclerView
    class PlansAdapter extends RecyclerView.Adapter<PlansAdapter.PlanViewHolder> {
        private List<Plan> plans;

        /**
         * Sets the list of plans and notifies the adapter to update the data.
         *
         * @param plans the list of {@link Plan} objects to display
         */
        public void setPlans(List<Plan> plans) {
            this.plans = plans;
            notifyDataSetChanged();
        }

        @Override
        public PlanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(parent.getContext(), R.layout.item_plan, null);
            return new PlanViewHolder(view);
        }

        @Override
        public void onBindViewHolder(PlanViewHolder holder, int position) {
            Plan plan = plans.get(position);
            holder.bind(plan);
        }

        @Override
        public int getItemCount() {
            return plans != null ? plans.size() : 0;
        }

        class PlanViewHolder extends RecyclerView.ViewHolder {

            /**
             * Constructs a new {@code PlanViewHolder}.
             *
             * @param itemView the view representing a single item in the RecyclerView
             */
            public PlanViewHolder(View itemView) {
                super(itemView);
            }

            /**
             * Binds a {@link Plan} object to the item view.
             * This method populates the views with the plan's duration, notes, collaborators,
             * and destination details.
             *
             * @param plan the {@link Plan} object to bind
             */
            public void bind(Plan plan) {
                // Get references to the TextView fields in the layout
                EditText planDurationTextView = itemView.findViewById(R.id.textViewDuration);
                EditText planNotesTextView = itemView.findViewById(R.id.textViewNotes);
                EditText planCollaboratorsTextView = itemView.
                        findViewById(R.id.textViewCollaborators);
                EditText planDestinationsTextView = itemView.
                        findViewById(R.id.textViewDestinations);

                // Set the values for the duration and notes
                planDurationTextView.setText(String.format(
                        "Duration: %d days", plan.getDuration()));
                planNotesTextView.setText(String.format("Notes: " + plan.getNotes()));

                // Bind the collaborators list
                if (plan.getCollaborators() != null && !plan.getCollaborators().isEmpty()) {
                    planCollaboratorsTextView.setText(
                            "Collaborators: " + String.join(", ", plan.getCollaborators()));
                } else {
                    planCollaboratorsTextView.setText("Collaborators: None");
                }

                // Bind the destinations list
                StringBuilder destinationsBuilder = new StringBuilder();
                List<Destination> destinations = plan.getDestinations();
                if (destinations != null && !destinations.isEmpty()) {
                    for (int i = 0; i < destinations.size(); i++) {
                        Destination destination = destinations.get(i);
                        destinationsBuilder.append("Destination ").append(i + 1).append(":\n")
                                .append("- Location: ").
                                append(destination.getLocation()).append("\n")
                                .append("- Transportation: ").
                                append(destination.getTransportation()).append("\n")
                                .append("- Accommodations: ").append(String.join(", ",
                                        destination.getAccommodations())).append("\n")
                                .append("- Dining Reservations: ").append(String.join(", ",
                                        destination.getDiningReservations())).append("\n\n");
                    }
                } else {
                    destinationsBuilder.append("No destinations added.");
                }
                planDestinationsTextView.setText(destinationsBuilder.toString());
            }


        }
    }
}