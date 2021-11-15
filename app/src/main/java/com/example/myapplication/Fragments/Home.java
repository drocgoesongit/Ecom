package com.example.myapplication.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Adapters.CategoriesDetailAdapter;
import com.example.myapplication.Adapters.CategoriesSmallAdapter;
import com.example.myapplication.CategoriesDetail;
import com.example.myapplication.Model.Banner;
import com.example.myapplication.Model.Categories;
import com.example.myapplication.Model.Groups;
import com.example.myapplication.Model.ProductClassified;
import com.example.myapplication.Model.Products;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.util.ArrayList;

public class Home extends Fragment {
private FragmentHomeBinding binding;
private CategoriesSmallAdapter adapter;
private CategoriesDetailAdapter trendingAdapter;
private LinearLayoutManager llmForCategories;
private ArrayList<CarouselItem> carouselItems;
private ArrayList<ProductClassified> trendingList;
private ArrayList<Categories> categoriesList;
private GridLayoutManager glmForTrending;

    public Home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater(), container, false);
        llmForCategories = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        trendingList = new ArrayList<>();
        categoriesList = new ArrayList<>();
        glmForTrending = new GridLayoutManager(getContext(), 2);

        binding.cartButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        carouselItems = new ArrayList<>();

        setCarousel();
        setTrending();
        setSmallCategories();

        return binding.getRoot();
    }

    private void setSmallCategories() {
        FirebaseDatabase.getInstance().getReference().child("Categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoriesList.clear();
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    Categories category = snapshot1.getValue(Categories.class);
                    categoriesList.add(category);
                }
                adapter = new CategoriesSmallAdapter(getContext(), categoriesList);
                binding.categoriesMainRecyclerView.setAdapter(adapter);
                binding.categoriesMainRecyclerView.setLayoutManager(llmForCategories);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setTrending() {
        FirebaseDatabase.getInstance().getReference().child("Trending").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                trendingList.clear();
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    Groups groups = snapshot1.getValue(Groups.class );
                    Log.i("Home", groups.getId());
                    FirebaseDatabase.getInstance().getReference().child("Products").child(groups.getId()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ProductClassified products = snapshot.getValue(ProductClassified.class);
                            Log.i("Home", products.getName());
                            trendingList.add(products);
                            trendingAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
                trendingAdapter = new CategoriesDetailAdapter(requireContext(),trendingList);
                binding.trendingRecyclerView.setAdapter(trendingAdapter);
                binding.trendingRecyclerView.setHasFixedSize(true);
                binding.trendingRecyclerView.stopScroll();
                binding.trendingRecyclerView.setLayoutManager(glmForTrending);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void setCarousel() {
        FirebaseDatabase.getInstance().getReference().child("Banner").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                carouselItems.clear();
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    Banner banner = snapshot1.getValue(Banner.class);
                    carouselItems.add(
                            new CarouselItem(banner.getImageUrl())
                    );
                }
                binding.carousel.setData(carouselItems);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.carousel.start();
    }
}