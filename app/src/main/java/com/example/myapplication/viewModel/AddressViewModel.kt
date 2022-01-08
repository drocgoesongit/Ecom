package com.example.myapplication.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.myapplication.Adapters.AddressAdapter
import com.example.myapplication.Model.Address
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AddressViewModel(application: Application): AndroidViewModel(application) {
    private var addressList: ArrayList<Address> = arrayListOf()

    init{
        getAddressListFromDatabase()
    }

    // function to get the list from database.
    private fun getAddressListFromDatabase(){
        FirebaseDatabase.getInstance().reference.child("Address")
            .child(FirebaseAuth.getInstance().uid!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    addressList.clear()
                    for (snapshot1 in snapshot.children) {
                        val address = snapshot1.getValue(
                            Address::class.java
                        )
                        addressList.add(address!!)
                    }
                    Log.w(TAG, "Size of address list in the viewModel: ${addressList.size}")
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "error while getting addressList")
                }
            })
    }

    companion object {
        private const val TAG = "Address"
    }

    // function to return the list.
    fun getAddressList(): ArrayList<Address>{
        return addressList
    }

}