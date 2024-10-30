package com.example.sprintproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sprintproject.R;
import java.util.List;

public class InviteAdapter extends ArrayAdapter<String> {

    private Context context;
    private List<String> invites;

    public InviteAdapter(Context context, List<String> invites) {
        super(context, 0, invites);
        this.context = context;
        this.invites = invites;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.invite_item, parent, false);
        }

        String invite = getItem(position);
        TextView inviteText = convertView.findViewById(R.id.inviteText);
        inviteText.setText(invite);

        Button acceptButton = convertView.findViewById(R.id.acceptButton);
        Button declineButton = convertView.findViewById(R.id.declineButton);

        acceptButton.setOnClickListener(v -> {
            // Change background color or show checkmark when accepted
            acceptButton.setEnabled(false);
            Toast.makeText(context, "Invite accepted", Toast.LENGTH_SHORT).show();
        });

        declineButton.setOnClickListener(v -> {
            // Remove invite from list when declined
            invites.remove(position);
            notifyDataSetChanged();
            Toast.makeText(context, "Invite declined", Toast.LENGTH_SHORT).show();
        });

        return convertView;
    }
}
