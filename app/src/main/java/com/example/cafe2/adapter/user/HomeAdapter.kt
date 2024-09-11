package com.example.cafe2.adapter.user

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.cafe2.util.Utils
import com.example.cafe2.view.fragment.user.TabFragment

class HomeAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return Utils.listCategory?.size ?: 0
    }

    override fun createFragment(position: Int): Fragment {
        val idCategory: String = Utils.listCategory?.get(position)?.id?.toString() ?: ""
        return TabFragment.newInstance(idCategory)
    }
}
