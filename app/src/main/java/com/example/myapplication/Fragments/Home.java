package com.example.myapplication.Fragments;

import static com.example.myapplication.activities.CategoriesDetail.TAG_MAIN;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Adapters.CategoriesSmallAdapter;
import com.example.myapplication.Adapters.SmallProductAdapter;
import com.example.myapplication.activities.CartScreen;
import com.example.myapplication.Model.Banner;
import com.example.myapplication.Model.Categories;
import com.example.myapplication.Model.Groups;
import com.example.myapplication.Model.ProductClassified;
import com.example.myapplication.activities.CategoriesDetail;
import com.example.myapplication.activities.MainActivity;
import com.example.myapplication.activities.ProductDetail;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.example.myapplication.viewModel.ProductViewModel;
import com.example.myapplication.viewModel.ViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.imaginativeworld.whynotimagecarousel.listener.CarouselListener;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.util.ArrayList;
import java.util.List;

public class Home extends Fragment {

private FragmentHomeBinding binding;
private CategoriesSmallAdapter adapter;
private LinearLayoutManager llmForCategories;
private ArrayList<CarouselItem> carouselItems;
private List<Categories> categoriesList;
private ArrayList<ProductClassified> trendingList;
private SmallProductAdapter adapterForTrending;
private LinearLayoutManager llmForTrending;
private ArrayList<ProductClassified> forYouList;
private SmallProductAdapter adapterForForYou;
private LinearLayoutManager llmForForYou;
private ArrayList<CarouselItem> carouselForSpotLight;
private String spotlighItemId;
private String spotLightItemCategory;
private ViewModel dataViewModel;

