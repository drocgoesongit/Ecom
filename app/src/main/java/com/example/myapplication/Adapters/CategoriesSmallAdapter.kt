package com.example.myapplication.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.activities.CategoriesDetail
import com.example.myapplication.Model.Categories
import com.example.myapplication.R
import com.squareup.picasso.Picasso

class CategoriesSmallAdapter(val context: Context, var list: List<Categories>) : RecyclerView.Adapter<CategoriesSmallAdapter.ViewHolder>(){

    class ViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
        val image = itemView.findViewById<ImageView>(R.id.categories_image);
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.categories_tile, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        Picasso.get().load(item.image).placeholder(R.color.light_gray).into(holder.image)
        holder.image.setOnClickListener{
            val intent = Intent(context, CategoriesDetail::class.java)
            intent.putExtra("name",item.name)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
    return list.size    }

    fun setData(updatedList: List<Categories>){
        list = updatedList
        notifyDataSetChanged()
    }
}

