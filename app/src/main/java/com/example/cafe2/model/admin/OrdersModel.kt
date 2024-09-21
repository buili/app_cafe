package com.example.cafe2.model.admin

class OrdersModel (private var success:Boolean,
                   private var message:String,
                   private var result:MutableList<Orders>) {

    fun isSuccess() = success
    fun setSuccess(success: Boolean){
        this.success = success
    }

    fun getMesage() = message
    fun setMessage(message: String){
        this.message = message
    }

    fun getResult() = result
    fun setResult(result: MutableList<Orders>){
        this.result = result
    }
}