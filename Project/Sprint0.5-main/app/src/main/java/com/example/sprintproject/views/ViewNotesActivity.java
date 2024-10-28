package com.example.sprintproject.views;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sprintproject.R;
import java.util.ArrayList;

public class ViewNotesActivity extends AppCompatActivity {

    private ListView notesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_notes);

        notesListView = findViewById(R.id.notes_list_view);
        Button exitButton = findViewById(R.id.exitButton);

        // Sample data, replace with your actual data retrieval logic
        ArrayList<String> notes = new ArrayList<>();
        notes.add("user@example.com - This is a sample note.");
        notes.add("user2@example.com - Another sample note.");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notes);
        notesListView.setAdapter(adapter);

        exitButton.setOnClickListener(v -> finish());
    }
}
