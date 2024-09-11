package com.example.cafe2.adapter.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cafe2.R
import com.example.cafe2.databinding.ItemProductAdminBinding

import com.example.cafe2.model.user.Product

class ItemProductAdapter(val listProduct:MutableList<Product>)
    :RecyclerView.Adapter<ItemProductAdapter.ViewHolder>(){
    class ViewHolder(val  itemProductAdminBinding: ItemProductAdminBinding):RecyclerView.ViewHolder(itemProductAdminBinding.root) {
        init {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemProductAdminBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
          return  listProduct.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listProduct[position]
        holder.itemProductAdminBinding.txtNameProductAdmin.text = item.getName()
        holder.itemProductAdminBinding.txtPriceProductAdmin.text = item.getPrice().toString()

        val context = holder.itemView.context

        Glide.with(context)
            .load(item.getImage())
            .placeholder(R.drawable.noanh)
            .error(R.drawable.error)
            .into(holder.itemProductAdminBinding.imgProductAdmin)

        holder.itemProductAdminBinding.imgEditProduct.setOnClickListener{
            Toast.makeText(context, "Sửa ${position}", Toast.LENGTH_SHORT).show()
        }

        holder.itemProductAdminBinding.imgDeleteProduct.setOnClickListener{
            Toast.makeText(context, "Xóa ${position}", Toast.LENGTH_SHORT).show()
        }
    }
}