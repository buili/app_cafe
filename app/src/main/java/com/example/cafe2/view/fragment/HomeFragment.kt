package com.example.cafe2.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafe2._interface.RvInterface
import com.example.cafe2.adapter.ItemProductAdapter
import com.example.cafe2.model.Product


import com.example.cafe2.R
import com.example.cafe2.retrofit.ApiCafe
import com.example.cafe2.retrofit.RetrofitClient
import com.example.cafe2.util.Utils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class HomeFragment : Fragment(), RvInterface {

    private lateinit var rootView: View
    private lateinit var listProduct: MutableList<Product>
    private lateinit var recyclerView: RecyclerView
    private lateinit var itemProductAdapter: ItemProductAdapter
    private val compositeDisposable = CompositeDisposable()
    private val apiCafe: ApiCafe by lazy {
        RetrofitClient.getInstance(Utils.BASE_URL).create(ApiCafe::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_home, container, false)
        initView()
        getData()
        return rootView
    }

    private fun initView() {
        recyclerView = rootView.findViewById(R.id.recycler_home)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)

        listProduct = ArrayList()

        val dividerItemDecoration = DividerItemDecoration(recyclerView.context, layoutManager.orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)
    }

    private fun getData() {
        compositeDisposable.add(
            apiCafe.getProduct()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { productModel ->
                        if (productModel.isSuccess()) {
                            listProduct = productModel.getResult()
                            itemProductAdapter = ItemProductAdapter(listProduct, this)
                            recyclerView.adapter = itemProductAdapter
                        }
                    },
                    { throwable ->
                        Log.e("HomeFragment", throwable.message.toString())
                        Toast.makeText(context, "Lỗi ${throwable.message}", Toast.LENGTH_SHORT).show()
                    }
                )
        )
    }

    override fun onClick(position: Int) {
        Toast.makeText(context, "Item clicked at position $position", Toast.LENGTH_SHORT).show()
    }
}




