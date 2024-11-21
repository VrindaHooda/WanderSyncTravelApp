package com.example.sprintproject.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sprintproject.R;
import com.example.sprintproject.adapters.InviteAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewInvitesActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private String userId;
    private List<Map<String, String>> invites;
    private InviteAdapter inviteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_invites);

        // Initialize Firestore and get the logged-in user ID
        firestore = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Initialize the ListView and Adapter
        ListView inviteListView = findViewById(R.id.invite_list_view);
        invites = new ArrayList<>();
        inviteAdapter = new InviteAdapter(this, invites, firestore, userId);
        inviteListView.setAdapter(inviteAdapter);

        // Fetch invites from Firestore
        fetchInvites();
    }

    private void fetchInvites() {
        firestore.collection("users")
                .document(userId)
                .collection("invites")
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Log.e("ViewInvitesActivity", "Error fetching invites", e);
                        return;
                    }

                    if (snapshots != null) {
                        invites.clear();
                        for (DocumentSnapshot doc : snapshots.getDocuments()) {
                            Map<String, String> invite = new HashMap<>();
                            invite.put("planId", doc.getString("planId"));
                            invite.put("tripName", doc.getString("tripName"));
                            invite.put("status", doc.getString("status"));
                            invites.add(invite);
                        }
                        inviteAdapter.notifyDataSetChanged();
                    }
                });
    }
}

