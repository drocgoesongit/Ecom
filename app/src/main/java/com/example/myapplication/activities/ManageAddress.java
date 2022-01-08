package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.example.myapplication.Adapters.ManageAddressAdapter;
import com.example.myapplication.Model.Address;
import com.example.myapplication.databinding.ActivityManageAddressBinding;
import com.example.myapplication.viewModel.AddressViewModel;
import java.util.ArrayList;


public class ManageAddress extends AppCompatActivity {
private ActivityManageAddressBinding binding;
private ArrayList<Address> addressList;
private AddressViewModel addressViewModel;
private ManageAddressAdapter adapter;
private LinearLayoutManager llm;
private static final String TAG = "Address";
private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        addressViewModel = new ViewModelProvider(this).get(AddressViewModel.class);
        addressList = new ArrayList<>();
        addressList = addressViewModel.getAddressList();
        pd = new ProgressDialog(this);
        pd.setMessage("wait a minute");
        pd.show();

        getList();

        binding.addAddressButtonManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageAddress.this, AddAddress.class);
                startActivity(intent);
            }
        });

        // TODO
        // Complete Manage Address
        // Complete settings and about
        // TODO

    }

    private void getList() {
        addressList = addressViewModel.getAddressList();

        // checking if list is not empty. & if empty recalling the function 1.5 seconds later.
        if(addressList.size() != 0){
            pd.dismiss();
            setRecyclerView();
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.w(TAG, "Inside post delayed. Array list of address is empty.");
                    checkIfNull();
                    getList();
                }
            }, 1000);
        }
    }

    private void setRecyclerView() {
        if(addressList.size() != 0){
            pd.dismiss();
            adapter = new ManageAddressAdapter(this, addressList);
            binding.allAddressRecyclerviewManage.setAdapter(adapter);
            llm = new LinearLayoutManager(this);
            binding.allAddressRecyclerviewManage.setLayoutManager(llm);
        }else{
            getList();
        }
    }

    private void checkIfNull(){
        if(addressList.size() != 0){
            pd.dismiss();
            binding.noAddressGroup.setVisibility(View.INVISIBLE);
        }else{
            pd.dismiss();
            binding.noAddressGroup.setVisibility(View.VISIBLE);
        }
    }
}