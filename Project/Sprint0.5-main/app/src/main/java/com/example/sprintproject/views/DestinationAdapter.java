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
import java.util.concurrent.TimeUnit;

public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.ViewHolder> {
    private List<TravelLog> travelLogs = new ArrayList<>();

    // Constructor
    /**
     * Constructs a {@code DestinationAdapter} with no initial data.
     */
    public DestinationAdapter() {
    }

    // Set data method
    /**
     * Sets the travel logs to display in the adapter and refreshes the RecyclerView.
     *
     * @param travelLogs the list of {@link TravelLog} objects to display
     */
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
        private final TextView durationTextView;

        /**
         * Constructs a {@code ViewHolder} and initializes the views for a travel log item.
         *
         * @param itemView the view for the travel log item
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            startDateTextView = itemView.findViewById(R.id.startDateTextView);
            endDateTextView = itemView.findViewById(R.id.endDateTextView);
            durationTextView = itemView.findViewById(R.id.durationTextView);
        }

        /**
         * Binds a {@link TravelLog} object to the views in the ViewHolder.
         *
         * @param travelLog the {@link TravelLog} to bind
         */
        public void bind(TravelLog travelLog) {
            locationTextView.setText(travelLog.getLocation());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            startDateTextView.setText(dateFormat.format(travelLog.getStartDate()));
            endDateTextView.setText(dateFormat.format(travelLog.getEndDate()));

            // Calculate and set the duration
            long diffInMillies = Math.abs(travelLog.getEndDate().
                    getTime() - travelLog.getStartDate().getTime());
            int duration = (int) TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            durationTextView.setText("Duration: " + duration + " days");
        }
    }
}
