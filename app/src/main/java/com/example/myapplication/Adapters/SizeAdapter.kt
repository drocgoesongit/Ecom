package com.example.myapplication.Adapters

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Model.Size
import com.example.myapplication.R

class SizeAdapter(val context: Context, val sizeList:ArrayList<Size> ): RecyclerView.Adapter<SizeAdapter.ViewHolder>() {
    var selectedItem: Int= 0
    class ViewHolder(val view: View): RecyclerView.ViewHolder(view){
        val size = itemView.findViewById<TextView>(R.id.size)
        val card = itemView.findViewById<CardView>(R.id.sizeCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.size_tile, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = sizeList[position]
        holder.size.setText(item.size)

        holder.card.setOnClickListener{
            selectedItem = position
            notifyDataSetChanged()
        }
        if(position == selectedItem) {
            holder.card.animate().scaleX(1.1f);
            holder.card.animate().scaleY(1.1f);
            holder.card.setCardBackgroundColor(Color.BLACK)
            holder.size.setTextColor(Color.WHITE)
        }else{
            holder.card.animate().scaleX(1f);
            holder.card.animate().scaleY(1f);
            holder.card.setCardBackgroundColor(Color.GRAY)
            holder.size.setTextColor(Color.BLACK);
        }
    }

    override fun getItemCount(): Int {
        return sizeList.size
    }

    fun getSelectedItemNo(): String? {
        val item = sizeList[selectedItem]
        return item.size
    }

}