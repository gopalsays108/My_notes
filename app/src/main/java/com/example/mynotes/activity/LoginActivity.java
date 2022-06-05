package com.example.mynotes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mynotes.MainActivity;
import com.example.mynotes.database.MyDatabase;
import com.example.mynotes.databinding.ActivityLoginBinding;
import com.example.mynotes.utils.PasswordHash;
import com.example.mynotes.utils.SharedPreference;

import java.nio.charset.StandardCharsets;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private MyDatabase myDatabase;
    private String email;
    private String password;
    private final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        myDatabase = MyDatabase.getInstance(getApplicationContext());
        binding.signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyInputs();
            }
        });

        binding.gotoSignUpPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                finish();
            }
        });
    }

    private void verifyInputs() {
        email = binding.emailId.getText().toString().trim();
        password = binding.password.getText().toString().trim();

        if (email.isEmpty()) {
            binding.emailId.setError("Please enter email id");
            binding.emailId.requestFocus();
        } else if (password.isEmpty()) {
            binding.password.setError("Please enter password");
            binding.password.requestFocus();
        } else {
            signIn();
        }
    }

    private void signIn() {
        String hashedPasswordFromDb = myDatabase.getDao().getPassword(email);
        boolean isVerified = PasswordHash.verifyHashPassword(password, hashedPasswordFromDb,
                new String(PasswordHash.getNextSalt(), StandardCharsets.UTF_8));

        if (isVerified) saveDataToDatabase();
        else Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
    }

    private void saveDataToDatabase() {
        String name = myDatabase.getDao().getName(email);
        saveUserToSharedPreferences(name, email);
    }

    private void saveUserToSharedPreferences(String name, String email) {
        SharedPreference.setIsLogin(getApplicationContext(), true);
        SharedPreference.setUserEmail(getApplicationContext(), email);
        SharedPreference.setUserName(getApplicationContext(), name);

        Toast.makeText(this, "Login success", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}