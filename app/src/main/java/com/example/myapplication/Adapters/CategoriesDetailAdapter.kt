package com.example.myapplication.Adapters

import android.content.Context
import android.content.Intent
import android.media.ImageReader
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Model.ProductClassified
import com.example.myapplication.Model.Products
import com.example.myapplication.ProductDetail
import com.example.myapplication.R
import com.squareup.picasso.Picasso
import kotlin.coroutines.coroutineContext

class CategoriesDetailAdapter(private val context: Context, private val productList: ArrayList<ProductClassified>, private val category: String): RecyclerView.Adapter<CategoriesDetailAdapter.ViewHolder>() {
    class ViewHolder(val view: View): RecyclerView.ViewHolder(view){
        val image = itemView.findViewById<ImageView>(R.id.image)
        val name = itemView.findViewById<TextView>(R.id.name)
        val price = itemView.findViewById<TextView>(R.id.price)
        val rating = itemView.findViewById<TextView>(R.id.ratings)
        val heart =  itemView.findViewById<ImageView>(R.id.heart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_tile, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position]
        Picasso.get().load(product.image).placeholder(R.drawable.ic_user_ecom).into(holder.image)
        holder.name.setText(product.name)
        holder.price.text = "₹ ${product.price}"
        holder.rating.text = product.rating.toString()
        holder.itemView.setOnClickListener(View.OnClickListener {
            val id = product.id
            val intent = Intent(context, ProductDetail::class.java)
            intent.putExtra("id",id)
            intent.putExtra("category", category)
            context.startActivity(intent)
        })
        holder.heart.setOnClickListener{
            if(holder.heart.getTag().equals("unfilled")){
                holder.heart.setImageResource(R.drawable.ic_filled_heart)
                holder.heart.setTag("filled")
            }else if(holder.heart.getTag().equals("filled")){
                holder.heart.setImageResource(R.drawable.ic_unfilled_heart)
                holder.heart.setTag("unfilled")
            }
        }

    }

    override fun getItemCount(): Int {
        return productList.size
    }


}