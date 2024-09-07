package com.example.cafe2.view.activity.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.cafe2.R
import com.example.cafe2.databinding.ActivityMainBinding
import com.example.cafe2.util.Utils
import com.example.cafe2.view.fragment.user.AccountFragment
import com.example.cafe2.view.fragment.user.HomeFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        val mainBinding: ActivityMainBinding =
            DataBindingUtil.setContentView<ActivityMainBinding>( this,
                R.layout.activity_main)


        // Mặc định hiển thị HomeFragment khi Activity được tạo
        if (savedInstanceState == null) {
            val idCategory:String = Utils.category(0)
            supportFragmentManager.beginTransaction()
                //.replace(R.id.fragment_container, HomeFragment.newInstance(idCategory))
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
               // .commitAllowingStateLoss()
        }

        // Xử lý sự kiện chọn mục trong BottomNavigationView
        mainBinding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    val idCategory: String = Utils.category(0)
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
//        val transaction = supportFragmentManager.beginTransaction()
//
//        // Tìm kiếm fragment hiện có theo tag
//        val existingFragment = supportFragmentManager.findFragmentByTag(fragment::class.java.simpleName)
//
//        if (existingFragment != null) {
//            transaction.show(existingFragment)
//        } else {
//            transaction.add(R.id.fragment_container, fragment, fragment::class.java.simpleName)
//        }
//
//        transaction.commit()
    }
}
