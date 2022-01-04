package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.myapplication.databinding.ActivityManageAddressBinding;

public class ManageAddress extends AppCompatActivity {
private ActivityManageAddressBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();


    }
}