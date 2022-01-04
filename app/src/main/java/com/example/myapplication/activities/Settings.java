package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.myapplication.databinding.ActivitySettingsBinding;

public class Settings extends AppCompatActivity {
private ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

    }
}