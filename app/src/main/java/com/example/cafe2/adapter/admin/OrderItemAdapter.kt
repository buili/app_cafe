package com.example.cafe2.adapter.admin

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafe.model.User
import com.example.cafe2.databinding.OrderProcessingBinding
import com.example.cafe2.model.admin.Item
import com.example.cafe2.model.admin.Orders
import com.example.cafe2.retrofit.ApiCafe
import com.example.cafe2.retrofit.RetrofitClient
import com.example.cafe2.util.Utils
import com.example.cafe2.view.activity.admin.OrderCompeteActivity
import com.example.cafe2.view.activity.admin.OrderTrackingActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class OrderItemAdapter(private val listOrder: MutableList<Orders>) :
    RecyclerView.Adapter<OrderItemAdapter.ViewHolder>() {
    var viewPool: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()
    private val compositeDisposable = CompositeDisposable()
    private val apiCafe by lazy {
        RetrofitClient.getInstance(Utils.BASE_URL).create(ApiCafe::class.java)
    }
    private var role = 1

    class ViewHolder(val orderProcessingBinding: OrderProcessingBinding) :
        RecyclerView.ViewHolder(orderProcessingBinding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            OrderProcessingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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


        // Set layoutManager và adapter
        holder.orderProcessingBinding.recyclerviewOrders.layoutManager = layoutManager

        if (orders.status == 2) {
            val itemOrderCompletedAdapter = ItemOrderCompletedAdapter(itemOrderList)
            holder.orderProcessingBinding.recyclerviewOrders.adapter = itemOrderCompletedAdapter
            holder.orderProcessingBinding.txtOrderTracking.setOnClickListener {
                val intent = Intent(context, OrderCompeteActivity::class.java)
                intent.putExtra("idOrder", orders.id)
                context.startActivity(intent)
            }
        } else {
            // Sử dụng danh sách rỗng nếu orders.listItem là null
            val itemProcessAdapter = ItemProcessAdapter(itemOrderList)
            holder.orderProcessingBinding.recyclerviewOrders.adapter = itemProcessAdapter
            holder.orderProcessingBinding.txtOrderTracking.setOnClickListener {
                val intent = Intent(context, OrderTrackingActivity::class.java)
                intent.putExtra("idOrder", orders.id)
                context.startActivity(intent)
            }
        }
//        val detailOrderAdapter = DetailOrderAdapter(itemOrderList)
//            holder.orderProcessingBinding.recyclerviewOrders.adapter = detailOrderAdapter

        // Kiểm tra nếu RecyclerView chưa null trước khi gán viewPool
        holder.orderProcessingBinding.recyclerviewOrders.setRecycledViewPool(viewPool)
        val dividerItemDecoration =
            DividerItemDecoration(
                holder.orderProcessingBinding.recyclerviewOrders.context,
                layoutManager.orientation
            )
        holder.orderProcessingBinding.recyclerviewOrders.addItemDecoration(dividerItemDecoration)


    }



}