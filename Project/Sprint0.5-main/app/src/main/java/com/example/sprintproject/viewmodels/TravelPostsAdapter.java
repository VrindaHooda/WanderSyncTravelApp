package com.example.sprintproject.viewmodels;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sprintproject.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
        holder.notesTextView.setText("Notes: " + post.get("notes"));

        boolean isBoosted = (boolean) post.getOrDefault("isBoosted", false);
        if (isBoosted) {
            holder.itemView.setBackgroundColor(
                    holder.itemView.getContext().getResources().
                            getColor(R.color.sky_blue)); // Add color in colors.xml
        } else {
            holder.itemView.setBackgroundColor(
                    holder.itemView.getContext().getResources().
                            getColor(android.R.color.transparent));
        }

        String details = isBoosted
                ? "[BOOSTED] Destination: " + post.get("destination")
                : "Destination: " + post.get("destination");
        holder.destinationsTextView.setText(details);

        // Set userEmail
        String userEmail = (String) post.get("userEmail");
        if (userEmail != null) {
            holder.userEmailTextView.setText("User Email: " + userEmail);
        } else {
            holder.userEmailTextView.setText("User Email: N/A");
        }
    }

    /**
     * Adds a new travel post to the adapter. Boosted posts are added to the top of the list,
     * while regular posts are added to the end.
     *
     * @param newPost the new travel post to add
     */
    public void addPost(Map<String, Object> newPost) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date newPostDate = formatter.parse((String) newPost.get("startDate"));

            // Find the correct position to insert the new post
            int position = 0;
            while (position < travelPosts.size()) {
                Map<String, Object> existingPost = travelPosts.get(position);
                Date existingPostDate = formatter.parse((String) existingPost.get("startDate"));

                if (newPostDate.before(existingPostDate)) {
                    break;
                }
                position++;
            }

            // Insert the new post at the correct position
            travelPosts.add(position, newPost);
            notifyDataSetChanged();
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
        private TextView userEmailTextView; // New TextView for userEmail

        /**
         * Constructs a {@code ViewHolder} and initializes
         * references to the views in the travel post layout.
         *
         * @param itemView the view of the travel post item
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            durationTextView = itemView.findViewById(R.id.durationTextView);
            destinationsTextView = itemView.findViewById(R.id.destinationsTextView);
            notesTextView = itemView.findViewById(R.id.notesTextView);
            userEmailTextView = itemView.findViewById(R.id.userEmailTextView);
            // Initialize userEmailTextView
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

        /**
         * Returns the {@link TextView} displaying the user email.
         *
         * @return the userEmail {@link TextView}
         */
        public TextView getUserEmailTextView() {
            return userEmailTextView;
        }
    }
}
