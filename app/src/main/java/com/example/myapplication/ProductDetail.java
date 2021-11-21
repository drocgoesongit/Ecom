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

import com.example.myapplication.Adapters.SizeAdapter;
import com.example.myapplication.Model.Order;
import com.example.myapplication.Model.ProductClassified;
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
private String name;
private String price;
private String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        id = getIntent().getStringExtra("id");
        llm = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);

        carouselList = new ArrayList<>();
        sizeList = new ArrayList<>();

        setHistory();

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
                // Adding order to fd.
                Order order = new Order(id, image, name ,price, "1", FirebaseAuth.getInstance().getUid(), "inCart");
                FirebaseDatabase.getInstance().getReference().child("Orders").
                        child(FirebaseAuth.getInstance().getUid()).
                        child(String.valueOf(System.currentTimeMillis()))
                        .setValue(order).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        pd.dismiss();
                        Log.i("Info", "Order added to cart");
                        Intent intent = new Intent(ProductDetail.this, CartScreen.class);
                        startActivity(intent);
                    }
                });
            }
        });

        setActivity();

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

    private void setHistory() {
        //FirebaseDatabase.getInstance().getReference().child("History").
//                child(FirebaseAuth.getInstance().getUid()).
//                child(String.valueOf(System.currentTimeMillis())).
//                child("id").setValue(id);
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