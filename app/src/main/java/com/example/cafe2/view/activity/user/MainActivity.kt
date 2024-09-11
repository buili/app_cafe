package com.example.cafe2.view.activity.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.cafe2.R
import com.example.cafe2.databinding.ActivityMainBinding
import com.example.cafe2.model.user.Product
import com.example.cafe2.util.Utils
import com.example.cafe2.view.fragment.user.AccountFragment
import com.example.cafe2.view.fragment.user.CartFragment
import com.example.cafe2.view.fragment.user.HomeFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        val mainBinding: ActivityMainBinding =
            DataBindingUtil.setContentView<ActivityMainBinding>(
                this,
                R.layout.activity_main
            )


        val bundleFromIntent = intent.getBundleExtra("addProduct")
        if (bundleFromIntent != null) {

//        }
//
//        val product = intent.getSerializableExtra("productDetail") as Product?
//        if (product != null) {
//            Log.d("MainActivity", "Received product: $product")
            val product = bundleFromIntent.getSerializable("productDetail") as? Product
            val quantity = bundleFromIntent.getInt("quantity")
            product?.let {
                val cartFragment = CartFragment()
                val bundle = Bundle()
                //  bundle.putSerializable("productDetail", product)
                val bundleToFragment = Bundle()
                bundleToFragment.putSerializable("productDetail", product)
                bundleToFragment.putInt("quantity", quantity)
                cartFragment.arguments = bundleToFragment
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.fragment_container, cartFragment)
//                .commit()
                replaceFragment(cartFragment)
                mainBinding.bottomNavigation.selectedItemId = R.id.navigation_cart
            }
        } else {
            // Hiển thị HomeFragment khi Activity được tạo mà không có sản phẩm từ Intent
            if (savedInstanceState == null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, HomeFragment())
                    .commit()
            }
        }
        // Xử lý sự kiện chọn mục trong BottomNavigationView
        mainBinding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // val idCategory: String = Utils.category(1)
                    replaceFragment(HomeFragment())
                    true
                }

                R.id.navigation_cart -> {
                    replaceFragment(CartFragment())
                    true
                }

                R.id.navigation_account -> {
                    replaceFragment(AccountFragment())
                    true
                }

                else -> false
            }
        }
    }

    // Hàm thay thế Fragment trong container
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
