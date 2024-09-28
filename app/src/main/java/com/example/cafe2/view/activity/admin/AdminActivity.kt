package com.example.cafe2.view.activity.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.cafe2.R
import com.example.cafe2.databinding.ActivityAdminBinding
import com.example.cafe2.view.fragment.admin.CategoryFragment
import com.example.cafe2.view.fragment.admin.OrderFragment
import com.example.cafe2.view.fragment.admin.ProductFragment
import com.example.cafe2.view.fragment.admin.SettingFragment
import com.example.cafe2.view.fragment.user.HomeFragment


class AdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_admin)

        val adminBinding: ActivityAdminBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_admin)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_admin, CategoryFragment())
                .commit()
        }

        adminBinding.bottomNavigationAdmin.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_category -> {
                    replaceFragment(CategoryFragment())
                    true
                }
                R.id.navigation_beverage -> {
                    replaceFragment(ProductFragment())
                    true
                }
                R.id.navigation_order->{
                    replaceFragment(OrderFragment())
                    true
                }

                R.id.navigation_setting -> {
                    replaceFragment(SettingFragment())
                    true
                }

                else -> false
            }

        }
    }
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_admin, fragment)
            .commit()
    }
}

