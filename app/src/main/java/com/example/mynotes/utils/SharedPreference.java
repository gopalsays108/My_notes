package com.example.mynotes.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreference {
    public static final String LOGIN = "loginCheck";
    public static final String USERNAME = "userName";
    public static final String USER_EMAIL = "userEmail";


    public static String getUserName(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(USERNAME, MODE_PRIVATE);
        return preferences.getString(USERNAME, "");
    }

    public static String getUserEmail(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(USER_EMAIL, MODE_PRIVATE);
        return preferences.getString(USER_EMAIL, "");
    }

    public static void setUserName(Context context, String name) {
        SharedPreferences preference = context.getSharedPreferences(USERNAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor.putString(USERNAME, name);
        editor.apply();
    }

    public static void setUserEmail(Context context, String email) {
        SharedPreferences preference = context.getSharedPreferences(USER_EMAIL, MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor.putString(USER_EMAIL, email);
        editor.apply();
    }

    public static boolean getIsLogin(Context context) {
        SharedPreferences preference = context.getSharedPreferences(LOGIN, MODE_PRIVATE);
        return preference.getBoolean(LOGIN, false);
    }

    public static void setIsLogin(Context context, Boolean isLoggedIn) {
        SharedPreferences preference = context.getSharedPreferences(LOGIN, MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor.putBoolean(LOGIN, isLoggedIn);
        editor.apply();
    }
}
