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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sprintproject.R;
import com.example.sprintproject.model.Destination;
import com.example.sprintproject.model.Id;
import com.example.sprintproject.model.Plan;
import com.example.sprintproject.viewmodels.LogisticsViewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
//import java.util.concurrent.atomic.AtomicReference;
//Commented out to satisfy Checkstyle

import com.example.sprintproject.model.FirebaseRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class MakePlansActivity extends AppCompatActivity {
    private LogisticsViewModel logisticsViewModel;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private PlansAdapter plansAdapter;
    private EditText editTextDuration, editTextNotes;
    private EditText editTextLocation, editTextTransportation, editTextDiningReservations, editTextAccommodations, editTextCollaborators;
    private Button buttonAddPlan, buttonAddDestination, buttonExit;
    private String userId;
    private ArrayList<Destination> destinations = new ArrayList<>();
    private ArrayList<String> collaborators = new ArrayList<>();
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

        // Observe LiveData from ViewModel
        logisticsViewModel.getPlansLiveData().observe(this, plans -> plansAdapter.setPlans(plans));

        logisticsViewModel.getIsLoading().observe(this, isLoading -> progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE));

        logisticsViewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(MakePlansActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
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

                if (!durationText.isEmpty() && !notes.isEmpty() && !destinations.isEmpty()) {
                    int duration;
                    try {
                        duration = Integer.parseInt(durationText);
                    } catch (NumberFormatException e) {
                        Toast.makeText(MakePlansActivity.this,
                                "Invalid duration", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        return;
                    }

                // Create a new Plan object
                Plan newPlan = new Plan(duration, destinations, notes, collaborators, "");

                    Log.d("Data", "Collaborators: " + collaboratorsText);
                    ArrayList<String> collaboratorsEmailList = parseCollaboratorsList(collaboratorsText);
                    List<String> collaboratorsIdList = new ArrayList<>();
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    Plan newPlan = new Plan(duration, destinations, notes, collaborators);

                    firebaseRepository.addPlan(userId, newPlan,
                            new FirebaseRepository.PlanCallback() {
                            @Override
                            public void onPlanAdded(String planId) {
                                // Fetch all collaborator IDs
                                AtomicInteger counter = new AtomicInteger(
                                        collaboratorsEmailList.size());

                                for (String collaboratorEmail : collaboratorsEmailList) {
                                    getUidByEmail(collaboratorEmail, uid -> {
                                        if (uid != null) {
                                            collaboratorsIdList.add(uid);
                                        }
                                        if (counter.decrementAndGet() == 0) {
                                            // All UIDs fetched, send invites
                                            for (String collaboratorId : collaboratorsIdList) {
                                                Log.w("Id", "CollaboratorId " + collaboratorId);
                                                firebaseRepository.sendInvite(
                                                        collaboratorId, planId, userId, notes);
                                            }
                                          logisticsViewModel.refreshPlans(userId); // Trigger refresh of plans

                        plansAdapter.addPlanToList(plan);
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
            progressBar.setVisibility(View.GONE);
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

    private ArrayList<String> parseList(String input) {
        ArrayList<String> list = new ArrayList<>();
        if (!input.isEmpty()) {
            String[] items = input.split(",");
            for (String item : items) {
                list.add(item.trim());
            }
        }
        return list;
    }

    private ArrayList<Destination> parseDestinations(String destinationsText) {
        ArrayList<Destination> destinationList = new ArrayList<>();
        Log.d("ParseDestinations", "Raw Destinations Text: " + destinationsText);

        if (destinationsText == null || destinationsText.isEmpty()) {
            return destinationList; // Return empty if no destinations are specified
        }

        // Normalize line breaks
        destinationsText = destinationsText.replace("\r\n", "\n");

        // Split blocks by double newlines or other patterns indicating block separation
        String[] destinationBlocks = destinationsText.split("\n\n");
        for (String block : destinationBlocks) {
            try {
                String[] lines = block.split("\n");

                // Extract values for each field using updated extract methods
                String location = extractField(findLineWithPrefix(lines, "Location:"), "Location:");
                String transportation = extractField(findLineWithPrefix(lines, "Transportation:"), "Transportation:");
                ArrayList<String> accommodations = extractListField(findLineWithPrefix(lines, "Accommodations:"), "Accommodations:");
                ArrayList<String> diningReservations = extractListField(findLineWithPrefix(lines, "Dining Reservations:"), "Dining Reservations:");

                // Create Destination object only if location is present
                if (!location.isEmpty()) {
                    Destination destination = new Destination(location, accommodations, diningReservations, transportation);
                    destinationList.add(destination);
                } else {
                    Log.w("ParseDestinations", "Skipped block due to missing location: " + block);
                }
            } catch (Exception e) {
                Log.e("ParseDestinations", "Error parsing block: " + block, e);
            }
        }

        return destinationList;
    }



    private String extractField(String line, String prefix) {
        if (line == null || prefix == null || !line.contains(prefix)) {
            Log.w("ExtractField", "Prefix not found: " + prefix + " in line: " + line);
            return ""; // Return empty if prefix is missing or line is invalid
        }

        // Remove the prefix and trim the result to get the field value
        return line.substring(line.indexOf(prefix) + prefix.length()).trim();
    }
    private ArrayList<String> extractListField(String line, String prefix) {
        ArrayList<String> items = new ArrayList<>();

        if (line == null || prefix == null || !line.contains(prefix)) {
            Log.w("ExtractListField", "Prefix not found: " + prefix + " in line: " + line);
            return items; // Return empty list if line is invalid or prefix is missing
        }

        // Remove the prefix and trim the remaining text
        String listPart = line.substring(line.indexOf(prefix) + prefix.length()).trim();

        // Split the remaining text into items using a comma as the delimiter
        if (!listPart.isEmpty()) {
            String[] splitItems = listPart.split(",");
            for (String item : splitItems) {
                items.add(item.trim()); // Trim each item and add it to the list
            }
        }

        return items;
    }

    private String findLineWithPrefix(String[] lines, String prefix) {
        for (String line : lines) {
            if (line.contains(prefix)) {
                return line.trim();
            }
        }
        return ""; // Return an empty string if no matching line is found
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
        private ArrayList<Plan> plans;

        public void setPlans(ArrayList<Plan> plans) {
            this.plans = plans;
            notifyDataSetChanged();
        }

        public void addPlanToList(Plan plan) {
            plans.add(plan); // Add the new plan to the list
            notifyItemInserted(plans.size() - 1); // Notify adapter to update UI
        }

        @Override
        public PlanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(parent.getContext(), R.layout.item_plan, null);
            return new PlanViewHolder(view);
        }

        @Override
        public void onBindViewHolder(PlanViewHolder holder, int position) {
            Plan plan = plans.get(position);
            Log.d("MakePlansActivity", "Binding plan: " + plans.toString());
            holder.bind(plan);
            Log.d("PlanViewHolder", "Binding Plan ID: " + plan.getId());
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
                EditText planCollaboratorsTextView = itemView.findViewById(R.id.textViewCollaborators);
                EditText planDestinationsTextView = itemView.findViewById(R.id.textViewDestinations);
                Button buttonUpdatePlan = itemView.findViewById(R.id.buttonUpdatePlan);
                Log.d("Setting plan ID", "Setting plan id to " + plan.getId());
                buttonUpdatePlan.setTag(plan.getId());
                Log.d("Set Tag", "Line 303 Plan ID: " + plan);

                // Set the values for the duration and notes
                planDurationTextView.setText(String.format(
                        "Duration: %d days", plan.getDuration()));
                planNotesTextView.setText(String.format("Notes: " + plan.getNotes()));

                // Bind the collaborators list
                if (plan.getCollaborators() != null && !plan.getCollaborators().isEmpty()) {
                    planCollaboratorsTextView.setText(String.join(", ", plan.getCollaborators()));
                } else {
                    planCollaboratorsTextView.setText("Collaborators: None");
                }

                // Bind the destinations list
                StringBuilder destinationsBuilder = new StringBuilder();
                ArrayList<Destination> destinations = plan.getDestinations();
                Log.d("Data", "Destinations: " + destinations);
                if (destinations != null && !destinations.isEmpty()) {
                    for (int i = 0; i < destinations.size(); i++) {
                        Destination destination = destinations.get(i);
                        destinationsBuilder.append("Destination ").append(i + 1).append(":\n")
                                .append("- Location: ").append(destination.getLocation() != null ? destination.getLocation() : "Unknown").append("\n")
                                .append("- Transportation: ").append(destination.getTransportation() != null ? destination.getTransportation() : "None").append("\n")
                                .append("- Accommodations: ")
                                .append(destination.getAccommodations() != null ? String.join(", ", destination.getAccommodations()) : "None").append("\n")
                                .append("- Dining Reservations: ")
                                .append(destination.getDiningReservations() != null ? String.join(", ", destination.getDiningReservations()) : "None").append("\n\n");
                    }
                    if (destinationsBuilder.length() > 0) {
                        destinationsBuilder.setLength(destinationsBuilder.length() - 2); // Remove last "\n\n"
                    }
                } else {
                    destinationsBuilder.append("No destinations added.");
                }

// Update UI on the main thread
                runOnUiThread(() -> planDestinationsTextView.setText(destinationsBuilder.toString()));


                buttonUpdatePlan.setOnClickListener(v -> {
                    Log.d("MakePlansActivity", "Update button clicked");

                    progressBar.setVisibility(View.VISIBLE);
                    Log.d("MakePlansActivity", "Progress bar visible");

                    // Capture the updated values from the EditText fields
                    String durationText = planDurationTextView.getText().toString(); // Get updated duration
                    String notes = planNotesTextView.getText().toString(); // Get updated notes
                    String collaboratorsText = planCollaboratorsTextView.getText().toString(); // Get updated collaborators
                    String destinationsText = planDestinationsTextView.getText().toString(); // Get updated destinations
                    Log.d("Destinations Before Save", destinationsText);


                    Log.d("MakePlansActivity", "Captured input fields: duration=" + durationText + ", notes=" + notes + ", collaborators=" + collaboratorsText + ", destinations=" + destinationsText);

                    // Validate that the fields are not empty
                    if (!durationText.isEmpty() && !notes.isEmpty() && !destinationsText.isEmpty()) {
                        Log.d("MakePlansActivity", "Input fields are valid");

                        // Extract the numeric value from the duration text (e.g., "13" from "Duration: 13 days")
                        int duration = 0;
                        try {
                            String durationNumber = durationText.replaceAll("[^0-9]", "");
                            if (durationNumber.isEmpty()) {
                                Log.e("MakePlansActivity", "Invalid duration text: " + durationText);
                                Toast.makeText(MakePlansActivity.this, "Invalid duration value", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                return;
                            }
                            duration = Integer.parseInt(durationNumber);
                            Log.d("MakePlansActivity", "Parsed duration: " + duration);
                        } catch (NumberFormatException e) {
                            Log.e("MakePlansActivity", "Error parsing duration: " + durationText, e);
                            Toast.makeText(MakePlansActivity.this, "Invalid duration value", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            return;  // Exit the method early if parsing fails
                        }

                        // Parse collaborators and destinations
                        ArrayList<String> collaborators = parseList(collaboratorsText); // Helper function for parsing collaborators
                        ArrayList<Destination> updatedDestinations = parseDestinations(destinationsText); // Parse destinations from the updated text

                        Log.d("MakePlansActivity", "Parsed values: duration=" + duration + ", collaborators=" + collaborators + ", destinations=" + updatedDestinations);

                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Get logged-in user ID
                        Log.d("MakePlansActivity", "Retrieved User ID: " + userId);

                        String planId = (String) buttonUpdatePlan.getTag(); // Ensure plan is not null before calling getId()
                        Log.d("MakePlansActivity", "Retrieved Plan ID: " + planId);

                        if (planId == null) {
                            Log.e("MakePlansActivity", "Plan ID is null, cannot update plan");
                            Toast.makeText(MakePlansActivity.this, "Plan ID is missing. Please try again.", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            return; // Exit early if planId is null
                        }

                        Log.d("MakePlansActivity", "User ID: " + userId + ", Plan ID: " + planId);

                        // Create a new Plan object with the updated values
                        Plan updatedPlan = new Plan();
                        updatedPlan.setId(planId);
                        updatedPlan.setDuration(duration);
                        updatedPlan.setCollaborators(collaborators);
                        updatedPlan.setDestinations(updatedDestinations);
                        Log.d("Plan Update", "Updated Plan Destinations: " + updatedDestinations);



                        Log.d("MakePlansActivity", "Created updated Plan object: " + updatedPlan);

                        Log.d("Update Plan", "Plan ID: " + planId); // Log to ensure planId is correctly set

                        if (planId != null && !planId.isEmpty()) {
                            Log.d("UPDATING PLAN", "UPDATING PLAN");
                            logisticsViewModel.updatePlan(userId, planId, updatedPlan);
                        } else {
                            Log.e("Update Error", "Plan ID is null or empty");
                        }
                        Log.d("MakePlansActivity", "Calling ViewModel to update plan");

                        // Clear input fields after the plan is updated
                        clearInputFields();
                        Log.d("MakePlansActivity", "Cleared input fields");

                    } else {
                        Log.d("MakePlansActivity", "Input fields are empty or invalid");
                        Toast.makeText(MakePlansActivity.this, "All fields are required and at least one destination must be added", Toast.LENGTH_SHORT).show();
                    }

                    progressBar.setVisibility(View.GONE);
                    Log.d("MakePlansActivity", "Progress bar hidden");
                });

            }
        }
    }
}