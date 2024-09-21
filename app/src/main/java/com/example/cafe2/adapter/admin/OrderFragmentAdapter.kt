package com.example.cafe2.adapter.admin

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.cafe2.util.Utils
import com.example.cafe2.view.fragment.admin.TabOrderFragment

class OrderFragmentAdapter(fragment: Fragment):FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
       // return Utils..size ?: 0
        return 2
    }

    override fun createFragment(position: Int): Fragment {
//        val idCategory: String = Utils.listCategory.get(position).id.toString()
//        Log.d("TabOrder" , "Fragment Created 1")
//        return TabOrderFragment.newInstance(idCategory)
//        if (idCategory.isNullOrEmpty()) {
//            Log.e("TabOrder", "Invalid category ID")
//            return TabOrderFragment.newInstance("") // Hoặc xử lý phù hợp nếu ID không hợp lệ
//        } else {
//            return TabOrderFragment.newInstance(idCategory)
//        }

       return when(position){
            0 -> TabOrderFragment.newInstance(0)
            1 ->  TabOrderFragment.newInstance(2)
            else -> TabOrderFragment.newInstance(0)
        }
//        val status = Utils.statusOrder(position) ?: "..."
//        return TabOrderFragment.newInstance(status)

        //return TabOrderFragment()
    }
}