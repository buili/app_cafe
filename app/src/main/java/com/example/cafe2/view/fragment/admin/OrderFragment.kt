package com.example.cafe2.view.fragment.admin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.cafe2.R
import com.example.cafe2.adapter.admin.OrderFragmentAdapter
import com.example.cafe2.adapter.user.HomeAdapter
import com.example.cafe2.databinding.FragmentOrderBinding
import com.example.cafe2.retrofit.ApiCafe
import com.example.cafe2.retrofit.RetrofitClient
import com.example.cafe2.util.Utils
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class OrderFragment : Fragment() {
    private lateinit var fragmentOrderBinding: FragmentOrderBinding
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var orderFragmentAdapter: OrderFragmentAdapter
    private val compositeDisposable = CompositeDisposable()
    private val apiCafe: ApiCafe by lazy {
        RetrofitClient.getInstance(Utils.BASE_URL).create(ApiCafe::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentOrderBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_order, container, false)

        initView()
       // getCategory()
        initControl()
        return fragmentOrderBinding.root
    }


    private fun initControl() {
        if (viewPager2.adapter == null) {
            orderFragmentAdapter =OrderFragmentAdapter(this)
            viewPager2.adapter = orderFragmentAdapter
            TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
                val title = Utils.statusOrder(position)
                tab.text = title ?: "..."
            }.attach()
            Log.e("OrderFragment", "listCategory is not null")
        } else {
            Log.e("OrderFragment", "listCategory is null or empty")
        }
    }

    private fun initView() {
        tabLayout = fragmentOrderBinding.tablayoutOrder
        viewPager2 = fragmentOrderBinding.viewPagerOrder
    }
}