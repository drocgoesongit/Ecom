package com.example.myapplication.Fragments.order

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Adapters.OrderManagerAdapter
import com.example.myapplication.Model.Order
import com.example.myapplication.R
import com.example.myapplication.viewModel.OrderViewModel
import kotlinx.coroutines.*

private const val TAG = "Order"

class OrdersList : Fragment() {
    private lateinit var orderViewModel: OrderViewModel
    private lateinit var orderList: List<Order>
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_orders_list, container, false)
        orderViewModel = ViewModelProvider(this).get(OrderViewModel::class.java)
        orderList = orderViewModel.getOrderList()

        recyclerView = view.findViewById(R.id.recyclerViewOrders)

        checkForList()

        return view
    }
    private fun checkForList(){
        if(orderList.size == 0) {
            GlobalScope.launch {
                suspend {
                    Log.w(TAG, "Running in gloabal scope waiting for list.")
                    delay(3000)
                    withContext(Dispatchers.Main) {
                        if(orderList.size == 0){
                            orderList = orderViewModel.getOrderList()
                            checkForList()
                        }else{
                            setList()
                            Log.w(TAG, "list size: ${orderList.size}")
                        }
                    }
                }.invoke()
            }
        }
    }
    private fun setList(){
        val adapterForRecyclerView = OrderManagerAdapter(requireContext(), orderList)
        recyclerView.adapter = adapterForRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

    }
}

