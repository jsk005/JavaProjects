package com.link2me.android.notesample.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.link2me.android.notesample.R;
import com.link2me.android.notesample.databinding.ItemNoteBinding;
import com.link2me.android.notesample.model.Note;

public class NoteAdapter extends ListAdapter<Note, NoteAdapter.NoteHolder> {

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Note note);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public NoteAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(Note oldItem, Note newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(Note oldItem, Note newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.getPriority() == newItem.getPriority();
        }
    };

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNoteBinding binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new NoteHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Note currentNote = getItem(position);
        holder.bindItem(currentNote, position);
    }

    public Note getNoteAt(int position) {
        return getItem(position);
    }

    class NoteHolder extends RecyclerView.ViewHolder {
        ItemNoteBinding itemBinding;

        public NoteHolder(ItemNoteBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bindItem(Note item, int position){
            itemBinding.textViewTitle.setText(item.getTitle());
            itemBinding.textViewDescription.setText(item.getDescription());
            itemBinding.textViewPriority.setText(String.valueOf(item.getPriority()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

}