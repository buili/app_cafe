package com.example.cafe2.adapter.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cafe2.R
import com.example.cafe2.databinding.ItemCartBinding
import com.example.cafe2.model.user.Cart
import kotlin.time.times

class ItemCartAdapter(private val listCart:MutableList<Cart>)
    :RecyclerView.Adapter<ItemCartAdapter.ViewHolder>() {
    inner class ViewHolder(val itemCartBinding: ItemCartBinding): RecyclerView.ViewHolder(itemCartBinding.root) {
        init {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
       return listCart.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val item = listCart[position]

        holder.itemCartBinding.txtPriceProductCart.text = item.product.getPrice().toString()
        holder.itemCartBinding.txtNameProductCart.text = item.product.getName()
        holder.itemCartBinding.txtQuantityCart.text = item.quantity.toString()
        holder.itemCartBinding.txtQuantityProductCart.text = item.quantity.toString()
        holder.itemCartBinding.imgReduceNumberCart
        holder.itemCartBinding.imgAddNumberCart

        val price= item.quantity * item.product.getPrice()
        holder.itemCartBinding.txtSumPriceProductCart.text = price.toString()

        val context = holder.itemView.context
        Glide.with(context)
            .load(item.product.getImage())
            .placeholder(R.drawable.noanh)
            .error(R.drawable.error)
            .into(holder.itemCartBinding.imgProductCart)
    }
}