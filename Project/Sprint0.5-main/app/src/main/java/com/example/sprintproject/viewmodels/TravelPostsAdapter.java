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

    /**
     * Constructs a {@code TravelPostsAdapter} with the specified list of travel posts.
     *
     * @param travelPosts the list of travel posts to display
     */
    public TravelPostsAdapter(ArrayList<Map<String, Object>> travelPosts) {
        this.travelPosts = travelPosts;
    }

    /**
     * Called when a new {@link ViewHolder} is created. Inflates the layout for a travel post item.
     *
     * @param parent   the parent view group
     * @param viewType the view type
     * @return a new {@link ViewHolder} instance
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.travel_post_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Called to bind data to a {@link ViewHolder} at a specific position.
     *
     * @param holder   the {@link ViewHolder} to bind data to
     * @param position the position of the item in the list
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map<String, Object> post = travelPosts.get(position);
        holder.durationTextView.setText("Duration: " + post.get("duration"));
        holder.destinationsTextView.setText("Destinations: " + post.get("destination"));
        holder.notesTextView.setText("Notes: " + post.get("notes"));
        boolean isBoosted = (boolean) post.getOrDefault("isBoosted", false);
        if (isBoosted) {
            holder.itemView.setBackgroundColor(
                    holder.itemView.getContext().getResources().getColor(R.color.sky_blue)); // Add color in colors.xml
        } else {
            holder.itemView.setBackgroundColor(
                    holder.itemView.getContext().getResources().getColor(android.R.color.transparent));
        }
        String details = isBoosted
                ? "[BOOSTED] Destination: " + post.get("destination")
                : "Destination: " + post.get("destination");
        holder.destinationsTextView.setText(details);

    }

    /**
     * Adds a new travel post to the adapter. Boosted posts are added to the top of the list,
     * while regular posts are added to the end.
     *
     * @param newPost the new travel post to add
     */
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

    /**
     * Returns the total number of travel posts in the list.
     *
     * @return the item count
     */
    @Override
    public int getItemCount() {
        return travelPosts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView durationTextView;
        private TextView destinationsTextView;
        private TextView notesTextView;

        /**
         * Constructs a {@code ViewHolder} and initializes references to the views in the travel post layout.
         *
         * @param itemView the view of the travel post item
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            durationTextView = itemView.findViewById(R.id.durationTextView);
            destinationsTextView = itemView.findViewById(R.id.destinationsTextView);
            notesTextView = itemView.findViewById(R.id.notesTextView);
        }

        /**
         * Returns the {@link TextView} displaying the duration.
         *
         * @return the duration {@link TextView}
         */
        public TextView getDurationTextView() {
            return durationTextView;
        }

        /**
         * Returns the {@link TextView} displaying the destinations.
         *
         * @return the destinations {@link TextView}
         */
        public TextView getDestinationsTextView() {
            return destinationsTextView;
        }

        /**
         * Returns the {@link TextView} displaying the notes.
         *
         * @return the notes {@link TextView}
         */
        public TextView getNotesTextView() {
            return notesTextView;
        }
    }
}
