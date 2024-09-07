package com.example.cafe.model

import android.text.TextUtils
import android.util.Patterns
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

class User(private var id:Int,
           private var email: String,
           private var password: String,
           private var username: String,
           private  var status:Int) {

    fun getEmail():String{
        return email
    }

    fun getPassword():String{
        return password
    }

    fun setEmail(email: String){
        this.email = email
    }

    fun setPassword(password: String){
        this.password = password
    }
}