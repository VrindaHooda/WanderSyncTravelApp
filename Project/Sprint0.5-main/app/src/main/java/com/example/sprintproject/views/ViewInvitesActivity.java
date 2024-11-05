package com.example.sprintproject.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sprintproject.R;
import com.example.sprintproject.adapters.InviteAdapter;
import java.util.ArrayList;
import java.util.List;

public class ViewInvitesActivity extends AppCompatActivity {

    private ListView inviteListView;
    private InviteAdapter inviteAdapter;
    private List<String> invites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_invites);

        inviteListView = findViewById(R.id.inviteListView);

        invites = new ArrayList<>();
        invites.add("Trip to Paris");
        invites.add("Trip to Tokyo");
        invites.add("Trip to New York");

        inviteAdapter = new InviteAdapter(this, invites);
        inviteListView.setAdapter((ListAdapter) inviteAdapter);
        Button exitButton = findViewById(R.id.exitButton);
        exitButton.setOnClickListener(v -> {
            Intent intent = new Intent(ViewInvitesActivity.this, LogisticsActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
