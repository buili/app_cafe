package com.example.cafe2.view.fragment.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafe2.R
import com.example.cafe2.adapter.user.ItemCartAdapter
import com.example.cafe2.adapter.user.ItemProductAdapter
import com.example.cafe2.databinding.FragmentCartBinding
import com.example.cafe2.model.user.Cart
import com.example.cafe2.model.user.Product
import com.example.cafe2.retrofit.ApiCafe
import com.example.cafe2.retrofit.RetrofitClient
import com.example.cafe2.util.Utils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class CartFragment : Fragment() {

    private lateinit var fragmentCartBinding:FragmentCartBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnAddOrder:AppCompatButton
    private lateinit var listCart:MutableList<Cart>
    private var product:Product? = null
    private val compositeDisposable = CompositeDisposable()
    private lateinit var itemCartAdapter: ItemCartAdapter
    private val apiCafe: ApiCafe by lazy {
        RetrofitClient.getInstance(Utils.BASE_URL).create(ApiCafe::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentCartBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_cart,
            container,
            false
        )
        initView()
        initControl()
        //getCart(Utils.userCurrent.getId())
        getCart(1)
        return fragmentCartBinding.root
    }

    private fun initControl() {
        btnAddOrder.setOnClickListener{

        }
    }
    private fun getCart(idUser: Int) {
        compositeDisposable.add(
            apiCafe.getCart(idUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { cartModel ->
                        if (cartModel.isSuccess()) {
                            listCart = cartModel.getResult()
                            //  itemProductAdapter = ItemProductAdapter(listProduct, this)
                            itemCartAdapter = ItemCartAdapter(listCart)
                            recyclerView.adapter = itemCartAdapter
                        }else{
                            Log.e("CartFragment", "Lỗi")
                        }
                    },
                    { throwable ->
                        Log.e("CartFragment", throwable.message.toString())
                        Toast.makeText(context, "Lỗi ${throwable.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
                )
        )
    }

    private fun initView() {
//         product = arguments?.getSerializable("productDetail") as Product?
//         product?.let { Log.d("productDetail", it.getName()) }
        val bundle = arguments
        if (bundle != null) {
            // Lấy product và quantity từ bundle
            val product = bundle.getSerializable("productDetail") as? Product
            val quantity = bundle.getInt("quantity")

            // Kiểm tra product không null trước khi sử dụng
            product?.let {
                Log.d("CartFragment", "Received product: ${it.getName()}, Quantity: $quantity")
                // Xử lý tiếp với product và quantity
            }
        }
         btnAddOrder = fragmentCartBinding.btnAddOrder
        recyclerView = fragmentCartBinding.recyclerCart
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        val dividerItemDecoration =
            DividerItemDecoration(recyclerView.context, layoutManager.orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)

        listCart = ArrayList()
    }
}