package com.example.myapplication.Model

data class Order (
    val address: String? = null,
    val productList: ArrayList<ProductListItem>? = null,
    val total: String? = null,
    val status: String? = null,
    val consumer: String? = null,
    val orderId: String? = null
)