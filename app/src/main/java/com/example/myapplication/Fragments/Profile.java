package com.example.myapplication.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Adapters.ProfileElementAdapters;
import com.example.myapplication.Model.UserComplete;
import com.example.myapplication.R;
import com.example.myapplication.activities.LogIn;
import com.example.myapplication.activities.Settings;
import com.example.myapplication.databinding.FragmentProfileBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class Profile extends Fragment {
    private FragmentProfileBinding binding;
    private ProfileElementAdapters adapter;
    private LinearLayoutManager llm;

    public Profile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
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

        getImage();

        binding.logOutButtonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(requireActivity(), LogIn.class);
                startActivity(intent);
            }
        });

        binding.imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(requireContext(), R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_FullWidthButtons)
                        .setMessage("Edit profile?")
                        .setTitle("Go to profile settings")
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(requireContext(), Settings.class);
                                startActivity(intent);
                            }
                        }).show();
            }
        });

        return binding.getRoot();
    }

    private void getImage() {
        FirebaseDatabase.getInstance().getReference().child("UserImage").child(FirebaseAuth.getInstance().getUid())
                        .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                       if(snapshot.exists()){
                           UserComplete user = snapshot.getValue(UserComplete.class);
                           if(user.getImage() != null){
                               Picasso.get().load(user.getImage()).placeholder(R.color.light_gray).into(binding.imageProfile);
                           }
                       }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}