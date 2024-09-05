package com.example.cafe.model

import android.text.TextUtils
import android.util.Patterns
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

class User(private var email: String, private var pass: String) {

    fun isValidEmail():Boolean{
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    fun isValidPassword():Boolean{
        return !TextUtils.isEmpty(pass) && pass.length >= 6
    }

    fun getEmail():String{
        return email
    }

    fun getPassword():String{
        return pass
    }

    fun setEmail(email: String){
        this.email = email
    }

    fun setPassword(pass: String){
        this.pass = pass
    }
}