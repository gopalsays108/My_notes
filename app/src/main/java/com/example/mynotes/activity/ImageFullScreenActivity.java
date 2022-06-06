package com.example.mynotes.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;

import com.example.mynotes.databinding.ActivityImageFullScreenBinding;

public class ImageFullScreenActivity extends AppCompatActivity {
    ActivityImageFullScreenBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImageFullScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("image_uri");
            binding.fullScreenImage.setImageURI(Uri.parse(value));
        }
    }
}