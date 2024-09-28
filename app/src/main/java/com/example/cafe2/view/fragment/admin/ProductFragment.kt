package com.example.cafe2.view.fragment.admin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafe2.R
import com.example.cafe2.adapter.admin.CategoryAdapter
import com.example.cafe2.adapter.admin.ItemProductAdapter
import com.example.cafe2.databinding.FragmentCategoryBinding
import com.example.cafe2.databinding.FragmentProductBinding
import com.example.cafe2.model.admin.Category
import com.example.cafe2.model.user.Product
import com.example.cafe2.retrofit.ApiCafe
import com.example.cafe2.retrofit.RetrofitClient
import com.example.cafe2.util.Utils
import com.example.cafe2.view.activity.admin.AddProductActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class ProductFragment : Fragment() {

    private lateinit var fragmentProductBinding: FragmentProductBinding
    private val compositeDisposable = CompositeDisposable()
    private val apiCafe: ApiCafe by lazy {
        RetrofitClient.getInstance(Utils.BASE_URL).create(ApiCafe::class.java)
    }
    private lateinit var recyclerView: RecyclerView
    private lateinit var imgAddProduct: ImageView
    private lateinit var listProduct: MutableList<Product>
    private lateinit var itemProductAdapter: ItemProductAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentProductBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_product,
            container,
            false
        )
        initView()
        getProduct()
        initControl()
        return fragmentProductBinding.root
    }

    private fun initControl() {
        imgAddProduct.setOnClickListener{
            val intent = Intent(context, AddProductActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getProduct() {
        compositeDisposable.add(apiCafe.getProduct()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { productModel ->
                    if (productModel.isSuccess()) {
                        listProduct = productModel.getResult()
                        itemProductAdapter = ItemProductAdapter(listProduct)
                        recyclerView.adapter = itemProductAdapter
                    }
                }, { throwable ->
                    Toast.makeText(context, "Lỗi ${throwable.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            )
        )
    }

    private fun initView() {
        recyclerView = fragmentProductBinding.recyclerProduct
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        imgAddProduct = fragmentProductBinding.imgAddProduct
        listProduct = ArrayList()
    }
}