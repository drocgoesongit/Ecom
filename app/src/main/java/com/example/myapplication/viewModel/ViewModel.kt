package com.example.myapplication.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.myapplication.activities.CategoriesDetail.TAG_MAIN
import com.example.myapplication.Model.Categories
import com.google.firebase.database.*
import java.util.logging.Logger

class ViewModel(application: Application): AndroidViewModel(application) {

    private var categoriesList: MutableList<Categories> = mutableListOf()

    init{
        getCategoriesFromDatabase()
        getWishListFromDatabase()
        getSimilarProductFromDatabase()
        getHistoryProductFromDatabase()
    }

    private fun getCategoriesFromDatabase(){
        val Log = Logger.getLogger(TAG_MAIN)
        Log.warning("getting categories from database initiated.")
        FirebaseDatabase.getInstance().getReference("Categories").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(snapshot1 in snapshot.children){
                    val category = snapshot1.getValue(Categories::class.java)
                    if (category != null) {
                        categoriesList.add(category)
                    }
                }
                Log.warning("Categories size: ${categoriesList.size}")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.warning("Categories error: ${error.message}")
            }
        })

    }

    private fun getSimilarProductFromDatabase(){

    }

    private fun getWishListFromDatabase(){

    }

    fun getCategoriesList(): List<Categories>{
        return categoriesList
    }

    private fun getHistoryProductFromDatabase(){

    }

}