package com.example.myapplication.activities;

import static com.example.myapplication.activities.CategoriesDetail.TAG_MAIN;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.Adapters.CheckoutAdapter;
import com.example.myapplication.Model.Address;
import com.example.myapplication.Model.Order;
import com.example.myapplication.Model.ProductListItem;
import com.example.myapplication.databinding.ActivityPlaceOrderBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PlaceOrder extends AppCompatActivity {
private ActivityPlaceOrderBinding binding;
private String addressId;
private ProgressDialog pd;
private CheckoutAdapter adapter;
private ArrayList<ProductListItem> productList;
private LinearLayoutManager llm;
private int cartTotal = 0;
private String completeAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlaceOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        addressId = getIntent().getStringExtra("address");
        pd = new ProgressDialog(this);
        pd.setMessage("Wait a minute");
        productList = new ArrayList<>();
        llm = new LinearLayoutManager(this);

        getAddress();
        getProduct();

        binding.change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = String.valueOf(System.currentTimeMillis());
                Order order = new Order(completeAddress, productList, String.valueOf(cartTotal), "placed", FirebaseAuth.getInstance().getUid());
                FirebaseDatabase.getInstance().getReference().child("Orders").
                        child(FirebaseAuth.getInstance().getUid()).child(id).
                        setValue(order).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.w(TAG_MAIN, "Order added to database.");
                        FirebaseDatabase.getInstance().getReference().child("Carts").child(FirebaseAuth.getInstance().getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.w(TAG_MAIN, "Cart emptied.");
                                Toast.makeText(PlaceOrder.this, "Order Placed.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(PlaceOrder.this, Checkout.class);
                                startActivity(intent);
                            }
                        });
                    }
                });
            }
        });

    }

    private void getProduct() {
        FirebaseDatabase.getInstance().getReference().
                child("Carts").
                child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    ProductListItem cartItem = snapshot1.getValue(ProductListItem.class);
                    cartTotal += cartItem.getProductPrice();
                    productList.add(cartItem);
                }
                // Setting total prices.
                binding.subTotalNumber.setText(String.valueOf("₹ " + cartTotal));
                binding.totalNumber.setText(String.valueOf("₹ " + cartTotal));
                // Setting up recyclerView
                adapter = new CheckoutAdapter(PlaceOrder.this, productList);
                binding.allItemRecyclerView.setAdapter(adapter);
                binding.allItemRecyclerView.setLayoutManager(llm);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getAddress() {
        pd.show();
        FirebaseDatabase.getInstance().getReference().child("Address").child(FirebaseAuth.getInstance().getUid())
                .child(addressId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Address address = snapshot.getValue(Address.class);
                binding.nameAddressCheckout.setText(address.getName());
                binding.cityNameAddress.setText(address.getCity());
                binding.phoneNumberAddressCheckout.setText(address.getPhoneNo());
                binding.addressCheckout.setText(address.getAddress());
                completeAddress = address.getName() + "  " + address.getPhoneNo() + "  " + address.getAddress() + "  " + address.getName();
                pd.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}