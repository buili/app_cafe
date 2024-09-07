package com.example.cafe2.model

import com.example.cafe.model.User

class UserModel(private var success:Boolean,
    private var message:String,
    private var result:User) {
    fun isSuccess() = success
    fun setSuccess(success: Boolean){
        this.success = success
    }

    fun getMesage() = message
    fun setMessage(message: String){
        this.message = message
    }

    fun getResult() = result
    fun setResult(result: User){
        this.result = result
    }
}