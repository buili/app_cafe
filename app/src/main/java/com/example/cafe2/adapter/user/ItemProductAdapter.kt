package com.example.cafe2.adapter.user

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cafe2.R
import com.example.cafe2.databinding.ItemProductBinding
import com.example.cafe2.model.user.Product
import com.example.cafe2.view.activity.user.DetailsProductActivity

class ItemProductAdapter(
    private val listProduct: MutableList<Product>,
   // private val listener: ProductInterface,
) :
    RecyclerView.Adapter<ItemProductAdapter.ViewHolder>() {


    inner class ViewHolder(val itemProductBinding: ItemProductBinding) : RecyclerView.ViewHolder(itemProductBinding.root) {
       init {
           itemProductBinding.root.setOnClickListener{
               //listener.onClick(adapterPosition)
              // listener.onClick()
           }
       }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listProduct.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: Product = listProduct[position]
        holder.itemProductBinding.txtNameProduct.text = item.getName()
       // holder.itemProductBinding.imgProduct.setImageResource(item.getImage())
        holder.itemProductBinding.txtPriceProduct.text = item.getPrice().toString()

        // Lấy context từ itemView
        val context = holder.itemView.context

        Glide.with(context)
            .load(item.getImage())
            .placeholder(R.drawable.noanh)
            .error(R.drawable.error)
            .into(holder.itemProductBinding.imgProduct)

        holder.itemView.setOnClickListener{
            val intent = Intent(context, DetailsProductActivity::class.java)
            intent.putExtra("product",item)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }

    }
}


