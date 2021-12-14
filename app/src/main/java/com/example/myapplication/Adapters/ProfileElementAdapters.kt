package com.example.myapplication.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Data.ArrayLists
import com.example.myapplication.R

class ProfileElementAdapters(val context: Context) : RecyclerView.Adapter<ProfileElementAdapters.ViewHolder>(){

    val listOfProfileElement = ArrayLists().loadProfileElements()

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val image = itemView.findViewById<ImageView>(R.id.svg_profile_element)
        val name = itemView.findViewById<TextView>(R.id.name_profile_element)
        val desc = itemView.findViewById<TextView>(R.id.desc_profile_element)
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
    }

    override fun getItemCount(): Int {
        return  listOfProfileElement.size  }
}
