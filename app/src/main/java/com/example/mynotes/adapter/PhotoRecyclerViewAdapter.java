package com.example.mynotes.adapter;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynotes.databinding.ImageRvBinding;

import java.util.List;

public class PhotoRecyclerViewAdapter extends RecyclerView.Adapter<PhotoRecyclerViewAdapter.ViewHolder> {
    private final List<Uri> location;

    public PhotoRecyclerViewAdapter(List<Uri> location) {
        this.location = location;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ImageRvBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Uri notesModel = location.get(position);
        holder.binding.photoIv.setImageURI(notesModel);
    }

    @Override
    public int getItemCount() {
        return location.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageRvBinding binding;

        public ViewHolder(@NonNull ImageRvBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
