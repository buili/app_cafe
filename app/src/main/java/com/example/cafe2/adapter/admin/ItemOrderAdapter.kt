package com.example.cafe2.adapter.admin

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.example.cafe2.databinding.OrderProcessingBinding
import com.example.cafe2.model.admin.Item
import com.example.cafe2.model.admin.Orders
import com.example.cafe2.view.activity.admin.OrderTrackingActivity

class ItemOrderAdapter(private val listOrder:MutableList<Orders>)
    :RecyclerView.Adapter<ItemOrderAdapter.ViewHolder>(){
    var viewPool: RecycledViewPool = RecyclerView.RecycledViewPool()
    class ViewHolder(val orderProcessingBinding: OrderProcessingBinding) : RecyclerView.ViewHolder(orderProcessingBinding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = OrderProcessingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listOrder.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val orders: Orders = listOrder[position]
        holder.orderProcessingBinding.txtIdOrders.text = orders.id.toString()
        holder.orderProcessingBinding.txtUserName

        // Nếu orders.listItem là null, thì sẽ dùng danh sách rỗng
        val itemOrderList: MutableList<Item> = orders.listItem ?: ArrayList()

        val layoutManager = LinearLayoutManager(
            holder.orderProcessingBinding.recyclerviewOrders.context,
            LinearLayoutManager.VERTICAL,
            false
        )

        layoutManager.initialPrefetchItemCount = itemOrderList.size

        // Sử dụng danh sách rỗng nếu orders.listItem là null
        val itemProcessAdapter = ItemProcessAdapter(itemOrderList)

        // Set layoutManager và adapter
        holder.orderProcessingBinding.recyclerviewOrders.layoutManager = layoutManager
        holder.orderProcessingBinding.recyclerviewOrders.adapter = itemProcessAdapter

        // Kiểm tra nếu RecyclerView chưa null trước khi gán viewPool
        holder.orderProcessingBinding.recyclerviewOrders.setRecycledViewPool(viewPool)
        val dividerItemDecoration =
            DividerItemDecoration( holder.orderProcessingBinding.recyclerviewOrders.context, layoutManager.orientation)
        holder.orderProcessingBinding.recyclerviewOrders.addItemDecoration(dividerItemDecoration)

        holder.orderProcessingBinding.txtOrderTracking.setOnClickListener{
            val intent = Intent(context, OrderTrackingActivity::class.java)
            intent.putExtra("idOrder", orders.id)
            context.startActivity(intent)
        }
    }
}