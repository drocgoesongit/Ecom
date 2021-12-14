package com.example.myapplication.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Adapters.ProfileElementAdapters;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentProfileBinding;
import com.squareup.picasso.Picasso;


public class Profile extends Fragment {
    private FragmentProfileBinding binding;
    private ProfileElementAdapters adapter;
    private LinearLayoutManager llm;

    public Profile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(getLayoutInflater(), container, false);
        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/ecommercejava-afde3.appspot.com/o/Banner%2F1639502199126?alt=media&token=58c1d9a2-882e-461c-b00f-2c6e0b63c08a")
                .into(binding.backgroundImageProfile);

        // setting the recyclerView.
        adapter = new ProfileElementAdapters(getContext());
        llm = new LinearLayoutManager(getContext());
        binding.recyclerViewProfile.setAdapter(adapter);
        binding.recyclerViewProfile.setLayoutManager(llm);

        return binding.getRoot();
    }
}