package com.example.myapplication.Adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Model.ProductClassified
import com.example.myapplication.activities.ProductDetail
import com.example.myapplication.R
import com.squareup.picasso.Picasso

class SmallProductAdapter(val context: Context, val productList:ArrayList<ProductClassified>, val category: String) : RecyclerView.Adapter<SmallProductAdapter.ViewHolder>(){

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val image = itemView.findViewById<ImageView>(R.id.imageSmallProductTile)
        val name = itemView.findViewById<TextView>(R.id.nameSmallProductTile)
        val price = itemView.findViewById<TextView>(R.id.priceSmallProductTile)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.small_product_tile, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position]
        Log.i("Home", "product: ${product.name}")
        holder.name.setText(product.name)
        holder.price.setText("₹ ${product.price}")
        Picasso.get().load(product.image).placeholder(R.color.light_gray).into(holder.image)
        holder.itemView.setOnClickListener{
            val id = product.id
            val intent = Intent(context, ProductDetail::class.java)
            intent.putExtra("id",id)
            intent.putExtra("category", category)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return productList.size    }
}
