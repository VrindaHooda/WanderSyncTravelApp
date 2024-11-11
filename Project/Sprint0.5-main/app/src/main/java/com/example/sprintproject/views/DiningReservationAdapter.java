package com.example.sprintproject.views;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sprintproject.databinding.ReservationListItemBinding;
import com.example.sprintproject.model.DiningReservation;
import java.util.List;

public class DiningReservationAdapter
        extends RecyclerView.Adapter<DiningReservationAdapter.ReservationViewHolder> {

    private List<DiningReservation> reservationList;

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
        holder.bind(reservation);
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    static class ReservationViewHolder extends RecyclerView.ViewHolder {
        private final ReservationListItemBinding binding;

        public ReservationViewHolder(ReservationListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(DiningReservation reservation) {
            binding.setReservation(reservation);
            binding.executePendingBindings();
        }
    }
}
