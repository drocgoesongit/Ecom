package com.example.myapplication.Adapters

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Model.Address
import com.example.myapplication.R
import com.example.myapplication.activities.SelectAddress
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.SuccessContinuation
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@InternalCoroutinesApi
class ManageAddressAdapter(val context: Context, var addressList:ArrayList<Address> ): RecyclerView.Adapter<ManageAddressAdapter.ViewHolder>() {
    var selectedItem = 0

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
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
        holder.name.text = item.name
        holder.address.text = item.address
        holder.cityName.text = item.city
        holder.phoneNo.text = item.phoneNo
        holder.tick.setImageResource(R.drawable.ic_delete_ecom)

        holder.tick.setOnClickListener{

            MaterialAlertDialogBuilder(context,
                R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_FullWidthButtons)
                .setMessage("Delete selected address permanently?")
                .setTitle("Delete address?")
                .setNegativeButton("No") { _, _ ->
                }
                .setPositiveButton("Yes") { _, _ ->
                    FirebaseDatabase.getInstance().reference.child("Address")
                        .child(FirebaseAuth.getInstance().uid!!)
                        .child(item.id!!)
                        .removeValue()
                        .addOnSuccessListener (object: OnSuccessListener<Void?> {
                            override fun onSuccess(p0: Void?) {
                                notifyDataSetChanged()
                                Toast.makeText(context, "Address deleted successfully.", Toast.LENGTH_SHORT).show()
                            }
                        })
                }
                .show()

        }

    }

    override fun getItemCount(): Int {
        return addressList.size
    }

}