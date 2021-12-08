package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.Adapters.CategoriesDetailAdapter;
import com.example.myapplication.Adapters.FacilitiesAdapter;
import com.example.myapplication.Adapters.SizeAdapter;
import com.example.myapplication.Adapters.SmallProductAdapter;
import com.example.myapplication.Model.Facilities;
import com.example.myapplication.Model.Order;
import com.example.myapplication.Model.ProductClassified;
import com.example.myapplication.Model.ProductListItem;
import com.example.myapplication.Model.Products;
import com.example.myapplication.Model.Size;
import com.example.myapplication.databinding.ActivityProductDetailBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.util.ArrayList;

public class ProductDetail extends AppCompatActivity {
    private ActivityProductDetailBinding binding;
    private String id;
    private ArrayList<CarouselItem> carouselList;
    private ArrayList<Size> sizeList;
    private LinearLayoutManager llm;
    private SizeAdapter adapterSize;
    private FacilitiesAdapter adapterForFacilites;
    private LinearLayoutManager llmForFacilitiesAdapter;
    private ArrayList<ProductClassified> similarProductsList;
    private SmallProductAdapter adapterForSimilar;
    private LinearLayoutManager llmForSimilarProduct;
    private SmallProductAdapter adapterForMoreItem;
    private ArrayList<ProductClassified> moreItemsProductList;
    private LinearLayoutManager llmForMoreItem;
    private String name;
    private String price;
    private String image;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        id = getIntent().getStringExtra("id");
        category = getIntent().getStringExtra("category");
        llm = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);

        carouselList = new ArrayList<>();
        sizeList = new ArrayList<>();
        moreItemsProductList = new ArrayList<>();
        similarProductsList = new ArrayList<>();

        setHistory();
        setFacilities();
        setActivity();
        setSimilarProduct();
        setMoreItem();

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetail.this, CartScreen.class);
                startActivity(intent);
            }
        });

        binding.buynowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog pd = new ProgressDialog(ProductDetail.this);
                pd.setMessage("Making your request");
                pd.show();
                // Adding item to fd.
                String itemId = String.valueOf(System.currentTimeMillis());
                String size = adapterSize.getSelectedItemNo();
                ProductListItem item = new ProductListItem(itemId,id, image, name, 1, Integer.parseInt(price), size);
                FirebaseDatabase.getInstance().getReference().child("Carts").
                        child(FirebaseAuth.getInstance().getUid()).
                        child(itemId)
                        .setValue(item).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        pd.dismiss();
                        Log.i("Info", "product added to cart: " + name);
                        Intent intent = new Intent(ProductDetail.this, CartScreen.class);
                        startActivity(intent);
                    }
                });
            }
        });

        binding.heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(  binding.heart.getTag().toString().equals("unfilled")){
                    binding.heart.setImageResource(R.drawable.ic_filled_heart);
                    binding.heart.setTag("filled");
                }
                else if(  binding.heart.getTag().toString().equals("filled")){
                    binding.heart.setImageResource(R.drawable.ic_unfilled_heart);
                    binding.heart.setTag("unfilled");
                }
            }
        });

    }

    private void setMoreItem() {
        FirebaseDatabase.getInstance().getReference().child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                moreItemsProductList.clear();
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    boolean hasCategory = false;
                    ProductClassified product = snapshot1.getValue(ProductClassified.class);
                    Log.i("Info", "Product name" + product.getName());
                    if(product.getCategories() != null){
                        for(String categoryName: product.getCategories()){
                            Log.i("Info", "Category name " + categoryName);
                            if(categoryName.equals(category)){
                                hasCategory = true;
                            }
                        }
                        if(!hasCategory){
                            moreItemsProductList.add(product);
                            adapterForMoreItem = new SmallProductAdapter(ProductDetail.this, moreItemsProductList, category);
                            adapterForMoreItem.notifyDataSetChanged();
                            Log.i("Info", "Product added" + product.getName() + "to more products list.");
                        }
                    }
                }
                adapterForMoreItem = new SmallProductAdapter(ProductDetail.this, moreItemsProductList, category);
                llmForMoreItem = new LinearLayoutManager(ProductDetail.this, RecyclerView.HORIZONTAL, false);
                binding.moreItemsRecyclerView.setAdapter(adapterForMoreItem);
                binding.moreItemsRecyclerView.setLayoutManager(llmForMoreItem);
                adapterForMoreItem.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProductDetail.this, "Can't load Products.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setSimilarProduct() {
        FirebaseDatabase.getInstance().getReference().child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                similarProductsList.clear();
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    ProductClassified product = snapshot1.getValue(ProductClassified.class);
                    Log.i("Info", "Product name" + product.getName());
                    if(product.getCategories() != null){
                        for(String categoryName: product.getCategories()){
                            Log.i("Info", "Category name " + categoryName);
                            if(categoryName.equals(category) && !product.getId().equals(id)){
                                similarProductsList.add(product);
                                Log.i("Info", "Product added" + product.getName() + "to similar products list.");
                            }
                        }
                    }
                }
                adapterForSimilar = new SmallProductAdapter(ProductDetail.this, similarProductsList, category);
                llmForSimilarProduct = new LinearLayoutManager(ProductDetail.this, RecyclerView.HORIZONTAL, false);
                binding.similarProductsRecyclerView.setAdapter(adapterForSimilar);
                binding.similarProductsRecyclerView.setLayoutManager(llmForSimilarProduct);
                adapterForSimilar.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProductDetail.this, "Can't load Products.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setFacilities() {
        adapterForFacilites = new FacilitiesAdapter(ProductDetail.this);
        binding.featuresRecyclerView.setAdapter(adapterForFacilites);
        llmForFacilitiesAdapter = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        binding.featuresRecyclerView.setLayoutManager(llmForFacilitiesAdapter);
    }

    private void setHistory() {
        FirebaseDatabase.getInstance().getReference().child("History").
                child(FirebaseAuth.getInstance().getUid()).
                child(String.valueOf(System.currentTimeMillis())).
                child("id").setValue(id);
    }

    private void setActivity() {
        ProgressDialog pd = new ProgressDialog(this);
        pd.show();

        FirebaseDatabase.getInstance().getReference().child("Products").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ProductClassified product = snapshot.getValue(ProductClassified.class);
                name = product.getName();
                image = product.getImage();
                price = product.getPrice();

                binding.name.setText(product.getName());
                binding.priceDetail.setText("₹ "+product.getPrice());
                binding.descriptionCategory.setText(product.getDesc());
                binding.rating.setText(product.getRating());

                //setting size.
                if(product.getSize() != null){
                    for(Size sizeItem: product.getSize()){
                        sizeList.add(sizeItem);
                    }
                    adapterSize = new SizeAdapter(ProductDetail.this, sizeList);
                    binding.recyclerViewSize.setAdapter(adapterSize);
                    binding.recyclerViewSize.setLayoutManager(llm);
                }

                // Setting Carousel.
                if(product.getImageList() == null){
                    carouselList.add(new CarouselItem(product.getImage()));
                }
                if(product.getImageList() != null){
                    for(String imageurl: product.getImageList()){
                        Log.i("Carousel", imageurl);
                        carouselList.add(
                                new CarouselItem(imageurl)
                        );
                    }
                }

                binding.carousel.setData(carouselList);
                binding.carousel.start();

                // CarouseLComplete.
                pd.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}