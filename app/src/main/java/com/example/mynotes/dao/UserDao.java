package com.example.mynotes.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.mynotes.model.UserModel;

@Dao
public interface UserDao {

    @Insert
    void insertUser(UserModel userModel);

    @Query("SELECT EXISTS (SELECT * from user_list where email= :email)")
    boolean isTaken(String email);

    @Query("SELECT EXISTS (SELECT * FROM user_list where email=:email AND password=:password)")
    boolean login(String email, String password);

    @Query("SELECT password FROM user_list WHERE email= :email ")
    String getPassword(String email);

    @Query("SELECT name FROM user_list WHERE email= :email ")
    String getName(String email);

}
