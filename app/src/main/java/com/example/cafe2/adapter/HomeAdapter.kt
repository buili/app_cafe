package com.example.cafe2.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.cafe2.util.Utils
import com.example.cafe2.view.fragment.user.TabFragment

class HomeAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        val idCategory: String = Utils.category(position)
        return TabFragment.newInstance(idCategory)
    }
}
