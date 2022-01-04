package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.myapplication.databinding.ActivityAboutBinding;

public class About extends AppCompatActivity {
private ActivityAboutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

    }
}