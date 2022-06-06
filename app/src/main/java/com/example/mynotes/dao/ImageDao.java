package com.example.mynotes.dao;

import android.net.Uri;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.mynotes.model.ImageModel;

import java.util.List;

@Dao
public interface ImageDao {

    @Insert
    void insert(ImageModel... imageModels);

    @Query("SELECT image_location FROM image_table WHERE email=:email and noteId=:noteId")
    List<String> getAllImage(String email, long noteId);

    @Query("SELECT * FROM image_table WHERE email=:email and noteId=:noteId LIMIT 1")
    ImageModel getOneImage(String email, long noteId);
}

