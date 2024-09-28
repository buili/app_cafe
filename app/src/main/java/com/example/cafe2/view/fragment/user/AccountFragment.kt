package com.example.cafe2.view.fragment.user

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment

import com.example.cafe2.R
import com.example.cafe2.databinding.FragmentAccountBinding
import com.example.cafe2.util.GoogleSign
import com.example.cafe2.view.activity.user.LoginActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class AccountFragment:Fragment() {

    private lateinit var fragmentAccountFragment:FragmentAccountBinding
    private lateinit var btnLogout:AppCompatButton
    private lateinit var txtLogout:TextView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentAccountFragment = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false)
        initView()


        initControl()
        return fragmentAccountFragment.root
    }

    private fun initControl() {
        btnLogout.setOnClickListener{
            val mypref: SharedPreferences = requireContext().getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)
            val myedit:SharedPreferences.Editor = mypref.edit()
            myedit.clear()
            myedit.commit()
            Firebase.auth.signOut()
            val googleSignInClient = GoogleSign.getGoogleSignInClient(requireContext())
            googleSignInClient.signOut()
            googleSignInClient.revokeAccess()
            val intent: Intent = Intent(
                context,
                LoginActivity::class.java
            )
            startActivity(intent)
        }

        txtLogout.setOnClickListener{
            val mypref: SharedPreferences = requireContext().getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)
            val myedit:SharedPreferences.Editor = mypref.edit()
            myedit.clear()
            myedit.commit()
            Firebase.auth.signOut()
            val googleSignInClient = GoogleSign.getGoogleSignInClient(requireContext())
            googleSignInClient.signOut()
            googleSignInClient.revokeAccess()
            val intent: Intent = Intent(
                context,
                LoginActivity::class.java
            )
            startActivity(intent)
        }
    }

    private fun initView() {
        btnLogout = fragmentAccountFragment.btnLogout
        txtLogout = fragmentAccountFragment.txtLogout
    }
}