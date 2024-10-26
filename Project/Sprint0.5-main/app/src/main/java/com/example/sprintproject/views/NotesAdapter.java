package com.example.sprintproject.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sprintproject.R;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {
    private List<String> notes; // List of notes to display

    public NotesAdapter(List<String> notes) {
        this.notes = notes;
    }

    // Method to update the list of notes
    public void updateNotes(List<String> newNotes) {
        this.notes = newNotes;
        notifyDataSetChanged(); // Refresh RecyclerView when data changes
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        String note = notes.get(position);
        holder.noteTextView.setText(note); // Display each note in a TextView
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    // ViewHolder class for individual note items
    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        public TextView noteTextView;

        public NoteViewHolder(View itemView) {
            super(itemView);
            noteTextView = itemView.findViewById(R.id.note_text);
        }
    }
}

