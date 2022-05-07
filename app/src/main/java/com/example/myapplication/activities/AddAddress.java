package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.Model.Address;
import com.example.myapplication.databinding.ActivityAddAddressBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class AddAddress extends AppCompatActivity {
    private ActivityAddAddressBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.addAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.fullNameTxt.getText().toString().equals("") || binding.fullNameTxt.getText().toString().equals(null)){
                    binding.textInputLayout.setError("enter name properly!");
                }else if(binding.completeAddressText.getText().toString().equals("") || binding.completeAddressText.getText().toString().equals(null)){
                    Toast.makeText(AddAddress.this, "Add complete address", Toast.LENGTH_SHORT).show();
                }else if(binding. pincodeTxt.getText().toString().equals("") || binding.pincodeTxt.getText().toString().equals(null)){
                    Toast.makeText(AddAddress.this, "Add pin code", Toast.LENGTH_SHORT).show();
                }else if(binding. cityNameTxt.getText().toString().equals("") || binding.cityNameTxt.getText().toString().equals(null)){
                    Toast.makeText(AddAddress.this, "Add city name", Toast.LENGTH_SHORT).show();
                }else if(binding. stateTxt.getText().toString().equals("") || binding.stateTxt.getText().toString().equals(null)){
                    Toast.makeText(AddAddress.this, "Add state name", Toast.LENGTH_SHORT).show();
                }else if(binding. phoneNumberTxt.getText().toString().equals("") || binding.phoneNumberTxt.getText().toString().equals(null)){
                    Toast.makeText(AddAddress.this, "Add phone number properly", Toast.LENGTH_SHORT).show();
                }else{
                    Boolean checkBoxStatus = binding.defaultCheckBox.isChecked();
                    String id = String.valueOf(System.currentTimeMillis());
                    Address address = new Address(binding.completeAddressText.getText().toString(),
                            binding.fullNameTxt.getText().toString(), binding.phoneNumberTxt.getText().toString()
                            , binding.cityNameTxt.getText().toString(), binding.stateTxt.getText().toString(),
                            binding.pincodeTxt.getText().toString(), checkBoxStatus, binding.landmarkTxt.getText().toString(), id);
                    FirebaseDatabase.getInstance().getReference().
                            child("Address").child(FirebaseAuth.getInstance().getUid()).
                            child(id).setValue(address).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(AddAddress.this, "Address added successfully.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }

            }
        });
    }
}