package com.example.myapplication.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Model.Size
import com.example.myapplication.R

class SizeAdapter(val context: Context, val sizeList:ArrayList<Size> ): RecyclerView.Adapter<SizeAdapter.ViewHolder>() {
    class ViewHolder(val view: View): RecyclerView.ViewHolder(view){
        val size = itemView.findViewById<TextView>(R.id.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.size_tile, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = sizeList[position]
        holder.size.setText(item.size)
    }

    override fun getItemCount(): Int {
        return sizeList.size
    }
}