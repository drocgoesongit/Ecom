package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityPaymentModeBinding;

public class PaymentMode extends AppCompatActivity {
    private ActivityPaymentModeBinding binding;
    String addressId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentModeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        addressId = getIntent().getStringExtra("address");

        binding.constraintLayout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Intent intent = new Intent(PaymentMode.this, PlaceOrder.class);
                 intent.putExtra("address", addressId);
                 intent.putExtra("type", "prepaid");
                 startActivity(intent);
            }
        });
        binding.layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentMode.this, PlaceOrder.class);
                intent.putExtra("address", addressId);
                intent.putExtra("type", "cod");
                startActivity(intent);
            }
        });

    }
}