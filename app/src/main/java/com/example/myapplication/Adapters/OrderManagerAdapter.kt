package com.example.myapplication.Adapters

import android.content.Context
import android.content.Intent
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Model.Order
import com.example.myapplication.R
import com.example.myapplication.activities.ProductDetail
import com.squareup.picasso.Picasso

class OrderManagerAdapter(private val context: Context, private var orderList: List<Order> ): RecyclerView.Adapter<OrderManagerAdapter.MyViewHolder>() {
    class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        val imageOM = itemView.findViewById<ImageView>(R.id.image_order)
        val statusOm = itemView.findViewById<TextView>(R.id.statusOrderText)
        val nameOm = itemView.findViewById<TextView>(R.id.orderName)
        val buyAgain = itemView.findViewById<Button>(R.id.buyAgainButton)
        val deleteOm = itemView.findViewById<ImageView>(R.id.delete_cart_order)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.order_tile, parent, false))
     }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = orderList[position]
        val list = item.productList
        val product = list?.get(0)
        if (product != null) {
            Picasso.get().load(product.productImage).placeholder(R.color.light_gray).into(holder.imageOM)
            holder.nameOm.text = product.productName
            holder.statusOm.text = item.status
        }

        holder.deleteOm.setOnClickListener{
            Toast.makeText(context, "delete clicked.", Toast.LENGTH_SHORT).show()
        }

        holder.buyAgain.setOnClickListener{
            val intent = Intent(context, ProductDetail::class.java)
            intent.putExtra("id", product?.productId)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return orderList.size
    }

}