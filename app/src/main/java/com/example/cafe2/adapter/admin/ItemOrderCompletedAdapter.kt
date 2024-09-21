package com.example.cafe2.adapter.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cafe2.R
import com.example.cafe2.databinding.ItemOrderCompleteBinding
import com.example.cafe2.databinding.ItemOrderProcessingBinding
import com.example.cafe2.model.admin.Item

class ItemOrderCompletedAdapter(val listItem:MutableList<Item>)
    : RecyclerView.Adapter<ItemOrderCompletedAdapter.ViewHolder>(){
    class ViewHolder(val itemOrderCompleteBinding: ItemOrderCompleteBinding): RecyclerView.ViewHolder(itemOrderCompleteBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemOrderCompleteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  listItem.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: Item = listItem[position]
        val context = holder.itemView.context
        Glide.with(context)
            .load(item.image)
            .placeholder(R.drawable.noanh)
            .error(R.drawable.error)
            .into(holder.itemOrderCompleteBinding.imgProductComplete)
        holder.itemOrderCompleteBinding.txtNameUser
        holder.itemOrderCompleteBinding.txtPriceProduct.text = item.price.toString()
        holder.itemOrderCompleteBinding.txtIdOrder
        holder.itemOrderCompleteBinding.txtQuantity.text = item.quantity.toString()
    }
}