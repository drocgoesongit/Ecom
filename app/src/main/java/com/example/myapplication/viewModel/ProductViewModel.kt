package com.example.myapplication.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.myapplication.Model.Groups
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

private const val TAG: String = "ProductViewModel"

class ProductViewModel(application: Application): AndroidViewModel(application) {
    private var wishList: MutableList<String> = mutableListOf()
    private var historyList: MutableList<String> = mutableListOf()

    init{
        getWishListFromDatabase()
        getHistoryFromDatabase()
    }

    // fun to get wishList from the database.
    private fun getWishListFromDatabase(){
        FirebaseDatabase.getInstance().reference.child("Wishlist")
            .child(FirebaseAuth.getInstance().uid!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    wishList.clear()
                    for (snapshot1 in snapshot.children) {
                        val item = snapshot1.getValue(
                            Groups::class.java
                        )
                        wishList.add(item!!.id.toString())
                    }
                    Log.w(
                        TAG,
                        "wishList item added, size: ${wishList.size}"
                    )
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "error while getting wishList: ${error.message}")
                }
            })
    }

    // fun to get historyList from the database.
    private fun getHistoryFromDatabase(){
        FirebaseDatabase.getInstance().reference.child("History")
            .child(FirebaseAuth.getInstance().uid!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    historyList.clear()
                    for (snapshot1 in snapshot.children) {
                        val item = snapshot1.getValue(
                            Groups::class.java
                        )
                        historyList.add(item!!.id.toString())
                    }
                    Log.w(
                        TAG,
                        "history item added, size: ${historyList.size}"
                    )
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "error while getting history: ${error.message}")
                }
            })
    }

    fun getWishList(): List<String>{
        return wishList
    }
}