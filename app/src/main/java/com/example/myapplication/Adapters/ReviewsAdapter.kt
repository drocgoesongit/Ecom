package com.example.myapplication.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Model.Review
import com.example.myapplication.R
import com.squareup.picasso.Picasso

class ReviewsAdapter(private val context: Context, private val reviewList: ArrayList<Review>): RecyclerView.Adapter<ReviewsAdapter.MyViewHolder>(){
    class MyViewHolder(val view: View): RecyclerView.ViewHolder(view){
        val image = itemView.findViewById<ImageView>(R.id.imageReview)
        val username = itemView.findViewById<TextView>(R.id.nameReview)
        val review = itemView.findViewById<TextView>(R.id.reviewTextMain)
        val ratings = itemView.findViewById<TextView>(R.id.ratingTextReview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.reveiw_tile, parent , false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = reviewList[position]
       // Picasso.get().load(item.)
    }

    override fun getItemCount(): Int { return reviewList.size    }
}