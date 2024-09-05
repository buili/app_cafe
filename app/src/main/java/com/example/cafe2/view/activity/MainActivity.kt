package com.example.cafe2.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.cafe2.R
import com.example.cafe2.databinding.ActivityMainBinding
import com.example.cafe2.view.fragment.AccountFragment
import com.example.cafe2.view.fragment.HomeFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainBinding: ActivityMainBinding =
            DataBindingUtil.setContentView<ActivityMainBinding>(/* activity = */ this, /* layoutId = */
                R.layout.activity_main)


        // Mặc định hiển thị HomeFragment khi Activity được tạo
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
        }

        // Xử lý sự kiện chọn mục trong BottomNavigationView
        mainBinding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    replaceFragment(HomeFragment())
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
