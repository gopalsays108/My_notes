package com.example.mynotes.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.example.mynotes.dao.UserDao;
import com.example.mynotes.database.MyDatabase;
import com.example.mynotes.model.UserModel;
//todo: Implement repository class for signup
public class UserRepository {
    private MyDatabase myDatabase;
    private UserModel userModel;

    public UserRepository(Application application) {
        this.myDatabase = MyDatabase.getInstance(application);
    }

    public void insertUser(UserModel userModel) {
        new InsertAsyncTask(myDatabase).execute(userModel);
    }

    static class InsertAsyncTask extends AsyncTask<UserModel, Void, Void> {

        private UserDao crewsDao;

        InsertAsyncTask(MyDatabase crewDatabase) {
            crewsDao = crewDatabase.getDao();
        }

        @Override
        protected Void doInBackground(UserModel... lists) {
            Log.d("TAG", "insertCrew: bg " + lists[0]);
            crewsDao.insertUser(lists[0]);
            return null;
        }

    }

    public void isTaken(String email) {
        new IsTakenAsyncTask(myDatabase).execute(email);
    }

    static class IsTakenAsyncTask extends AsyncTask<String, Void, Void> {

        private UserDao crewsDao;

        IsTakenAsyncTask(MyDatabase crewDatabase) {
            crewsDao = crewDatabase.getDao();
        }

        @Override
        protected Void doInBackground(String... email) {
            crewsDao.isTaken(email[0]);
            return null;
        }
    }
}
