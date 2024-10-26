package com.example.sprintproject.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sprintproject.R;
import java.util.List;

public class ContributorsAdapter extends RecyclerView.Adapter<ContributorsAdapter.ContributorViewHolder> {
    private List<String> contributors;

    public ContributorsAdapter(List<String> contributors) {
        this.contributors = contributors;
    }

    /**
     * Updates the list of contributors and notifies the adapter to refresh the RecyclerView.
     */
    public void updateContributors(List<String> newContributors) {
        this.contributors = newContributors;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ContributorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item_contributor layout for each item in the RecyclerView
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contributor, parent, false);
        return new ContributorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContributorViewHolder holder, int position) {
        // Bind the data (contributor name) to the ViewHolder's TextView
        String contributor = contributors.get(position);
        holder.contributorTextView.setText(contributor);
    }

    @Override
    public int getItemCount() {
        // Return the number of items in the contributors list
        return contributors.size();
    }

    /**
     * ViewHolder class for managing individual item views in the RecyclerView.
     */
    public static class ContributorViewHolder extends RecyclerView.ViewHolder {
        public TextView contributorTextView;

        public ContributorViewHolder(View itemView) {
            super(itemView);
            // Initialize the TextView that displays the contributor's name
            contributorTextView = itemView.findViewById(R.id.contributor_text);
        }
    }
}
