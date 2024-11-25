package com.example.sprintproject.views;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sprintproject.databinding.ReservationListItemBinding;
import com.example.sprintproject.model.DiningReservation;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DiningReservationAdapter
        extends RecyclerView.Adapter<DiningReservationAdapter.ReservationViewHolder> {

    private List<DiningReservation> reservationList;

    /**
     * Constructs a new {@code DiningReservationAdapter}.
     *
     * @param reservationList the list of {@link DiningReservation} objects to display
     */
    public DiningReservationAdapter(List<DiningReservation> reservationList) {
        this.reservationList = reservationList;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ReservationListItemBinding binding
                = ReservationListItemBinding.inflate(inflater, parent, false);
        return new ReservationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        DiningReservation reservation = reservationList.get(position);

        // Get current date and time
        Calendar today = Calendar.getInstance();

        // Set reservation date and time
        Calendar reservationDate = Calendar.getInstance();
        reservationDate.set(reservation.getYear(),
                reservation.getMonth() - 1, reservation.getDay());

        // Parse time and set it in the reservation calendar
        try {
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Date reservationTime = timeFormat.parse(reservation.getTime());
            if (reservationTime != null) {
                Calendar timeCalendar = Calendar.getInstance();
                timeCalendar.setTime(reservationTime);
                reservationDate.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY));
                reservationDate.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Check if the reservation date and time are in the past
        if (reservationDate.before(today)) {
            holder.binding.getRoot().setBackgroundColor(Color.GRAY); // Expired reservation
        } else {
            holder.binding.getRoot().setBackgroundColor(Color.GREEN); // Upcoming reservation
        }

        holder.bind(reservation);
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    static class ReservationViewHolder extends RecyclerView.ViewHolder {
        private final ReservationListItemBinding binding;

        /**
         * Constructs a new {@code ReservationViewHolder}.
         *
         * @param binding the data binding object for the list item layout
         */
        public ReservationViewHolder(ReservationListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        /**
         * Binds a {@link DiningReservation} object to the list item layout.
         *
         * @param reservation the {@link DiningReservation} to bind
         */
        public void bind(DiningReservation reservation) {
            binding.setReservation(reservation);
            binding.executePendingBindings();
        }
    }
}
