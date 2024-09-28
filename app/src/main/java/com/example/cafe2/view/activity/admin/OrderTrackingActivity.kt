package com.example.cafe2.view.activity.admin

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafe.model.User
import com.example.cafe2.R
import com.example.cafe2.adapter.admin.ItemProcessAdapter
import com.example.cafe2.databinding.ActivityOrderTrackingBinding
import com.example.cafe2.model.admin.Item
import com.example.cafe2.model.admin.Orders
import com.example.cafe2.retrofit.ApiCafe
import com.example.cafe2.retrofit.RetrofitClient
import com.example.cafe2.util.Utils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class OrderTrackingActivity : AppCompatActivity() {
    private lateinit var orderTrackingBinding: ActivityOrderTrackingBinding
    private lateinit var recyclerView: RecyclerView
    private val compositeDisposable = CompositeDisposable()
    private val apiCafe by lazy {
        RetrofitClient.getInstance(Utils.BASE_URL).create(ApiCafe::class.java)
    }
    private lateinit var itemItemProcessAdapter: ItemProcessAdapter

    private lateinit var img0: ImageView
    private lateinit var img1: ImageView
    private lateinit var img2: ImageView
    private lateinit var view1: View
    private lateinit var view2: View
    private var status = 0
    private var role = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_order_tracking)
        orderTrackingBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_order_tracking)
        initView()
        val idOrder = intent.getIntExtra("idOrder", 0)
        if (idOrder != null) {
            getRole(Utils.userCurrent.getId())
            getData(idOrder)
            getStatus(idOrder)
            initControl(idOrder)
        }


    }

    private fun initControl(idOrder: Int) {
        if(role == 0) {
            img1.setOnClickListener {
               // updateOrder(idOrder, 1)
                confirmUpdateOrder(idOrder, 1)
            }
            img2.setOnClickListener {
                //updateOrder(idOrder, 2)
                confirmUpdateOrder(idOrder, 2)
            }
        }
    }
    private fun confirmUpdateOrder(idOrder: Int, status: Int){
        var builder: AlertDialog.Builder = AlertDialog.Builder(applicationContext)
        builder.setTitle("Thông báo")
            .setIcon(R.drawable.baseline_announcement_24)
            .setMessage("Xác nhận tiếp theo")
            .setPositiveButton("Đồng ý") { dialog, which ->
                updateOrder(idOrder, status)
            }
            .setNegativeButton("Hủy", { dialog, which ->
                dialog.dismiss()
            })
        builder.show()
    }

    private fun updateOrder(idOrder: Int, status: Int) {
        compositeDisposable.add(apiCafe.updateOrder(idOrder, status)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { messageModel ->
                    if (messageModel.success) {
                        getStatus(idOrder)
                    }
                },
                {
                    Toast.makeText(
                        applicationContext,
                        "Lỗi " + it.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            ))
    }

    private fun updateUi() {
        if (status == 0) {
            img0.setImageResource(R.drawable.baseline_check_circle_24)
        } else if (status == 1) {
            img0.setImageResource(R.drawable.baseline_check_circle_24)
            img1.setImageResource(R.drawable.baseline_check_circle_24)
            view1.setBackgroundColor(applicationContext.getColor(R.color.backgroundchosse))
        } else if (status == 2) {
            img0.setImageResource(R.drawable.baseline_check_circle_24)
            img1.setImageResource(R.drawable.baseline_check_circle_24)
            view1.setBackgroundColor(applicationContext.getColor(R.color.backgroundchosse))
            view2.setBackgroundColor(applicationContext.getColor(R.color.backgroundchosse))
            img2.setImageResource(R.drawable.baseline_check_circle_24)
        }
    }

    private fun getStatus(idOrder: Int) {
        compositeDisposable.add(apiCafe.getStatus(idOrder)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { orderModel ->
                    if (orderModel.isSuccess()) {

                        val orders: Orders = orderModel.getResult().get(0)
                        status = orders.status
                        Log.d("Order", "status: " + status)
                        updateUi()
                    }
                },
                {
                    Toast.makeText(
                        applicationContext,
                        "Lỗi " + it.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            ))
    }

    private fun getData(idOrder: Int) {
        compositeDisposable.add(apiCafe.getOrderTracking(idOrder)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { itemModel ->
                    if (itemModel.isSuccess()) {
                        Log.d("OrderTracking", "orderModelModel: " + itemModel.getResult())
                        val orderList: MutableList<Item> = itemModel.getResult()
                        itemItemProcessAdapter = ItemProcessAdapter(orderList)
                        recyclerView.adapter = itemItemProcessAdapter
                    }
                },
                {
                    Toast.makeText(
                        applicationContext,
                        "Lỗi " + it.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            ))
    }

    private fun getRole(idUser: Int) {
        compositeDisposable.add(apiCafe.getRole(idUser)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { it ->
                    if (it.isSuccess()) {
                        val user: User = it.getResult()
                        role = user.getStatus()
                        Log.d("OrderTracking", role.toString())
                    }
                },{
                    Toast.makeText(
                        applicationContext,
                        "Lỗi " + it.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        )
    }

    private fun initView() {
        recyclerView = orderTrackingBinding.recyclerOrderTracking
        val layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)

        val dividerItemDecoration =
            DividerItemDecoration(recyclerView.context, layoutManager.orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)

        img0 = orderTrackingBinding.imgStatus0
        img1 = orderTrackingBinding.imgStatus1
        img2 = orderTrackingBinding.imgStatus2
        view1 = orderTrackingBinding.view1
        view2 = orderTrackingBinding.view2
    }
}