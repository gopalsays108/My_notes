package com.example.mynotes.adapter;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynotes.databinding.ImageRvBinding;
import com.example.mynotes.interfaces.RecyclerViewFullImageInterface;
import com.example.mynotes.interfaces.RecyclerViewInterface;

import java.util.List;

public class PhotoRecyclerViewAdapter extends RecyclerView.Adapter<PhotoRecyclerViewAdapter.ViewHolder> {
    private final List<Uri> location;
    private final RecyclerViewFullImageInterface recyclerViewFullImageInterface;

    public PhotoRecyclerViewAdapter(List<Uri> location, RecyclerViewFullImageInterface recyclerViewFullImageInterface) {
        this.location = location;
        this.recyclerViewFullImageInterface = recyclerViewFullImageInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ImageRvBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Uri uri = location.get(position);
        if (uri != null)
            holder.binding.photoIv.setImageURI(uri);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewFullImageInterface.onItemClick(holder.getBindingAdapterPosition());
            }
        });
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
