package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.method.CharacterPickerDialog;

import com.example.myapplication.Adapters.CartAdapter;
import com.example.myapplication.Model.Order;
import com.example.myapplication.databinding.ActivityCartScreenBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartScreen extends AppCompatActivity {
private ActivityCartScreenBinding binding;
private ArrayList<Order> orderList;
private CartAdapter adapter;
private LinearLayoutManager llm;
private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        orderList = new ArrayList<>();
        llm = new LinearLayoutManager(this);
        pd = new ProgressDialog(this);

        getOrders();

    }

    private void getOrders() {
        FirebaseDatabase.getInstance().getReference().
                child("Orders").
                child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderList.clear();
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    Order order = snapshot1.getValue(Order.class);
                    orderList.add(order);
                }
                adapter = new CartAdapter(CartScreen.this, orderList);
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