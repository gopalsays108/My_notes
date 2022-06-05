package com.example.mynotes.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynotes.databinding.LayoutNotesRvBinding;
import com.example.mynotes.model.NotesModel;

import java.util.List;

public class ListRecyclerViewAdapter extends RecyclerView.Adapter<ListRecyclerViewAdapter.ViewHolder> {
    private final List<NotesModel> notesModelList;

    public ListRecyclerViewAdapter(List<NotesModel> notesModelList) {
        this.notesModelList = notesModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutNotesRvBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotesModel notesModel = notesModelList.get(position);
        holder.binding.titleTv.setText(notesModel.getTitle());
        holder.binding.description.setText(notesModel.getDescription());
    }

    @Override
    public int getItemCount() {
        return notesModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final LayoutNotesRvBinding binding;

        public ViewHolder(@NonNull LayoutNotesRvBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
