package com.example.myapplication.Adapters

import android.app.ProgressDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Model.Groups
import com.example.myapplication.Model.Products
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class WishlistAdapters(private var productList: ArrayList<Products>, val context: Context): RecyclerView.Adapter<WishlistAdapters.MyViewHolder>() {
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val imageWl = itemView.findViewById<ImageView>(R.id.product_image_wl)
        val nameWl = itemView.findViewById<TextView>(R.id.product_name_wl)
        val price = itemView.findViewById<TextView>(R.id.product_price_wl)
        val deleteButton = itemView.findViewById<ImageView>(R.id.delete_cart_wl)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.wishlist_tile, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = productList[position]
        Picasso.get().load(item.image).placeholder(R.color.light_gray).into(holder.imageWl)
        holder.nameWl.text = item.name
        holder.price.text = item.price

        // Deleting the selected item.
        holder.deleteButton.setOnClickListener{
            FirebaseDatabase.getInstance().reference.child("Wishlist")
                .child(FirebaseAuth.getInstance().uid!!)
                .child(item.id.toString())
                .removeValue()
                .addOnSuccessListener {
                    productList.removeAt(position)
                    setData(productList)
                    Log.w("Wishlist", "size of list. ${productList.size}")
                    Log.w("Wishlist", "Single item removed. ${item.name}")
                }
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    fun setData(updatedList: ArrayList<Products>){
        this.productList = updatedList
        notifyDataSetChanged()
    }
}