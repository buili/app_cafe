package com.example.cafe2.adapter.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cafe2.databinding.ItemAddressBinding
import com.example.cafe2.model.user.Address

class ItemAddressAdapter(val listAddress:MutableList<Address>)
    :RecyclerView.Adapter<ItemAddressAdapter.ViewHolder>(){

    private var selectedPosition = - 1
    class ViewHolder(val itemAddressBinding: ItemAddressBinding):RecyclerView.ViewHolder(itemAddressBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listAddress.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item:Address = listAddress[position]
        holder.itemAddressBinding.txtNameUser.text = item.name
        holder.itemAddressBinding.txtPhoneUser.text = item.phone
        holder.itemAddressBinding.txtAddressUser.text = item.address

        holder.itemAddressBinding.rdcheck.isChecked = (position == selectedPosition)
        holder.itemAddressBinding.rdcheck.setOnClickListener{
            val currentPosition = holder.adapterPosition
            if (currentPosition != RecyclerView.NO_POSITION) {
                selectedPosition = currentPosition
                notifyDataSetChanged()
            }
        }
    }
}