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
import com.example.cafe2.databinding.FragmentCategoryBinding
import com.example.cafe2.model.admin.Category
import com.example.cafe2.retrofit.ApiCafe
import com.example.cafe2.retrofit.RetrofitClient
import com.example.cafe2.util.Utils
import com.example.cafe2.view.activity.admin.AddCategoryActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class CategoryFragment:Fragment() {

    private lateinit var fragmentCategoryBinding: FragmentCategoryBinding
    private val compositeDisposable = CompositeDisposable()
    private val apiCafe: ApiCafe by lazy {
        RetrofitClient.getInstance(Utils.BASE_URL).create(ApiCafe::class.java)
    }
    private lateinit var recyclerView:RecyclerView
    private lateinit var listCategory:MutableList<Category>
    private lateinit var itemCategoryAdapter: CategoryAdapter
    private lateinit var imgAdd:ImageView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentCategoryBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_category,
            container,
            false
        )
        initView()
        Log.d("UserCurrent", Utils.userCurrent.getId().toString())
        getCategory()
        initControl()
        return fragmentCategoryBinding.root
    }

    private fun initControl() {
        imgAdd.setOnClickListener{
            val intent = Intent(context, AddCategoryActivity::class.java)
            startActivity(intent)
        }
    }


    private fun initView() {
        recyclerView = fragmentCategoryBinding.recyclerCategory
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        imgAdd = fragmentCategoryBinding.imgAddCategory
        listCategory = ArrayList()
    }
    private fun getCategory() {
        compositeDisposable.add(apiCafe.getCategory()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { categoryModel ->
                    if (categoryModel.isSuccess()) {
                        Utils.listCategory = categoryModel.getResult()
                        listCategory = categoryModel.getResult()
                        itemCategoryAdapter = CategoryAdapter(listCategory)
                        recyclerView.adapter = itemCategoryAdapter

                    }
                },
                { throwable ->
                    Toast.makeText(context, "Lỗi ${throwable.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            )
        )
    }

    override fun onResume() {
        super.onResume()
        getCategory()
    }
}