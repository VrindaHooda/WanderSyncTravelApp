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
import com.google.firebase.firestore.FieldValue;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sprintproject.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;

public class InviteAdapter extends BaseAdapter {

    private Context context;
    private List<Map<String, String>> invites;
    private List<String> documentNames;
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
    public InviteAdapter(Context context, List<Map<String, String>> invites, FirebaseFirestore firestore, String userId, List<String> documentNames) {
        this.context = context;
        this.invites = invites;
        this.firestore = firestore;
        this.userId = userId;
        this.documentNames = documentNames;
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

    static class ViewHolder {
        TextView tripNameText;
        TextView statusText;
        Button acceptButton;
        Button declineButton;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.invite_item, parent, false);
            holder = new ViewHolder();
            holder.tripNameText = convertView.findViewById(R.id.trip_name_text);
            holder.statusText = convertView.findViewById(R.id.status_text);
            holder.acceptButton = convertView.findViewById(R.id.accept_button);
            holder.declineButton = convertView.findViewById(R.id.decline_button);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Map<String, String> invite = invites.get(position);
        String document = documentNames.get(position);
        String tripName = invite.get("tripName");
        String status = invite.get("status");

        holder.tripNameText.setText(tripName);
        holder.statusText.setText(status);

        // Set position as tag
        holder.acceptButton.setTag(position);
        holder.declineButton.setTag(position);;

        holder.acceptButton.setOnClickListener(v -> {
            int pos = (int) v.getTag();
            Map<String, String> currentInvite = invites.get(pos);
            String currentDocument = documentNames.get(pos);
            String planId = currentInvite.get("planId");

            // Update the invite status to "Accepted"
            firestore.collection("users")
                    .document(userId)
                    .collection("invites")
                    .document(currentDocument)
                    .update("status", "Accepted")
                    .addOnSuccessListener(aVoid -> {
                        // Add the user to the plan's collaborators list
                        firestore.collection("plans")
                                .document(planId)
                                .update("collaborators", FieldValue.arrayUnion(userId))
                                .addOnSuccessListener(aVoid2 -> {
                                    currentInvite.put("status", "Accepted");
                                    holder.statusText.setText("Accepted");
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "Invite accepted", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> Log.e("InviteAdapter", "Error updating plan collaborators", e));
                    })
                    .addOnFailureListener(e -> Log.e("InviteAdapter", "Error accepting invite", e));
        });

        holder.declineButton.setOnClickListener(v -> {
            int pos = (int) v.getTag();
            String currentDocument = documentNames.get(pos);

            firestore.collection("users")
                    .document(userId)
                    .collection("invites")
                    .document(currentDocument)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        invites.remove(pos);
                        documentNames.remove(pos);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Invite declined", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> Log.e("InviteAdapter", "Error declining invite", e));
        });

        return convertView;
    }

}
