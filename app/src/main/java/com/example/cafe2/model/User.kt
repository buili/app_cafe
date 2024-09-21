package com.example.cafe.model

import android.text.TextUtils
import android.util.Patterns
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

class User(private var id:Int = 0,
           private var email: String="",
           private var password: String="",
           private var username: String="",
           private  var status:Int=1) {

    fun getId():Int = id
    fun setId(id:Int){
        this.id = id
    }

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

    fun getStatus():Int = status
    fun setStatus(status:Int){
        this.status = status
    }
}