package com.example.cafe2.view.fragment.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel


import com.example.cafe2.R
import com.example.cafe2.adapter.HomeAdapter
import com.example.cafe2.databinding.FragmentHomeBinding
import com.example.cafe2.model.Advertisement
import com.example.cafe2.retrofit.ApiCafe
import com.example.cafe2.retrofit.RetrofitClient
import com.example.cafe2.util.Utils
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class HomeFragment : Fragment() {

    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    //private lateinit var rootView: View
    private val compositeDisposable = CompositeDisposable()
    private val apiCafe: ApiCafe by lazy {
        RetrofitClient.getInstance(Utils.BASE_URL).create(ApiCafe::class.java)
    }
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var imageSlider: ImageSlider

    private lateinit var homeAdapter: HomeAdapter

    private lateinit var idCategory: String // Lưu ID của Category
    private lateinit var listAdvertisement: MutableList<Advertisement>

//    companion object {
//        private const val ID_CATEGORY = "ID_CATEGORY"
//        fun newInstance(status: String): HomeFragment {
//            val fragment = HomeFragment()
//            val args = Bundle().apply {
//                putString(ID_CATEGORY, status)
//            }
//            fragment.arguments = args
//            return fragment
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            //idCategory = requireArguments().getString(ID_CATEGORY).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //rootView = inflater.inflate(R.layout.fragment_home, container, false)
        fragmentHomeBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home,
            container,
            false
        )
        initView()
        actionViewFlipper();
        initControl()

        // return rootView
        return fragmentHomeBinding.root
    }
    private fun initView() {
        // recyclerView = rootView.findViewById(R.id.recycler_home)
        tabLayout = fragmentHomeBinding.tablayoutHome
        viewPager2 = fragmentHomeBinding.viewPagerHome
        imageSlider = fragmentHomeBinding.imageSlider
        listAdvertisement = ArrayList()
        try {
            if (!::homeAdapter.isInitialized) {
                homeAdapter = HomeAdapter(this)
                viewPager2.adapter = homeAdapter
                Log.d("HomeFragment", "Adapter created successfully2")
            }
            Log.d("HomeFragment", "Adapter created successfully")
            //viewPager2.adapter = adapter
        } catch (e: Exception) {
            Log.e("HomeFragment", "Error creating adapter: ${e.message}")
            Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }

    }
    private fun initControl() {
        // Chỉ gọi TabLayoutMediator khi adapter đã được gán cho ViewPager2
        if (viewPager2.adapter != null) {
            TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
                val title = Utils.category(position)
                tab.text = title
            }.attach()
        } else {
            Log.e("HomeFragment", "ViewPager2 adapter is null")
        }
    }

    private fun actionViewFlipper() {
        compositeDisposable.add(apiCafe.getAdvertisement()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { advertisementModel ->
                    if (advertisementModel.isSuccess()) {
                        listAdvertisement = advertisementModel.getResult()
                        var imgeList: MutableList<SlideModel> = ArrayList()
                        for (i in listAdvertisement) {
                            imgeList.add(SlideModel(i.getImage(), null, null))
                        }
                        imageSlider.setImageList(imgeList, ScaleTypes.CENTER_CROP)
                        imageSlider.startSliding(2000)

                    }
                },
                { throwable ->
                    Toast.makeText(context, "Lỗi ${throwable.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            )
        )
    }


}




