package com.example.myapplication.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Model.Order
import com.example.myapplication.Model.Size
import com.example.myapplication.R
import com.squareup.picasso.Picasso


class CartAdapter(val context: Context, val orderList:ArrayList<Order> ): RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    class ViewHolder(val view: View): RecyclerView.ViewHolder(view){
        val name = itemView.findViewById<TextView>(R.id.product_name_cart)
        val image = itemView.findViewById<ImageView>(R.id.product_image_cart)
        val quantity = itemView.findViewById<TextView>(R.id.product_quantity_cart)
        val price = itemView.findViewById<TextView>(R.id.product_price_cart)
        val plus = itemView.findViewById<ImageView>(R.id.plus_button_cart)
        val minus = itemView.findViewById<ImageView>(R.id.minusButtonCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.cart_tile, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = orderList[position]
        holder.name.setText(item.productName)
        holder.price.setText("₹ ${item.productPrice}")
        holder.quantity.setText(item.quantity)
        Picasso.get().load(item.productImage).into(holder.image)

    }

    override fun getItemCount(): Int {
        return orderList.size
    }
}