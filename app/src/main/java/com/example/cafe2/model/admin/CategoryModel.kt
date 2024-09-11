package com.example.cafe2.model.admin

import com.example.cafe2.model.user.Product

class CategoryModel (private var success:Boolean,
                     private var message:String,
                     private var result:MutableList<Category>) {

    fun isSuccess() = success
    fun setSuccess(success: Boolean){
        this.success = success
    }

    fun getMesage() = message
    fun setMessage(message: String){
        this.message = message
    }

    fun getResult() = result
    fun setResult(result: MutableList<Category>){
        this.result = result
    }
}