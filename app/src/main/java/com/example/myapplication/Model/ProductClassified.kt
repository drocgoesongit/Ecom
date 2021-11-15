package com.example.myapplication.Model

data class ProductClassified(
    val name: String? = null,
    val desc: String? = null,
    val rating: String? = null,
    val price: String? = null,
    val image: String? = null,
    val id: String? = null,
    val size: ArrayList<Size>? = null,
    val imageList: ArrayList<String>? = null,
    val categories: ArrayList<String>? = null
)