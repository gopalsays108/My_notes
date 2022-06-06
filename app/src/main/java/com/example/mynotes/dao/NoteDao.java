package com.example.mynotes.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.mynotes.model.NotesModel;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert
    long insertNote(NotesModel notesModel);

    @Query("SELECT * FROM notes_table WHERE email=:email")
    List<NotesModel> noteList(String email);
}
