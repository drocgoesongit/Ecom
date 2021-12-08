package com.example.myapplication.Adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Model.Address
import com.example.myapplication.R

class AddressAdapter(val context: Context, val addressList:ArrayList<Address> ): RecyclerView.Adapter<AddressAdapter.ViewHolder>() {
    var selectedItem = 0

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view){
        val name = itemView.findViewById<TextView>(R.id.name_address)
        val phoneNo = itemView.findViewById<TextView>(R.id.phoneNumberAddress)
        val address = itemView.findViewById<TextView>(R.id.address)
        val cityName = itemView.findViewById<TextView>(R.id.cityNameAddress)
        val card = itemView.findViewById<CardView>(R.id.card)
        val tick = itemView.findViewById<ImageView>(R.id.tickImageAddress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.address_tile, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = addressList[position]
        holder.itemView.setOnClickListener{
            selectedItem = position
            notifyDataSetChanged()
        }
        holder.address.setOnClickListener{
            selectedItem = position
            notifyDataSetChanged()
        }
        holder.name.setOnClickListener{
            selectedItem = position
            notifyDataSetChanged()
        }
        holder.cityName.setOnClickListener{
            selectedItem = position
            notifyDataSetChanged()
        }

        if(position == selectedItem) {
            holder.card.animate().scaleX(1f)
            holder.card.animate().scaleY(1f)
            holder.tick.setImageResource(R.drawable.ic_ecom_tick)
        }else{
            holder.card.animate().scaleX(1f)
            holder.card.animate().scaleY(1f)
            holder.tick.setImageResource(R.drawable.dp30_lightgray_bg)
        }

        holder.name.setText(item.name)
        holder.phoneNo.setText(item.phoneNo)
        holder.address.setText(item.address)
        holder.cityName.setText(item.city)
    }

    override fun getItemCount(): Int {
        return addressList.size
    }

    // returning the selected item.
    fun getClickedItem(): Int{
        return selectedItem
    }
}