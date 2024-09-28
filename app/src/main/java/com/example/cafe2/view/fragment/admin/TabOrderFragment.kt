package com.example.cafe2.view.fragment.admin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafe2.R
import com.example.cafe2.adapter.admin.OrderItemAdapter
import com.example.cafe2.databinding.FragmentTabOrderBinding
import com.example.cafe2.model.admin.Orders
import com.example.cafe2.retrofit.ApiCafe
import com.example.cafe2.retrofit.RetrofitClient
import com.example.cafe2.util.Utils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class TabOrderFragment : Fragment() {
    private lateinit var fragmentTabOrderBinding: FragmentTabOrderBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var orderList: MutableList<Orders>
    private val compositeDisposable = CompositeDisposable()
    private val apiCafe: ApiCafe by lazy {
        RetrofitClient.getInstance(Utils.BASE_URL).create(ApiCafe::class.java)
    }

    private  var status: Int = 0
    companion object {
        private const val STATUS = "STATUS"
        fun newInstance(status: Int): TabOrderFragment {
            val fragment = TabOrderFragment()
            val args = Bundle().apply {
                putInt(STATUS, status)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            status = it.getInt(STATUS)  // Sử dụng getInt để lấy giá trị kiểu Int
            Log.d("TabOrder", "Status: $status")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentTabOrderBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_tab_order,
            container,
            false
        )
        Log.d("TabOrder", "Fragment Created")
        initView()  // Khởi tạo RecyclerView
        return fragmentTabOrderBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getOrderByStatus(Utils.userCurrent.getId(), status)  // Gọi API sau khi View được tạo xong
    }

    private fun getOrderByStatus(idUser:Int, status: Int) {
        Log.d("TabOrder", "Bắt đầu gọi API")
        compositeDisposable.add(apiCafe.getOrderByStatus(Utils.userCurrent.getId(), status)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { orderModel ->
                    if (orderModel.isSuccess()) {
                        Log.d("TabOrder", "orderModelModel: " + orderModel.getResult())
                        orderList = orderModel.getResult()
                      //  val orderAdapter = ItemOrderAdapter(orderList)
                        val orderAdapter = OrderItemAdapter(orderList)
                        recyclerView.adapter = orderAdapter
                        orderAdapter.notifyDataSetChanged()
                    } else {
                        Log.d("TabOrder", "API không trả về thành công")
                    }
                },
                {
                    Log.d("TabOrder", "Lỗi: " + it.message)
                    Toast.makeText(
                        context,
                        "Lỗi: " + it.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            ))
    }

    private fun initView() {
        recyclerView = fragmentTabOrderBinding.recyclerOrder
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)

        val dividerItemDecoration =
            DividerItemDecoration(recyclerView.context, layoutManager.orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)

        orderList = ArrayList()
        Log.d("TabOrder", "RecyclerView đã được khởi tạo")
        Log.d("TabOrder", "UserCurrent ${Utils.userCurrent.getId()}")
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()  // Xử lý việc dọn dẹp
    }
}
