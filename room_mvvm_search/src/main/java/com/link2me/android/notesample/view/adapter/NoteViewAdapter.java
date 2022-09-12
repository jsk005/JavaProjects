package com.link2me.android.notesample.view.adapter;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.link2me.android.notesample.databinding.ItemNoteBinding;
import com.link2me.android.notesample.model.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteViewAdapter extends RecyclerView.Adapter<NoteViewAdapter.ViewHolder> {
    private final String TAG = this.getClass().getSimpleName();

    private List<Note> rvItemList = new ArrayList<>();

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Note note);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public NoteViewAdapter() {

    }

    public void setNotes(List<Note> notes) {
        rvItemList = notes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNoteBinding binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewAdapter.ViewHolder holder, int position) {
        Note currentNote = rvItemList.get(position);
        holder.bindItem(currentNote, position);
    }

    @Override
    public int getItemCount() {
        return rvItemList.size();
    }

    public Note getNoteAt(int position) {
        return rvItemList.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemNoteBinding itemBinding;

        public ViewHolder(@NonNull ItemNoteBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bindItem(Note item, int position){
            itemBinding.textViewTitle.setText(item.getTitle());
            itemBinding.textViewDescription.setText(item.getDescription());
            itemBinding.textViewPriority.setText(String.valueOf(item.getPriority()));

            itemView.setOnClickListener(view -> {
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(item);
                }
            });
        }

    }
}
