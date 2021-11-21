package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivityAddAddressBinding;

public class AddAddress extends AppCompatActivity {
    private ActivityAddAddressBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        binding.addAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.fullNameTxt.getText().toString().equals("") || binding.fullNameTxt.getText().toString().equals(null)){
                    binding.textInputLayout.setError("enter name properly!");
                }else if(binding. cityNameTxt.getText().toString().equals("") || binding.cityNameTxt.getText().toString().equals(null)){
                    Toast.makeText(AddAddress.this, "Add city name", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}