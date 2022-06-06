package com.example.mynotes.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "image_table")
public class ImageModel {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private long noteId;

    @NonNull
    private String email;

    @NonNull
    private String image_location;

    public ImageModel(long noteId, @NonNull String email, @NonNull String image_location) {
        this.noteId = noteId;
        this.email = email;
        this.image_location = image_location;
    }

    @NonNull
    @Override
    public String toString() {
        return "ImageModel{" +
                "id=" + id +
                ", noteId=" + noteId +
                ", email='" + email + '\'' +
                ", ImageLocation='" + image_location + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getNoteId() {
        return noteId;
    }

    public void setNoteId(long noteId) {
        this.noteId = noteId;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    @NonNull
    public String getImage_location() {
        return image_location;
    }

    public void setImage_location(@NonNull String image_location) {
        this.image_location = image_location;
    }
}
