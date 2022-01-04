package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.myapplication.databinding.ActivityCheckoutBinding;

public class Checkout extends AppCompatActivity {
private ActivityCheckoutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        binding.continueShoppingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Checkout.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}