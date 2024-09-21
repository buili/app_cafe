package com.example.cafe2.view.fragment.admin

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.cafe2.R
import com.example.cafe2.databinding.FragmentSettingBinding
import com.example.cafe2.view.activity.user.LoginActivity

class SettingFragment:Fragment() {
    private lateinit var fragmentSettingBinding: FragmentSettingBinding
    private lateinit var btnLogout: AppCompatButton
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //return super.onCreateView(inflater, container, savedInstanceState)
        fragmentSettingBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false)
        initView()
        initControl()
        return fragmentSettingBinding.root
    }
    private fun initControl() {
        btnLogout.setOnClickListener{
            val mypref: SharedPreferences = requireContext().getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)
            val myedit: SharedPreferences.Editor = mypref.edit()
            myedit.clear()
            myedit.commit()
            val intent: Intent = Intent(
                context,
                LoginActivity::class.java
            )
            startActivity(intent)
        }
    }

    private fun initView() {
        btnLogout = fragmentSettingBinding.btnLogout
    }
}