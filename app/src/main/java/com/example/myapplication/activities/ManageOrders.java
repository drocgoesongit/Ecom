package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.myapplication.databinding.ActivityManageOrdersBinding;

public class ManageOrders extends AppCompatActivity {
private ActivityManageOrdersBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageOrdersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

    }
}