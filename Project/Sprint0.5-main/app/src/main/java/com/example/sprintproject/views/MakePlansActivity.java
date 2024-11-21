package com.example.sprintproject.views;

import android.os.Bundle;
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
import com.example.sprintproject.model.Plan;
import com.example.sprintproject.viewmodels.LogisticsViewModel;

import java.util.ArrayList;
import java.util.List;

import com.example.sprintproject.model.FirebaseRepository;
import com.google.firebase.auth.FirebaseAuth;


public class MakePlansActivity extends AppCompatActivity {
    private LogisticsViewModel logisticsViewModel;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private PlansAdapter plansAdapter;
    private EditText editTextDuration, editTextNotes;
    private EditText editTextLocation, editTextTransportation, editTextDiningReservations, editTextAccommodations, editTextCollaborators;
    private Button buttonAddPlan, buttonAddDestination;
    private String userId = "exampleUserId"; // Replace with actual user ID logic
    private List<Destination> destinations = new ArrayList<>();
    private List<String> collaborators = new ArrayList<>();
    private FirebaseRepository firebaseRepository;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_plans);

        firebaseRepository = new FirebaseRepository();

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

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        plansAdapter = new PlansAdapter();
        recyclerView.setAdapter(plansAdapter);

        // Initialize ViewModel
        logisticsViewModel = new ViewModelProvider(this).get(LogisticsViewModel.class);

        // Observe LiveData from ViewModel
        logisticsViewModel.getPlansLiveData().observe(this, new Observer<List<Plan>>() {
            @Override
            public void onChanged(List<Plan> plans) {
                plansAdapter.setPlans(plans);
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

        // Fetch initial plans
        logisticsViewModel.fetchPlans(userId);

        // Set up Add Destination button click listener
        buttonAddDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = editTextLocation.getText().toString();
                String transportation = editTextTransportation.getText().toString();
                String diningReservations = editTextDiningReservations.getText().toString();
                String accommodations = editTextAccommodations.getText().toString();

                if (!location.isEmpty() && !transportation.isEmpty() && !diningReservations.isEmpty() && !accommodations.isEmpty()) {
                    Destination destination = new Destination(location, parseList(accommodations), parseList(diningReservations), transportation);
                    destinations.add(destination);
                    Toast.makeText(MakePlansActivity.this, "Destination added", Toast.LENGTH_SHORT).show();
                    clearDestinationFields();
                } else {
                    Toast.makeText(MakePlansActivity.this, "All destination fields are required", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set up Add Plan button click listener
        buttonAddPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String durationText = editTextDuration.getText().toString();
                String notes = editTextNotes.getText().toString();
                String collaboratorsText = editTextCollaborators.getText().toString();

                if (!durationText.isEmpty() && !notes.isEmpty() && !destinations.isEmpty()) {
                    int duration = Integer.parseInt(durationText);
                    List<String> collaborators = parseList(collaboratorsText); // Helper function for parsing collaborators
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Get logged-in user ID

                    // Create a new Plan object
                    Plan newPlan = new Plan(duration, destinations, notes, collaborators);

                    // Add the Plan to Firestore and send invites
                    firebaseRepository.addPlan(userId, newPlan, new FirebaseRepository.PlanCallback() {
                        @Override
                        public void onPlanAdded(String planId) {
                            // Send invites to collaborators
                            for (String collaboratorId : collaborators) {
                                firebaseRepository.sendInvite(collaboratorId, planId, userId, notes);
                            }
                            Toast.makeText(MakePlansActivity.this, "Plan added and invites sent", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(String error) {
                            Toast.makeText(MakePlansActivity.this, "Failed to add plan: " + error, Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Clear input fields after the plan is added
                    clearInputFields();
                } else {
                    Toast.makeText(MakePlansActivity.this, "All fields are required and at least one destination must be added", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void clearInputFields() {
        editTextDuration.setText("");
        editTextNotes.setText("");
        editTextCollaborators.setText("");
        destinations.clear();
    }


    private void clearDestinationFields() {
        editTextLocation.setText("");
        editTextTransportation.setText("");
        editTextDiningReservations.setText("");
        editTextAccommodations.setText("");
    }

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


    // PlansAdapter class for RecyclerView
    class PlansAdapter extends RecyclerView.Adapter<PlansAdapter.PlanViewHolder> {
        private List<Plan> plans;

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

            public PlanViewHolder(View itemView) {
                super(itemView);
            }

            public void bind(Plan plan) {
            }
        }
    }
}
