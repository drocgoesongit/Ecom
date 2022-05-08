package com.example.myapplication.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Model.ProductListItem
import com.example.myapplication.R
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class CartAdapter(val context: Context, var productList:ArrayList<ProductListItem> ): RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    private var quantityOfItem = 0

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val name = itemView.findViewById<TextView>(R.id.product_name_cart)
        val image = itemView.findViewById<ImageView>(R.id.product_image_cart)
        val quantity = itemView.findViewById<TextView>(R.id.product_quantity_cart)
        val price = itemView.findViewById<TextView>(R.id.product_price_cart)
        val plus = itemView.findViewById<ImageView>(R.id.plus_button_cart)
        val minus = itemView.findViewById<ImageView>(R.id.minusButtonCart)
        val delete = itemView.findViewById<ImageView>(R.id.delete_cart_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.cart_tile, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = productList[position]
        holder.name.setText(item.productName)
        holder.price.setText("₹ ${item.productPrice}")
        holder.quantity.setText(item.quantity.toString())
        Picasso.get().load(item.productImage).into(holder.image)
        quantityOfItem = item.quantity!!

        holder.delete.setOnClickListener{
            val builder = MaterialAlertDialogBuilder(context, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)
            builder.setTitle("Remove selected item.")
            builder.setPositiveButton("Yes"){dialog, which ->
                FirebaseDatabase.getInstance().reference.child("Carts")
                    .child(FirebaseAuth.getInstance().uid!!)
                    .child(item.thisItemId.toString())
                    .removeValue()
                    .addOnSuccessListener {
//                        productList.removeAt(position)
                        // list is already deleting the item becaus e of firebase ig.
                        updateData(productList)
                    }
            }
            builder.setNegativeButton("No"){dialog, which ->
            }
            builder.show()

        }

        holder.plus.setOnClickListener{
            quantityOfItem= item.quantity
            if(quantityOfItem==5){
                Toast.makeText(context, "Max quantity.", Toast.LENGTH_SHORT).show()

            }else{
                quantityOfItem++
                val hashmap:HashMap<String,Any> = HashMap()
                hashmap.put("quantity", quantityOfItem)
                FirebaseDatabase.getInstance().reference.child("Carts")
                    .child(FirebaseAuth.getInstance().uid!!)
                    .child(item.thisItemId!!)
                    .updateChildren(hashmap).addOnSuccessListener {
                    }
            }

        }
        holder.minus.setOnClickListener{
            quantityOfItem = item.quantity
            if(quantityOfItem==1){
                Toast.makeText(context, "Least quantity.", Toast.LENGTH_SHORT).show()

            }else{
                quantityOfItem--
                val hashmap:HashMap<String,Any> = HashMap()
                hashmap.put("quantity", quantityOfItem)
                FirebaseDatabase.getInstance().reference.child("Carts")
                    .child(FirebaseAuth.getInstance().uid!!)
                    .child(item.thisItemId!!)
                    .updateChildren(hashmap).addOnSuccessListener {
                    }
            }


        }

    }

    override fun getItemCount(): Int {
        return productList.size
    }

    fun updateData(updatedList: ArrayList<ProductListItem>){
        this.productList = updatedList
        notifyDataSetChanged()
    }
}