package com.example.mynotes.utils;

import android.content.Context;
import android.widget.Toast;

import java.util.Locale;
import java.util.regex.Pattern;

public class Validators {
    private static final String mobileNumberRegex = "[6789]([0-9]{9})";
    private static final String passwordRegex
            = "^[a-z](?=.*[a-z])(?=.*[A-Z].*[A-Z])(?=.*\\d.*\\d)(?=.+[@$#!%*?&])[A-Za-z\\d@$#!%*?&]{8,14}$";

    private static final String emailRegex = "[a-zA-Z0-9]{4,25}@[a-z]{4,25}\\.[a-z]+";

    public static boolean validatePassword(String password, String name, Context context) {
        if (password.trim().length() < 8 || password.trim().length() > 15) {
            Toast.makeText(context, "Password length should be Greater than 8 but less than 15", Toast.LENGTH_LONG).show();
            return false;
        } else if (password.toLowerCase().contains(name.toLowerCase(Locale.ROOT))) {
            Toast.makeText(context, "Password should not contain your name", Toast.LENGTH_LONG).show();
            return false;
        } else if (!Pattern.matches(passwordRegex, password)) {
            Toast.makeText(context, "Password wrong", Toast.LENGTH_SHORT).show();
            Toast.makeText(context, "First character should be lowercase, must contain at least 2 uppercase characters, 2 digits and 1 special character.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public static boolean validateMobileNumber(String mobile_no) {
        return Pattern.matches(mobileNumberRegex, mobile_no);
    }

    public static boolean validateEmailId(String email) {
        return Pattern.matches(emailRegex, email);
    }
}
