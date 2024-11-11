package com.example.sprintproject.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.example.sprintproject.R;
import com.example.sprintproject.databinding.LogisticsScreenBinding;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;
import java.util.List;

public class LogisticsActivity extends AppCompatActivity {

    private PieChart pieChart;
    private int totalDays;
    private int duration;
    private String finalEmail;
    private String finalUserId;
    private LogisticsScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.bottomNavigation, NavigationFragment.class, null)
                    .commit();
        }

        // Initialize data binding
        binding = DataBindingUtil.setContentView(this, R.layout.logistics_screen);
        binding.setActivity(this);

        Intent intent = getIntent();
        finalEmail = intent.getStringExtra("username");
        finalUserId = intent.getStringExtra("userId");

        pieChart = binding.pieChart;

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        totalDays = sharedPreferences.getInt("TOTAL_DAYS_KEY", 0);
        duration = sharedPreferences.getInt("PLANNED_DAYS_KEY", 0);
        Log.d("LogisticsActivity", "Retrieved totalDays: " + totalDays + ", duration: " + duration);

        //binding.modifyNotes.setOnClickListener(v -> showModifyPlansDialog());

        updatePieChart();

        binding.btnGraph.setOnClickListener(v -> {
            if (pieChart.getVisibility() == View.GONE) {
                updatePieChart();
                pieChart.setVisibility(View.VISIBLE);
                binding.btnGraph.setText("Hide Graph");
            } else {
                pieChart.setVisibility(View.GONE);
                binding.btnGraph.setText("Show Graph");
            }
        });

        //        binding.invite.setOnClickListener(v -> {
        //            Intent intent1 = new Intent(LogisticsActivity.this, AddUserActivity.class);
        //            startActivity(intent1);
        //        });
        //
        //        binding.viewInvites.setOnClickListener(v -> {
        //            Intent intent2 = new Intent(LogisticsActivity.this
        //            , ViewInvitesActivity.class);
        //            startActivity(intent2);
        //        });
        //
        //        binding.viewNotes.setOnClickListener(v -> {
        //            Intent intent3 = new Intent(LogisticsActivity.this, ViewNotesActivity.class);
        //            startActivity(intent3);
        //        });
    }

    private void showModifyPlansDialog() {
        final EditText input = new EditText(this);
        input.setHint("Type in your note here");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add A Note")
                .setView(input)
                .setPositiveButton("Save", (dialog, which) -> {
                    String newPlan = input.getText().toString().trim();
                    if (!newPlan.isEmpty()) {
                        Toast.makeText(LogisticsActivity.this,
                                "Note saved: " + newPlan, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LogisticsActivity.this,
                                "Please add a note.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
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
}
