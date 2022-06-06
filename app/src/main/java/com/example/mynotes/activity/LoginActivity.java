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
import com.example.mynotes.utils.Validators;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private MyDatabase myDatabase;
    private String email;
    private String password;
    private final String TAG = LoginActivity.class.getSimpleName();
    ExecutorService service = Executors.newSingleThreadExecutor();

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

    @SuppressWarnings("ConstantConditions")
    private void verifyInputs() {
        email = binding.emailId.getText().toString().trim();
        password = binding.password.getText().toString().trim();

        if (email.isEmpty()) {
            binding.emailId.setError("Please enter email id");
            binding.emailId.requestFocus();
        } else if (!Validators.validateEmailId(email)) {
            Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show();
        } else if (password.isEmpty()) {
            binding.password.setError("Please enter password");
            binding.password.requestFocus();
        } else {
            signIn();
        }
    }

    private void signIn() {
        service.execute(new Runnable() {
            @Override
            public void run() {
                String hashedPasswordFromDb = myDatabase.getDao().getPassword(email);
                if (hashedPasswordFromDb != null) {
                    boolean isVerified = PasswordHash.verifyHashPassword(password, hashedPasswordFromDb,
                            new String(PasswordHash.getNextSalt(), StandardCharsets.UTF_8));

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isVerified) saveDataToDatabase();
                            else
                                Toast.makeText(LoginActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "Email id Not found", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void saveDataToDatabase() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {
                String name = myDatabase.getDao().getName(email);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        saveUserToSharedPreferences(name, email);
                    }
                });
            }
        });
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