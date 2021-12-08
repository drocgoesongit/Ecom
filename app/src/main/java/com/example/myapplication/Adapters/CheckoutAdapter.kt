package com.example.myapplication.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Model.ProductListItem
import com.example.myapplication.R
import com.squareup.picasso.Picasso

class CheckoutAdapter(val context: Context, val productList:ArrayList<ProductListItem> ): RecyclerView.Adapter<CheckoutAdapter.ViewHolder>() {

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view){
        val name = itemView.findViewById<TextView>(R.id.itemName)
        val price = itemView.findViewById<TextView>(R.id.itemPrice)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.checkout_order_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = productList[position]
        holder.name.setText(item.productName)
        holder.price.setText("₹ ${item.productPrice}")
    }

    override fun getItemCount(): Int {
        return productList.size
    }
}