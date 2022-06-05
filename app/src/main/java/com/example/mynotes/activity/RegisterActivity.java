package com.example.mynotes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mynotes.MainActivity;
import com.example.mynotes.database.MyDatabase;
import com.example.mynotes.databinding.ActivityRegisterBinding;
import com.example.mynotes.model.UserModel;
import com.example.mynotes.utils.PasswordHash;
import com.example.mynotes.utils.PasswordValidation;
import com.example.mynotes.utils.SharedPreference;

import java.nio.charset.StandardCharsets;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private MyDatabase myDatabase;
    private UserModel userModel;
    private boolean isTaken;
    private String name;
    private String email;
    private String password;
    private String mobile_no;
    private final String TAG = RegisterActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        myDatabase = MyDatabase.getInstance(getApplicationContext());

        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyInputs();
            }
        });

        binding.gotoSignInPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
    }

    private void verifyInputs() {
        name = binding.name.getText().toString();
        email = binding.emailId.getText().toString();
        mobile_no = binding.mobile.getText().toString();
        password = binding.password.getText().toString();

        if (name.isEmpty()) {
            binding.name.setError("Please enter name");
            binding.name.requestFocus();
        } else if (email.isEmpty()) {
            binding.emailId.setError("Please enter email id");
            binding.emailId.requestFocus();
        } else if (mobile_no.isEmpty()) {
            binding.mobile.setError("Please enter mobile no");
            binding.mobile.requestFocus();
        } else if (mobile_no.length() < 10) {
            Toast.makeText(this, "Please enter 10 digits mobile number", Toast.LENGTH_SHORT).show();
        } else if (password.isEmpty()) {
            binding.password.setError("Please enter password");
            binding.password.requestFocus();
        } else if (PasswordValidation.validatePassword(password)) {
            register();
        }
    }

    private void register() {
        String hashedPassword = PasswordHash.hashPassword(password,
                new String(PasswordHash.getNextSalt(), StandardCharsets.UTF_8));

        if (!myDatabase.getDao().isTaken(email)) {
            myDatabase.getDao().insertUser(new UserModel(name, email, mobile_no, hashedPassword));
            saveUserToSharedPreferences(name, email);
        } else {
            Toast.makeText(this, "User Already exist", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveUserToSharedPreferences(String name, String email) {
        SharedPreference.setIsLogin(getApplicationContext(), true);
        SharedPreference.setUserEmail(getApplicationContext(), email);
        SharedPreference.setUserName(getApplicationContext(), name);
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}