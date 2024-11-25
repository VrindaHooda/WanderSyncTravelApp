package com.example.sprintproject.views;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sprintproject.R;
//import com.example.sprintproject.views.InviteAdapter;
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
    private List<String> documentNames;
    private InviteAdapter inviteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_invites);

        // Initialize Firestore and get the logged-in user ID
        firestore = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Button buttonExit2 = findViewById(R.id.buttonExit2);

        // Initialize the ListView and Adapter
        ListView inviteListView = findViewById(R.id.invite_list_view);
        invites = new ArrayList<>();
        documentNames = new ArrayList<>();
        inviteAdapter = new InviteAdapter(this, invites, firestore, userId, documentNames);
        inviteListView.setAdapter(inviteAdapter);

        // Fetch invites from Firestore
        fetchInvites();

        // Set up Exit button click listener
        buttonExit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close the activity
            }
        });
    }

    /**
     * Fetches invites for current user from the Firestore database and updates the invites list.
     *
     * This method listens for real-time updates to the "invites" collection in Firestore
     * for the specified user and populates the local list of invites. Each invite is stored
     * as a {@link Map} with details such as plan ID, trip name, and status.
     *
     * <p>If an error occurs during the fetching process, it logs the error to the console.</p>
     */
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

                            String fullPath = doc.getReference().getPath();
                            String documentName = fullPath.substring(fullPath.lastIndexOf("/") + 1);
                            documentNames.add(documentName);
                        }
                        inviteAdapter.notifyDataSetChanged();
                    }
                });
    }
}

