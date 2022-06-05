package com.example.mynotes.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_list")
public class UserModel {

    private String name;
    @NonNull
    @PrimaryKey
    private String email;
    private String mobile_no;
    private String password;


    public UserModel(String name, @NonNull String email, String mobile_no, String password) {
        this.name = name;
        this.password = password;
        this.mobile_no = mobile_no;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    @NonNull
    @Override
    public String toString() {
        return "UserModel{" +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", mobile_no='" + mobile_no + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
