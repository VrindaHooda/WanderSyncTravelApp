package com.example.sprintproject.viewmodels;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.sprintproject.R;

import java.util.List;

public class ContributorAdapter extends RecyclerView.Adapter<ContributorAdapter.ViewHolder> {

    private List<String> contributorList;
    private Context context;

    // Constructor
    public ContributorAdapter(Context context, List<String> contributorList) {
        this.context = context;
        this.contributorList = contributorList;
    }

    // ViewHolder class to hold reference to each item's view
    public static class ViewHolder extends RecyclerView.ViewHolder {
        Button btnContributor;

        public ViewHolder(View itemView) {
            super(itemView);
            btnContributor = itemView.findViewById(R.id.btn_contributor);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contributor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String contributor = contributorList.get(position);
        holder.btnContributor.setText(contributor);

        // Set an OnClickListener for the button
        holder.btnContributor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click for each item
                Toast.makeText(context, contributor + " button clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return contributorList.size();
    }
}

