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

    public AccommodationAdapter(Context context, ArrayList<Accommodation> accommodations) {
        this.context = context;
        this.accommodations = accommodations;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.accommodation_item, parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Accommodation accommodation = accommodations.get(position);
        holder.getLocation().setText(accommodation.getLocation());
        holder.getCheckInDate().setText(accommodation.getCheckInDate());
        holder.getCheckOutDate().setText(accommodation.getCheckOutDate());
        holder.getNumRooms().setText(String.valueOf(accommodation.getNumRooms()));
        holder.getRoomType().setText(accommodation.getRoomType());
        holder.statusTextView.setText(accommodation.getReservationStatus());
    }

    @Override
    public int getItemCount() {
        return accommodations.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView location;
        private TextView checkInDate;
        private TextView checkOutDate;
        private TextView numRooms;
        private TextView roomType;
        TextView statusTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            location = itemView.findViewById(R.id.location);
            checkInDate = itemView.findViewById(R.id.checkInDate);
            checkOutDate = itemView.findViewById(R.id.checkOutDate);
            numRooms = itemView.findViewById(R.id.numRooms);
            roomType = itemView.findViewById(R.id.roomType);
            statusTextView = itemView.findViewById(R.id.statusTextView);
        }

        public TextView getLocation() {
            return location;
        }

        public TextView getCheckInDate() {
            return checkInDate;
        }

        public TextView getCheckOutDate() {
            return checkOutDate;
        }

        public TextView getNumRooms() {
            return numRooms;
        }

        public TextView getRoomType() {
            return roomType;
        }
    }
}

