package com.example.sprintproject.viewmodels;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sprintproject.R;
import com.example.sprintproject.model.Accommodation;
import java.util.ArrayList;

public class AccommodationAdapter extends RecyclerView.Adapter<AccommodationAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Accommodation> accommodations;

    /**
     * Constructs an {@code AccommodationAdapter} with the specified context and list of accommodations.
     *
     * @param context       the context in which the adapter is used
     * @param accommodations the list of accommodations to display
     */
    public AccommodationAdapter(Context context, ArrayList<Accommodation> accommodations) {
        this.context = context;
        this.accommodations = accommodations;
    }

    /**
     * Called when a new {@link ViewHolder} is created. Inflates the layout for an accommodation item.
     *
     * @param parent   the parent view group
     * @param viewType the view type
     * @return a new {@link ViewHolder} instance
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.accommodation_item, parent,
                false);
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
        Accommodation accommodation = accommodations.get(position);
        holder.getLocation().setText(accommodation.getLocation());
        holder.getCheckInDate().setText(accommodation.getCheckInDate());
        holder.getCheckOutDate().setText(accommodation.getCheckOutDate());
        holder.getNumRooms().setText(String.valueOf(accommodation.getNumRooms()));
        holder.getRoomType().setText(accommodation.getRoomType());
    }

    /**
     * Returns the total number of items in the list of accommodations.
     *
     * @return the number of items
     */
    @Override
    public int getItemCount() {
        return accommodations.size();
    }

    /**
     * The {@code ViewHolder} class holds references to the views for each accommodation item.
     * It is responsible for providing access to individual views in the layout.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView location;
        private TextView checkInDate;
        private TextView checkOutDate;
        private TextView numRooms;
        private TextView roomType;

        /**
         * Constructs a {@code ViewHolder} and initializes references to the views in the layout.
         *
         * @param itemView the view of the accommodation item
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            location = itemView.findViewById(R.id.location);
            checkInDate = itemView.findViewById(R.id.checkInDate);
            checkOutDate = itemView.findViewById(R.id.checkOutDate);
            numRooms = itemView.findViewById(R.id.numRooms);
            roomType = itemView.findViewById(R.id.roomType);
        }

        /**
         * Returns the {@link TextView} displaying the location.
         *
         * @return the location {@link TextView}
         */
        public TextView getLocation() {
            return location;
        }

        /**
         * Returns the {@link TextView} displaying the check-in date.
         *
         * @return the check-in date {@link TextView}
         */
        public TextView getCheckInDate() {
            return checkInDate;
        }

        /**
         * Returns the {@link TextView} displaying the check-out date.
         *
         * @return the check-out date {@link TextView}
         */
        public TextView getCheckOutDate() {
            return checkOutDate;
        }

        /**
         * Returns the {@link TextView} displaying the number of rooms.
         *
         * @return the number of rooms {@link TextView}
         */
        public TextView getNumRooms() {
            return numRooms;
        }

        /**
         * Returns the {@link TextView} displaying the room type.
         *
         * @return the room type {@link TextView}
         */
        public TextView getRoomType() {
            return roomType;
        }
    }
}