    public Home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater(), container, false);

        forYouList = new ArrayList<>();
        categoriesList = new ArrayList<>();
        trendingList = new ArrayList<>();
        carouselItems = new ArrayList<>();
        carouselForSpotLight = new ArrayList<>();
        llmForCategories = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        dataViewModel = new ViewModelProvider(this).get(ViewModel.class);

        setCarousel();
        setTrending();
        setSmallCategories();
        setForYou();
        setSpotLightItem();
        delayedCheck();

        binding.cartButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CartScreen.class);
                startActivity(intent);
            }
        });
        binding.carouselSpotLight.setCarouselListener(new CarouselListener() {
            @Nullable
            @Override
            public ViewBinding onCreateViewHolder(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup) {
                return null;
            }

            @Override
            public void onBindViewHolder(@NonNull ViewBinding viewBinding, @NonNull CarouselItem carouselItem, int i) {

            }

            @Override
            public void onClick(int i, @NonNull CarouselItem carouselItem) {
                Intent intent = new Intent(getContext(), ProductDetail.class);
                intent.putExtra("id", spotlighItemId);
                intent.putExtra("category", spotLightItemCategory);
                startActivity(intent);
            }

            @Override
            public void onLongClick(int i, @NonNull CarouselItem carouselItem) {

            }
        });
        binding.carousel.setCarouselListener(new CarouselListener() {
            @Nullable
            @Override
            public ViewBinding onCreateViewHolder(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup) {
                return null;
            }

            @Override
            public void onBindViewHolder(@NonNull ViewBinding viewBinding, @NonNull CarouselItem carouselItem, int i) {

            }

            @Override
            public void onClick(int i, @NonNull CarouselItem carouselItem) {
                Intent intent = new Intent(requireContext(), CategoriesDetail.class);
                intent.putExtra("name", "offers");
                startActivity(intent);
            }

            @Override
            public void onLongClick(int i, @NonNull CarouselItem carouselItem) {

            }
        });

        return binding.getRoot();
    }

    private void setSpotLightItem() {
        FirebaseDatabase.getInstance().getReference().child("Spotlight").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    Groups item = snapshot1.getValue(Groups.class);
                    Log.i("CarouselSpot", "got item: " + item.getId());
                    if(item.getId() != null){
                        FirebaseDatabase.getInstance().getReference().child("Products").child(item.getId()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ProductClassified product = snapshot.getValue(ProductClassified.class);
                                Log.i("CarouselSpot", "got product: " + product.getName());
                                spotlighItemId = product.getId();
                                for(String category: product.getCategories()){
                                    spotLightItemCategory = category;
                                }
                                if(product.getImageList() != null){
                                    carouselForSpotLight.clear();
                                    for(String image: product.getImageList()){
                                        carouselForSpotLight.add(
                                                new CarouselItem(image)
                                        );
                                    }
                                    binding.carouselSpotLight.setData(carouselForSpotLight);
                                }
                                binding.carouselSpotLight.start();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    else{
                        Log.w(TAG_MAIN, "spotlight item missing.");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setForYou() {
        Log.i("Home", "Set For you called.");
        FirebaseDatabase.getInstance().getReference().child("ForYou").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                forYouList.clear();
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    Groups item = snapshot1.getValue(Groups.class);
                    Log.i("Trending", "for you item id " + item.getId());
                    FirebaseDatabase.getInstance().getReference().child("Products").child(item.getId())
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    ProductClassified product = snapshot.getValue(ProductClassified.class);
                                    Log.i("Trending", "for you product name " + product.getName());
                                    forYouList.add(product);
                                    adapterForForYou = new SmallProductAdapter(getContext(), forYouList, "cleats");
                                    llmForForYou = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
                                    binding.forYouRecyclerView.setAdapter(adapterForForYou);
                                    binding.forYouRecyclerView.setLayoutManager(llmForForYou);
                                    adapterForForYou.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }
                adapterForForYou = new SmallProductAdapter(getContext(), forYouList, "cleats");
                llmForForYou = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
                binding.forYouRecyclerView.setAdapter(adapterForForYou);
                binding.forYouRecyclerView.setLayoutManager(llmForForYou);
                adapterForForYou.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setSmallCategories() {
        Log.i(TAG_MAIN, "Set small categories called.");
        categoriesList.clear();
        categoriesList = dataViewModel.getCategoriesList();
        adapter = new CategoriesSmallAdapter(getContext(), categoriesList);
        Log.w(TAG_MAIN, "CategoriesList size: "+ categoriesList.size());
        binding.categoriesMainRecyclerView.setAdapter(adapter);
        binding.categoriesMainRecyclerView.setLayoutManager(llmForCategories);
        adapter.notifyDataSetChanged();

        // Backup code if list is not retrieved properly.
        if(categoriesList.size() == 0){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    categoriesList = dataViewModel.getCategoriesList();
                    adapter.setData(categoriesList);
                    Log.w(TAG_MAIN, "Categorieslist size after update: "+ categoriesList.size());
                }
            }, 3000);
        }
    }

    private void setTrending() {
        Log.i("Home", "Set Trending called.");
        FirebaseDatabase.getInstance().getReference().child("Trending").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                trendingList.clear();
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    Groups item = snapshot1.getValue(Groups.class);
                    Log.i("Trending", "trending item id " + item.getId());
                    FirebaseDatabase.getInstance().getReference().child("Products").child(item.getId())
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    ProductClassified product = snapshot.getValue(ProductClassified.class);
                                    Log.i("Trending", "trending product name " + product.getName());
                                    trendingList.add(product);
                                    adapterForTrending = new SmallProductAdapter(getContext(), trendingList, "cleats");
                                    llmForTrending = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
                                    binding.trendingRecyclerView.setAdapter(adapterForTrending);
                                    binding.trendingRecyclerView.setLayoutManager(llmForTrending);
                                    adapterForTrending.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }
                adapterForTrending = new SmallProductAdapter(getContext(), trendingList, "cleats");
                llmForTrending = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
                binding.trendingRecyclerView.setAdapter(adapterForTrending);
                binding.trendingRecyclerView.setLayoutManager(llmForTrending);
                adapterForTrending.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setCarousel() {
        Log.i("Home", "Set carousel called.");
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

    // Adding a delayed check for categories because sometimes even with three second delay we were not
    // getting the categories list properly.
    private void delayedCheck(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(categoriesList.size() == 0 ){
                    Log.i("category", "Categories size after the delayed check is null.");
                    setSmallCategories();
                }
            }
        },5000);
    }
}