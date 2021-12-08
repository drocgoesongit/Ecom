package com.example.myapplication.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Model.Facilities
import com.example.myapplication.Model.ProductListItem
import com.example.myapplication.R

class FacilitiesAdapter(val context: Context): RecyclerView.Adapter<FacilitiesAdapter.ViewHolder>() {
    var facilitiesList: List<Facilities> = listOf(
        Facilities(R.drawable.ic_ecom_cod, "Cash on delivery"),
        Facilities(R.drawable.ic_replacement_ecom, "7 Days easy replacement"),
        Facilities(R.drawable.ic_ecom_shipping, "Fast shipping")
    )

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view){
        val name = itemView.findViewById<TextView>(R.id.name_feature)
        val image = itemView.findViewById<ImageView>(R.id.image_feature)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.facilities_tiles, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = facilitiesList[position]
        holder.name.setText(item.name)
        holder.image.setImageResource(item.image)

    }

    override fun getItemCount(): Int {
        return facilitiesList.size
    }
}