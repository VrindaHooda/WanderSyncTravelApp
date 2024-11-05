package com.example.sprintproject.views;

import android.content.Intent;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sprintproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;


public class LogisticsActivity extends AppCompatActivity {

    private PieChart pieChart;
    private int totalDays;
    private int duration;
    private String finalEmail;
    private String finalUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logistics_screen);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.bottomNavigation, NavigationFragment.class, null)
                    .commit();
        }
        Intent intent = getIntent();
        finalEmail = intent.getStringExtra("username");
        finalUserId = intent.getStringExtra("userId");
        Intent sendintent = new Intent(LogisticsActivity.this,
                DestinationActivity.class);
        sendintent.putExtra("userId", finalUserId);
        sendintent.putExtra("username", finalEmail);
        pieChart = findViewById(R.id.pieChart);
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs",
                MODE_PRIVATE);
        totalDays = sharedPreferences.getInt("TOTAL_DAYS_KEY", 0);
        duration = sharedPreferences.getInt("PLANNED_DAYS_KEY", 0);
        Log.d("LogisticsActivity", "Retrieved totalDays: " + totalDays
                + ", duration: " + duration);
        FloatingActionButton modifyPlansButton = findViewById(R.id.modify_notes);
        modifyPlansButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showModifyPlansDialog();
            }
        });
        updatePieChart();
        Button updateButton = findViewById(R.id.btn_graph);

        updateButton.setOnClickListener(v -> {
            if (pieChart.getVisibility() == View.GONE) {
                updatePieChart();
                pieChart.setVisibility(View.VISIBLE);
                updateButton.setText("Hide Graph");
            } else {
                pieChart.setVisibility(View.GONE);
                updateButton.setText("Show Graph");
            }
        });

        FloatingActionButton inviteButton = findViewById(R.id.invite);
        inviteButton.setOnClickListener(v -> {
            Intent intent1 = new Intent(LogisticsActivity.this,
                    AddUserActivity.class);
            startActivity(intent1);
        });
        FloatingActionButton viewInvitesButton = findViewById(R.id.view_invites);
        viewInvitesButton.setOnClickListener(v -> {
            Intent intent2 = new Intent(LogisticsActivity.this,
                    ViewInvitesActivity.class);
            startActivity(intent2);
        });

        FloatingActionButton viewNotesButton = findViewById(R.id.view_notes);
        viewNotesButton.setOnClickListener(v -> {
            Intent intent3 = new Intent(LogisticsActivity.this,
                    ViewNotesActivity.class);
            startActivity(intent3);
        });
    }

    private void showModifyPlansDialog() {
        final EditText input = new EditText(this);
        input.setHint("Type in your note here");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add A Note")
                .setView(input)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newPlan = input.getText().toString().trim();
                        if (!newPlan.isEmpty()) {
                            Toast.makeText(LogisticsActivity.this,
                                    "Note saved: " + newPlan, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LogisticsActivity.this,
                                    "Please add a note.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updatePieChart() {
        pieChart.clear();
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(totalDays, "Total Days: " + totalDays));
        entries.add(new PieEntry(duration, "Planned Days: " + duration));
        PieDataSet dataSet = new PieDataSet(entries, "Trip Days Overview");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setData(data);
        pieChart.setUsePercentValues(true);
        pieChart.invalidate(); // Refreshes the chart
    }

    private void listenForFirebaseUpdates() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference databaseRef = FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(userId).child("entry");
            Log.d("LogisticsActivity", "Listening for updates at path: users/"
                    + userId + "/entry");
            databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("LogisticsActivity", "DataSnapshot received: "
                            + dataSnapshot.toString());
                    if (dataSnapshot.child("totalDays").exists()
                            && dataSnapshot.child("duration").exists()) {
                        totalDays = dataSnapshot.child("totalDays").getValue(Integer.class);
                        duration = dataSnapshot.child("duration").getValue(Integer.class);
                        Log.d("LogisticsActivity", "Updated totalDays: "
                                + totalDays + ", duration: " + duration);
                        if (totalDays != 0 && duration != 0) {
                            updatePieChart();
                        } else {
                            Log.w("LogisticsActivity", "TotalDays or Duration is zero");
                            Toast.makeText(LogisticsActivity.this,
                                    "Data not found or is zero", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.d("LogisticsActivity",
                                "totalDays or duration field does not exist in the database.");
                        Toast.makeText(LogisticsActivity.this,
                                "Data not found in Firebase",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("LogisticsActivity", "Database error: "
                            + databaseError.getMessage(), databaseError.toException());
                    Toast.makeText(LogisticsActivity.this,
                            "Failed to retrieve data from Firebase",
                            Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Log.w("LogisticsActivity", "User not logged in");
            Toast.makeText(this, "User is not logged in", Toast.LENGTH_SHORT).show();
        }
    }
    protected void onResume() {
        super.onResume();
        updatePieChart();
        Intent intent = getIntent();
        finalEmail = intent.getStringExtra("username");
        finalUserId = intent.getStringExtra("userId");
        Intent sendintent = new Intent(LogisticsActivity.this,
                DestinationActivity.class);
        sendintent.putExtra("userId", finalUserId);
        sendintent.putExtra("username", finalEmail);
    }




}