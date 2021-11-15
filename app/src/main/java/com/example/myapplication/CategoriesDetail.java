package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

import com.example.myapplication.Adapters.CategoriesDetailAdapter;
import com.example.myapplication.Model.Categories;
import com.example.myapplication.Model.ProductClassified;
import com.example.myapplication.Model.Products;
import com.example.myapplication.databinding.ActivityCategoriesDetailBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CategoriesDetail extends AppCompatActivity {
    private ActivityCategoriesDetailBinding binding;
    private ArrayList<ProductClassified> productList;
    private GridLayoutManager glm;
    private CategoriesDetailAdapter adapter;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoriesDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        category = getIntent().getStringExtra("name");
        productList = new ArrayList<>();
        glm = new GridLayoutManager(this,2);
        adapter = new CategoriesDetailAdapter(this,productList);

        binding.cartButtonCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoriesDetail.this, MainActivity.class);
                intent.putExtra("intention", "cart");
                startActivity(intent);
            }
        });

        getList();

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                    if(product.getCategories() != null){
                        for(String categoryName: product.getCategories()){
                            if(categoryName.equals(category)){
                                productList.add(product);
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