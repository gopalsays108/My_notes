package com.example.mynotes.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;import java.io.Serializable;


@Entity(tableName = "notes_table")
public class NotesModel implements Serializable {
    @PrimaryKey(autoGenerate = true)
    int id;
    @NonNull
    String email;
    String title;
    String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public NotesModel(@NonNull String email, String title, String description) {
        this.email = email;
        this.title = title;
        this.description = description;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NonNull
    @Override
    public String toString() {
        return "NotesModel{" +
                "email='" + email + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
