package com.example.sprintproject.viewmodels;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sprintproject.R;

import java.util.ArrayList;
import java.util.Map;

public class TravelPostsAdapter extends RecyclerView.Adapter<TravelPostsAdapter.ViewHolder> {

    private final ArrayList<Map<String, Object>> travelPosts;

    public TravelPostsAdapter(ArrayList<Map<String, Object>> travelPosts) {
        this.travelPosts = travelPosts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.travel_post_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map<String, Object> post = travelPosts.get(position);
        holder.durationTextView.setText("Duration: " + post.get("duration"));
        holder.destinationsTextView.setText("Destinations: " + post.get("destination"));
        holder.notesTextView.setText("Notes: " + post.get("notes"));
        boolean isBoosted = (boolean) post.getOrDefault("isBoosted", false);
        if (isBoosted) {
            holder.itemView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.sky_blue)); // Add color in colors.xml
        } else {
            holder.itemView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(android.R.color.transparent));
        }
        String details = isBoosted
                ? "[BOOSTED] Destination: " + post.get("destination")
                : "Destination: " + post.get("destination");
        holder.destinationsTextView.setText(details);

    }
    public void addPost(Map<String, Object> newPost) {
        boolean isBoosted = (boolean) newPost.getOrDefault("isBoosted", false);

        if (isBoosted) {
            // Add boosted post to the top
            travelPosts.add(0, newPost);
        } else {
            // Add regular post to the end
            travelPosts.add(newPost);
        }
        notifyDataSetChanged(); // Refresh RecyclerView
    }

    @Override
    public int getItemCount() {
        return travelPosts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView durationTextView, destinationsTextView, notesTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            durationTextView = itemView.findViewById(R.id.durationTextView);
            destinationsTextView = itemView.findViewById(R.id.destinationsTextView);
            notesTextView = itemView.findViewById(R.id.notesTextView);
        }
    }
}
