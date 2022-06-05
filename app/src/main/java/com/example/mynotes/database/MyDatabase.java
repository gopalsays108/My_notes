package com.example.mynotes.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mynotes.dao.NoteDao;
import com.example.mynotes.dao.UserDao;
import com.example.mynotes.model.NotesModel;
import com.example.mynotes.model.UserModel;

@Database(entities = {UserModel.class, NotesModel.class}, version = 2)
public abstract class MyDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "UserDatabase";
    private static volatile MyDatabase INSTANCE;

    public abstract NoteDao getListDao();
    public abstract UserDao getDao();

    public static MyDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (MyDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context,
                                    MyDatabase.class,
                                    DATABASE_NAME)
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
