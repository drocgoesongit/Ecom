package com.example.myapplication.Data

import com.example.myapplication.Model.Categories
import com.example.myapplication.Model.ProfileElement
import com.example.myapplication.R
import com.example.myapplication.databinding.CategoriesTileBinding

class ArrayLists {
    fun loadProfileElements(): List<ProfileElement>{
        return listOf<ProfileElement>(
            ProfileElement("orders", "Your orders", R.drawable.ic_order_svg,"All your recent and old orders"),
            ProfileElement("wishlist", "Wishlist", R.drawable.ic_heart_svg_simple,"List of products you liked"),
            ProfileElement("address", "Your addresses", R.drawable.ic_location_svg,"Manage address for hassle free checkouts"),
            ProfileElement("setting", "Settings", R.drawable.ic_setting_svg,"Change username and profile photo"),
            ProfileElement("about", "About us", R.drawable.ic_about_svg,""),
        )
    }
}