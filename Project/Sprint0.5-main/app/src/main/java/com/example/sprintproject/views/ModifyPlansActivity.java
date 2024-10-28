package com.example.sprintproject.views;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sprintproject.R;
import com.example.sprintproject.viewmodels.UserDurationViewModel;
import java.util.ArrayList;
import java.util.List;

public class ModifyPlansActivity extends AppCompatActivity {

    private ListView destinationsListView;
    private Button addDestinationButton;
    private Button modifyButton;
    private Button exitButton;
    private Button open = findViewById(R.id.openStartDatePicker2);
    private Button enddate = findViewById(R.id.openEndDatePicker2);
    private TextView start = findViewById(R.id.startDateText2);
    private TextView dateend = findViewById(R.id.endDateText2);
    private List<String> destinations;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_trip_plans);

        destinationsListView = findViewById(R.id.destinations_list_view);
        addDestinationButton = findViewById(R.id.add_destination_button);
        modifyButton = findViewById(R.id.modify_button);
        exitButton = findViewById(R.id.exit_button);

        // Initialize the destination list and adapter
        destinations = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, destinations);
        destinationsListView.setAdapter(adapter);

        // Set OnClickListener for the add button
        addDestinationButton.setOnClickListener(v -> showAddDestinationDialog());

        // Set OnItemClickListener for the ListView items
        destinationsListView.setOnItemClickListener((parent, view, position, id) -> {
            showModifyDestinationDialog(destinations.get(position));
        });

        // Exit button action
        exitButton.setOnClickListener(v -> finish()); // Close the activity
    }

    private void showAddDestinationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_modify_destination, null);
        builder.setView(dialogView);

        EditText locationInput = dialogView.findViewById(R.id.locationInput);
        EditText durationInput = dialogView.findViewById(R.id.durationInput);
        TextView startDateText = dialogView.findViewById(R.id.startDateText);
        TextView endDateText = dialogView.findViewById(R.id.endDateText);
        Button saveButton = dialogView.findViewById(R.id.saveButton);

        AlertDialog dialog = builder.create();

        saveButton.setOnClickListener(v -> {
            String location = locationInput.getText().toString();
            String duration = durationInput.getText().toString();
            // Additional logic to save dates can be implemented here

            if (!location.isEmpty() && !duration.isEmpty()) {
                destinations.add(location); // Add the new destination to the list
                adapter.notifyDataSetChanged(); // Notify the adapter to refresh the ListView
                Toast.makeText(this, "Destination added!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }
    // Method to open the DatePickerDialog
    private void openDatePicker(TextView dateTextView) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create and show the DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Set the selected date in the TextView
                    dateTextView.setText("Selected Date: "
                            + selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
                }, year, month, day);
        datePickerDialog.show();
    private void showModifyDestinationDialog(String destination) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_modify_destination, null);
        builder.setView(dialogView);

        EditText locationInput = dialogView.findViewById(R.id.locationInput);
        EditText durationInput = dialogView.findViewById(R.id.durationInput);
        TextView startDateText = dialogView.findViewById(R.id.startDateText);
        TextView endDateText = dialogView.findViewById(R.id.endDateText);
        Button saveButton = dialogView.findViewById(R.id.saveButton);

        // Set current destination in the dialog
        locationInput.setText(destination);

        AlertDialog dialog = builder.create();

        saveButton.setOnClickListener(v -> {
            String location = locationInput.getText().toString();
            String duration = durationInput.getText().toString();

            if (!location.isEmpty() && !duration.isEmpty()) {
                int index = destinations.indexOf(destination);
                destinations.set(index, location); // Update the existing destination
                adapter.notifyDataSetChanged(); // Notify the adapter to refresh the ListView
                Toast.makeText(this, "Destination modified!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }
}
