package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.myapplication.databinding.ActivityCartScreenBinding;

public class CartScreen extends AppCompatActivity {
private ActivityCartScreenBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        

    }
}