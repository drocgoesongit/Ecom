package com.example.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.WithHint;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.example.myapplication.Adapters.WishlistAdapters;
import com.example.myapplication.Model.ProductListItem;
import com.example.myapplication.Model.Products;
import com.example.myapplication.databinding.ActivityWishListBinding;
import com.example.myapplication.viewModel.ProductViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WishList extends AppCompatActivity {
private ActivityWishListBinding binding;
private ProductViewModel productViewModel;
private List<String> listOfWishListString;
private ArrayList<Products> wishListForAdapter;
private static final String TAG = "Wishlist";
private WishlistAdapters adapterForWishlist;
private LinearLayoutManager llm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWishListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        listOfWishListString = productViewModel.getWishList();
        wishListForAdapter = new ArrayList<>();
        Log.w(TAG,"size of wish list from viewModel: " + listOfWishListString.size());

        getProductFromDatabase();

    }

    private void getProductFromDatabase() {
        if(listOfWishListString.size() != 0){
            wishListForAdapter.clear();
            for(String productId: listOfWishListString){
                FirebaseDatabase.getInstance().getReference()
                        .child("Products")
                        .child(productId)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Products product = snapshot.getValue(Products.class);
                                wishListForAdapter.add(product);
                                if(wishListForAdapter.size() != 0){
                                    Log.w(TAG, "inside function size of list: " + wishListForAdapter.size());
                                    adapterForWishlist = new WishlistAdapters(wishListForAdapter, WishList.this);
                                    llm = new LinearLayoutManager(WishList.this);
                                    binding.recyclerViewWL.setAdapter(adapterForWishlist);
                                    binding.recyclerViewWL.setLayoutManager(llm);
                                    binding.nothingElementGroup.setVisibility(View.INVISIBLE);
                                }else{
                                    Log.w(TAG, "wishlist for product is null.");
                                }
                                Log.w(TAG, "size of product list for adapter: " + wishListForAdapter.size());
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
            }

        }else{
            Log.w(TAG, "list from ViewModel is empty.");
            // trying to wait and then calling the function again.
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.w(TAG, "post delayed method called to get the list");
                    binding.nothingElementGroup.setVisibility(View.VISIBLE);
                    getProductFromDatabase();
                }
            }, 1500);
        }

    }
}