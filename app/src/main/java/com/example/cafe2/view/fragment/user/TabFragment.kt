package com.example.cafe2.view.fragment.user

import android.content.Intent
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
import com.example.cafe2._interface.ProductInterface
import com.example.cafe2.adapter.user.ItemProductAdapter
import com.example.cafe2.databinding.FragmentTabBinding
import com.example.cafe2.model.user.Product
import com.example.cafe2.retrofit.ApiCafe
import com.example.cafe2.retrofit.RetrofitClient
import com.example.cafe2.util.Utils
import com.example.cafe2.view.activity.user.DetailsProductActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

//class TabFragment : Fragment(), ItemProductAdapter {
class TabFragment : Fragment() {
    private lateinit var fragmentTabBinding: FragmentTabBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var itemProductAdapter: ItemProductAdapter
    private lateinit var idCategory: String
    private lateinit var listProduct: MutableList<Product>
    private val compositeDisposable = CompositeDisposable()
    private val apiCafe: ApiCafe by lazy {
        RetrofitClient.getInstance(Utils.BASE_URL).create(ApiCafe::class.java)
    }

    companion object {
        private const val ID_CATEGORY = "ID_CATEGORY"
        fun newInstance(idCategory: String): TabFragment {
            val fragment = TabFragment()
            val args = Bundle().apply {
                putString(ID_CATEGORY, idCategory)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idCategory = it.getString(ID_CATEGORY).toString()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        fragmentTabBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_tab,
            container,
            false
        )
        initView()

       // getData(idCategory.toInt())
        return fragmentTabBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData(idCategory.toInt())
    }

    private fun initView() {
        recyclerView = fragmentTabBinding.recyclerHome
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        val dividerItemDecoration =
            DividerItemDecoration(recyclerView.context, layoutManager.orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)

        listProduct = ArrayList()

    }

    private fun getData(idCategory: Int) {
        compositeDisposable.add(
            apiCafe.getProductByCategory(idCategory)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { productModel ->
                        if (productModel.isSuccess()) {
                            listProduct = productModel.getResult()
                          //  itemProductAdapter = ItemProductAdapter(listProduct, this)
                            itemProductAdapter = ItemProductAdapter(listProduct)
                            recyclerView.adapter = itemProductAdapter
                        }
                    },
                    { throwable ->
                        Log.e("HomeFragment", throwable.message.toString())
                        Toast.makeText(context, "Lỗi ${throwable.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
                )
        )
    }

//    override fun onClick(position: Int) {
//        //Toast.makeText(context, "Item clicked at position $position", Toast.LENGTH_SHORT).show()
//        val intent = Intent(context, DetailsProductActivity::class.java)
//        intent.putExtra("editCategory", item)
//        context.startActivity(intent)
//    }

//    override fun onResume() {
//        super.onResume()
//        if (listProduct.isEmpty()) {
//            getData(idCategory.toInt())
//        }
//    }



}