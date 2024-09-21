package com.example.cafe2.adapter.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cafe2.R
import com.example.cafe2.databinding.ItemOrderCompleteBinding
import com.example.cafe2.databinding.ItemOrderProcessingBinding
import com.example.cafe2.model.admin.Item

class OrderDetailAdapter(val listItem: MutableList<Item>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_PROCESSING = 1
    private val TYPE_FINISH = 2


    override fun getItemCount(): Int {
        return listItem.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_PROCESSING) {
            val binding = ItemOrderProcessingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ProcessingViewHolder(binding)
        } else {
            val binding = ItemOrderCompleteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            CompleteViewHolder(binding)
        }
    }

    class ProcessingViewHolder(val itemOrderProcessingBinding: ItemOrderProcessingBinding) :
        RecyclerView.ViewHolder(itemOrderProcessingBinding.root) {

    }

    class CompleteViewHolder(val itemOrderCompleteBinding: ItemOrderCompleteBinding) :
        RecyclerView.ViewHolder(itemOrderCompleteBinding.root) {
    }



}


