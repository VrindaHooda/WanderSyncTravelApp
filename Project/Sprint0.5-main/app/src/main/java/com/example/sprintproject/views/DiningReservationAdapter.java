package com.example.sprintproject.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sprintproject.R;
import com.example.sprintproject.model.DiningReservation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DiningReservationAdapter extends RecyclerView.Adapter<DiningReservationAdapter.ViewHolder> {

    private List<DiningReservation> reservations = new ArrayList<>();

    public void setReservations(List<DiningReservation> reservations) {
        this.reservations = reservations;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DiningReservationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your item layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reservation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiningReservationAdapter.ViewHolder holder, int position) {
        // Bind data to the item views
        DiningReservation reservation = reservations.get(position);
        holder.bind(reservation);
    }

    @Override
    public int getItemCount() {
        return reservations != null ? reservations.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Define your item views
        TextView restaurantNameTextView;
        TextView reservationDateTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            // Initialize item views
            restaurantNameTextView = itemView.findViewById(R.id.restaurantNameTextView);
            reservationDateTextView = itemView.findViewById(R.id.reservationDateTextView);

            // Handle item click or long click events if needed
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // Implement deletion or other actions
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        DiningReservation reservation = reservations.get(position);
                        // Call a method to delete the reservation
                        deleteReservation(reservation);
                    }
                    return true;
                }
            });
        }

        public void bind(DiningReservation reservation) {
            // Set data to views
            restaurantNameTextView.setText(reservation.getRestaurantName());

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy, HH:mm");
            String dateString = dateFormat.format(reservation.getReservationDate());
            reservationDateTextView.setText(dateString);
        }

        private void deleteReservation(DiningReservation reservation) {
            // Implement deletion logic
            // For example, call a method in the activity or ViewModel
            Toast.makeText(itemView.getContext(), "Delete " + reservation.getRestaurantName(), Toast.LENGTH_SHORT).show();
            // You can pass the reservation to the activity via an interface or callback
        }
    }
}
