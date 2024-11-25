package com.example.sprintproject.views;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sprintproject.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;

public class InviteAdapter extends BaseAdapter {

    private Context context;
    private List<Map<String, String>> invites;
    private FirebaseFirestore firestore;
    private String userId;

    /**
     * Constructs a new {@code InviteAdapter}.
     *
     * @param context  the application context
     * @param invites  the list of invites
     * @param firestore the Firestore instance for database operations
     * @param userId   the ID of the user
     */
    public InviteAdapter(Context context, List<Map<String,
            String>> invites, FirebaseFirestore firestore, String userId) {
        this.context = context;
        this.invites = invites;
        this.firestore = firestore;
        this.userId = userId;
    }

    /**
     * Accepts an invite by updating its status to "Accepted" in Firebase.
     *
     * @param inviteId the ID of the invite to accept
     */
    public void acceptInvite(String inviteId) {
        DatabaseReference inviteRef = FirebaseDatabase.
                getInstance().getReference("Invites").child(inviteId);
        inviteRef.child("status").setValue("Accepted");
    }

    /**
     * Declines an invite by removing it from Firebase.
     *
     * @param inviteId the ID of the invite to decline
     */
    public void declineInvite(String inviteId) {
        DatabaseReference inviteRef = FirebaseDatabase.
                getInstance().getReference("Invites").child(inviteId);
        inviteRef.removeValue();
    }


    @Override
    public int getCount() {
        return invites.size();
    }

    @Override
    public Object getItem(int position) {
        return invites.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.invite_item, parent, false);
        }

        TextView tripNameText = convertView.findViewById(R.id.trip_name_text);
        TextView statusText = convertView.findViewById(R.id.status_text);
        Button acceptButton = convertView.findViewById(R.id.accept_button);
        Button declineButton = convertView.findViewById(R.id.decline_button);

        Map<String, String> invite = invites.get(position);
        String tripName = invite.get("tripName");
        String status = invite.get("status");
        String planId = invite.get("planId");

        tripNameText.setText(tripName);
        statusText.setText(status);

        acceptButton.setOnClickListener(v -> {
            firestore.collection("users")
                    .document(userId)
                    .collection("invites")
                    .document(planId)
                    .update("status", "Accepted")
                    .addOnSuccessListener(aVoid -> {
                        statusText.setText("Accepted");
                        Toast.makeText(context, "Invite accepted", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> Log.e("InviteAdapter", "Error accepting invite", e));
        });

        declineButton.setOnClickListener(v -> {
            firestore.collection("users")
                    .document(userId)
                    .collection("invites")
                    .document(planId)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        invites.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Invite declined", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> Log.e("InviteAdapter", "Error declining invite", e));
        });

        return convertView;
    }
}
