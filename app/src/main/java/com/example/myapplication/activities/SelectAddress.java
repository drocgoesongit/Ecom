package com.example.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.myapplication.Adapters.AddressAdapter;
import com.example.myapplication.Model.Address;
import com.example.myapplication.databinding.ActivitySelectAddressBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SelectAddress extends AppCompatActivity {
private ActivitySelectAddressBinding binding;
private ArrayList<Address> addressList;
private AddressAdapter adapter;
private LinearLayoutManager linearLayoutManager;
private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        linearLayoutManager = new LinearLayoutManager(this);
        addressList = new ArrayList<>();
        pd = new ProgressDialog(this);

        getAddress();
        clickListener();

    }

    private void clickListener() {
        binding.addAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectAddress.this, AddAddress.class);
                startActivity(intent);
            }
        });

        // Proceed click
        binding.proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Address selectedAddress = addressList.get(adapter.getClickedItem());
                Intent intent = new Intent(SelectAddress.this, PaymentMode.class);
                intent.putExtra("address", selectedAddress.getId());
                startActivity(intent);
            }
        });
    }

    private void getAddress() {
        FirebaseDatabase.getInstance().getReference().child("Address").
                child(FirebaseAuth.getInstance().getUid()).
                addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                addressList.clear();
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    Address address = snapshot1.getValue(Address.class);
                    addressList.add(address);
                }
                adapter = new AddressAdapter(SelectAddress.this, addressList);
                binding.allAddressRecyclerview.setAdapter(adapter);
                binding.allAddressRecyclerview.setLayoutManager(linearLayoutManager);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}