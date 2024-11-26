package com.example.sprintproject.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sprintproject.R;
import com.example.sprintproject.model.DiningReservation;

import java.util.ArrayList;
import java.util.List;

public class DiningReservationAdapter extends
        RecyclerView.Adapter<DiningReservationAdapter.DiningReservationViewHolder> {

    private List<DiningReservation> reservations = new ArrayList<>();

    @NonNull
    @Override
    public DiningReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_reservation, parent, false);
        return new DiningReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiningReservationViewHolder holder, int position) {
        DiningReservation reservation = reservations.get(position);
        holder.nameTextView.setText(reservation.getRestaurantName());
        holder.websiteTextView.setText(reservation.getWebsite());
        holder.ratingBar.setRating(reservation.getRating());
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    public void setReservations(List<DiningReservation> reservations) {
        this.reservations = reservations;
        notifyDataSetChanged();
    }

    static class DiningReservationViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView websiteTextView;
        private RatingBar ratingBar;

        public TextView getNameTextView() {
            return nameTextView;
        }

        public TextView getWebsiteTextView() {
            return websiteTextView;
        }

        public RatingBar getRatingBar() {
            return ratingBar;
        }

        public DiningReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.restaurantNameTextView);
            websiteTextView = itemView.findViewById(R.id.websiteTextView);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }
}