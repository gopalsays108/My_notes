package com.example.mynotes.repository;

import android.app.Application;

import com.example.mynotes.dao.NoteDao;
import com.example.mynotes.database.MyDatabase;
import com.example.mynotes.model.NotesModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NoteRepository {
    private MyDatabase myDatabase;
    private NoteDao noteDao;
    private final ExecutorService service = Executors.newSingleThreadExecutor();


    public NoteRepository(Application application) {
        this.myDatabase = MyDatabase.getInstance(application);
        noteDao = this.myDatabase.getListDao();
    }

    public void insertNote(NotesModel noteModel) {
        save(noteModel, myDatabase);
    }

    public void getList(NotesModel noteModel) {
        getList(myDatabase);
    }

    private void getList(MyDatabase myDatabase) {
        service.execute(new Runnable() {
            @Override
            public void run() {
//                List<NotesModel> notesModels = noteDao.noteList();
            }


        });
    }

    void save(NotesModel notesModel, MyDatabase myDatabase) {
        service.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    noteDao.insertNote(notesModel);
                } catch (Exception e) {
                }
            }
        });

    }

}
