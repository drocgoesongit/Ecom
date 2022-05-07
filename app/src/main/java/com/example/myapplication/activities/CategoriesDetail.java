package com.example.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.Adapters.CategoriesDetailAdapter;
import com.example.myapplication.Model.ProductClassified;
import com.example.myapplication.databinding.ActivityCategoriesDetailBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CategoriesDetail extends AppCompatActivity {
    private ActivityCategoriesDetailBinding binding;
    private ArrayList<ProductClassified> productList;
    private GridLayoutManager glm;
    private CategoriesDetailAdapter adapter;
    private String category;
    public static final String TAG_MAIN= "MainDebugging";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoriesDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        category = getIntent().getStringExtra("name");
        productList = new ArrayList<>();
        glm = new GridLayoutManager(this, 2);
        adapter = new CategoriesDetailAdapter(this,productList, category);

        binding.cartButtonCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoriesDetail.this, CartScreen.class);
                startActivity(intent);
            }
        });
        binding.categoryNameText.setText(category);

        getList();

        binding.searchEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUsers(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void searchUsers(String s) {
        Query query = FirebaseDatabase.getInstance().getReference().child("Products").orderByChild("name").startAt(s).endAt(s + "\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                for(DataSnapshot snap: snapshot.getChildren()){
                    ProductClassified product = snap.getValue(ProductClassified.class);
                    if(product.getCategories() != null){
                        for(String categoryName: product.getCategories()){
                            Log.i("Info", "Category name" + categoryName);
                            if(categoryName.equals(category)){
                                productList.add(product);
                                Log.i("Info", "Product added" + product.getName());
                            }
                        }
                    }
                }
                binding.recyclerView.setAdapter(adapter);
                binding.recyclerView.setLayoutManager(glm);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getList() {
        FirebaseDatabase.getInstance().getReference().child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    ProductClassified product = snapshot1.getValue(ProductClassified.class);
                    Log.i("Info", "Product name" + product.getName());
                    if(product.getCategories() != null){
                        for(String categoryName: product.getCategories()){
                            Log.i("Info", "Category name" + categoryName);
                            if(categoryName.equals(category)){
                                productList.add(product);
                                Log.i("Info", "Product added" + product.getName());
                            }
                        }
                    }
                }
                binding.recyclerView.setAdapter(adapter);
                binding.recyclerView.setLayoutManager(glm);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CategoriesDetail.this, "Can't load Products.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}