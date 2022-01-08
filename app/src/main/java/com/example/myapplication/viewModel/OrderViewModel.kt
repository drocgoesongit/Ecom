package com.example.myapplication.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.myapplication.Model.Order
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

private const val TAG = "Order"

class OrderViewModel(application: Application): AndroidViewModel(application) {
    private var allOrdersList: MutableList<Order> = mutableListOf()

    init{
        getOrdersListFromDatabase()
    }

    private fun getOrdersListFromDatabase(){
        FirebaseDatabase.getInstance().reference
            .child("Orders")
            .child(FirebaseAuth.getInstance().uid!!)
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    allOrdersList.clear()
                    for(snap in snapshot.children){
                        val order = snap.getValue(Order::class.java)
                        allOrdersList.add(order!!)
                    }
                    Log.w(TAG, "list size order in viewmodel: ${allOrdersList.size}")
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "error while getting the order list: ${error.message}")
                }
            })
    }

    fun getOrderList(): List<Order>{
        return this.allOrdersList
    }

}