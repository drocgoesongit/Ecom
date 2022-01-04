package com.example.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.Adapters.CartAdapter;
import com.example.myapplication.Model.Address;
import com.example.myapplication.Model.ProductListItem;
import com.example.myapplication.databinding.ActivityCartScreenBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartScreen extends AppCompatActivity {
    private ActivityCartScreenBinding binding;
    private ArrayList<ProductListItem> cartItemList;
    private CartAdapter adapter;
    private LinearLayoutManager llm;
    private ProgressDialog pd;
    private ArrayList<Address> addressList;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        addressList = new ArrayList<>();
        cartItemList = new ArrayList<>();
        llm = new LinearLayoutManager(this);
        pd = new ProgressDialog(this);

        getOrders();
        getAddress();

        binding.checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cartItemList.size() != 0){
                    Intent intent = new Intent(CartScreen.this, SelectAddress.class );
                    startActivity(intent);
                }else{
                    Toast.makeText(CartScreen.this, "Add items to cart first.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getAddress() {
        FirebaseDatabase.getInstance().getReference().child("Address").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                addressList.clear();
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    Address address = snapshot1.getValue(Address.class);
                    addressList.add(address);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getOrders() {
        FirebaseDatabase.getInstance().getReference().
                child("Carts").
                child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartItemList.clear();
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    ProductListItem cartItem = snapshot1.getValue(ProductListItem.class);
                    cartItemList.add(cartItem);
                }
                adapter = new CartAdapter(CartScreen.this, cartItemList);
                binding.cartRecyclerView.setAdapter(adapter);
                binding.cartRecyclerView.setLayoutManager(llm);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}