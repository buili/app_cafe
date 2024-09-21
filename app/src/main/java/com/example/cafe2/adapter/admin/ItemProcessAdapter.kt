package com.example.cafe2.adapter.admin
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cafe2.R
import com.example.cafe2.databinding.ItemOrderProcessingBinding
import com.example.cafe2.model.admin.Item

class ItemProcessAdapter(val listItem:MutableList<Item>)
    :RecyclerView.Adapter<ItemProcessAdapter.ViewHolder>(){
    class ViewHolder(val itemOrderProcessingBinding: ItemOrderProcessingBinding):RecyclerView.ViewHolder(itemOrderProcessingBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemOrderProcessingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  listItem.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item:Item = listItem[position]
        val context = holder.itemView.context
        Glide.with(context)
            .load(item.image)
            .placeholder(R.drawable.noanh)
            .error(R.drawable.error)
            .into(holder.itemOrderProcessingBinding.imgProductProcessing)
        holder.itemOrderProcessingBinding.imgProductProcessing
        holder.itemOrderProcessingBinding.txtPriceProduct.text = item.price.toString()
        holder.itemOrderProcessingBinding.txtNameProduct.text = item.name
        holder.itemOrderProcessingBinding.txtQuantity.text = item.quantity.toString()
        holder.itemOrderProcessingBinding.txtSumPrice.text = (item.price * item.quantity).toString()
    }
}