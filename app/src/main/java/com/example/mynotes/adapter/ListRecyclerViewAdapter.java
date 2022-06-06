package com.example.mynotes.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynotes.R;
import com.example.mynotes.databinding.LayoutNotesRvBinding;
import com.example.mynotes.interfaces.RecyclerViewInterface;
import com.example.mynotes.model.ImageModel;
import com.example.mynotes.model.NotesModel;

import java.util.List;

public class ListRecyclerViewAdapter extends RecyclerView.Adapter<ListRecyclerViewAdapter.ViewHolder> {
    private final List<NotesModel> notesModelList;
    private final Context context;
    private final List<ImageModel> image;
    private final RecyclerViewInterface recyclerViewInterface;

    public ListRecyclerViewAdapter(List<NotesModel> notesModelList, Context context,
                                   List<ImageModel> image, RecyclerViewInterface recyclerViewInterface) {
        this.notesModelList = notesModelList;
        this.context = context;
        this.image = image;
        this.recyclerViewInterface = recyclerViewInterface;
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
        ImageModel imageModel = image.get(position);
        holder.binding.imageView.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.food));
        if (notesModel != null) {
            holder.binding.titleTv.setText(notesModel.getTitle());
            holder.binding.description.setText(notesModel.getDescription());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(holder.getBindingAdapterPosition()!= RecyclerView.NO_POSITION){
                        recyclerViewInterface.onItemClick(holder.getBindingAdapterPosition());
                    }
                }
            });
        }
        try {
            if (imageModel != null)
                holder.binding.imageView.setImageURI(Uri.parse(imageModel.getImage_location()));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
