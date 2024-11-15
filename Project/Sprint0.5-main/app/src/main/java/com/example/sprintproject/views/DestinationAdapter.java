package com.example.sprintproject.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sprintproject.R;
import com.example.sprintproject.model.TravelLog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.ViewHolder> {
    private List<TravelLog> travelLogs = new ArrayList<>();

    // Constructor
    public DestinationAdapter() {
    }

    // Set data method
    public void setTravelLogs(List<TravelLog> travelLogs) {
        this.travelLogs = travelLogs;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_travel_log, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TravelLog travelLog = travelLogs.get(position);
        holder.bind(travelLog);
    }

    @Override
    public int getItemCount() {
        return travelLogs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView locationTextView;
        private final TextView startDateTextView;
        private final TextView endDateTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            startDateTextView = itemView.findViewById(R.id.startDateTextView);
            endDateTextView = itemView.findViewById(R.id.endDateTextView);
        }

        public void bind(TravelLog travelLog) {
            locationTextView.setText(travelLog.getLocation());
            startDateTextView.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(travelLog.getStartDate()));
            endDateTextView.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(travelLog.getEndDate()));
        }
    }
}

