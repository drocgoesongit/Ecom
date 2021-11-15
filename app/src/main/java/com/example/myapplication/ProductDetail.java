package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.myapplication.Model.ProductClassified;
import com.example.myapplication.Model.Products;
import com.example.myapplication.Model.Size;
import com.example.myapplication.databinding.ActivityProductDetailBinding;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        id = getIntent().getStringExtra("id");

        carouselList = new ArrayList<>();
        sizeList = new ArrayList<>();

        setHistory();

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                binding.name.setText(product.getName());
                binding.priceDetail.setText("₨ "+product.getPrice());
                binding.descriptionCategory.setText(product.getDesc());
                binding.rating.setText(product.getRating());

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