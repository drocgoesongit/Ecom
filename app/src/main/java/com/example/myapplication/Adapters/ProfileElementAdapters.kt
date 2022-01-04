package com.example.myapplication.Adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.activities.CategoriesDetail.TAG_MAIN
import com.example.myapplication.Data.ArrayLists
import com.example.myapplication.R
import com.example.myapplication.activities.*

class ProfileElementAdapters(val context: Context) : RecyclerView.Adapter<ProfileElementAdapters.ViewHolder>(){

    val listOfProfileElement = ArrayLists().loadProfileElements()

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val image = itemView.findViewById<ImageView>(R.id.svg_profile_element)
        val name = itemView.findViewById<TextView>(R.id.name_profile_element)
        val desc = itemView.findViewById<TextView>(R.id.desc_profile_element)
        val layout = itemView.findViewById<ViewGroup>(R.id.profileConstraint)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.profile_element_tile, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listOfProfileElement[position]
        holder.name.setText(item.fullName)
        holder.desc.setText(item.desc)
        holder.image.setImageResource(item.image)

        holder.layout.setOnClickListener{
            when(item.name){
                "orders" -> {
                    val intent = Intent(context, ManageOrders::class.java)
                    context.startActivity(intent)
                }
                "wishlist" -> {
                    val intent = Intent(context, WishList::class.java)
                    context.startActivity(intent)
                }
                "address" -> {
                    val intent = Intent(context, ManageAddress::class.java)
                    context.startActivity(intent)
                }
                "setting" -> {
                    val intent = Intent(context, Settings::class.java)
                    context.startActivity(intent)
                }
                "about" -> {
                    val intent = Intent(context, About::class.java)
                    context.startActivity(intent)
                }
                else -> Log.w(TAG_MAIN, "Something else is clicked in profile adapter.")
            }
        }
    }

    override fun getItemCount(): Int {
        return  listOfProfileElement.size  }
}
