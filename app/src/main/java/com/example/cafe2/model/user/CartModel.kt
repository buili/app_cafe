package com.example.cafe2.model.user

class CartModel(private var success:Boolean,
                private var message:String,
                private var result:MutableList<Cart>) {

    fun isSuccess() = success
    fun setSuccess(success: Boolean){
        this.success = success
    }

    fun getMesage() = message
    fun setMessage(message: String){
        this.message = message
    }

    fun getResult() = result
    fun setResult(result: MutableList<Cart>){
        this.result = result
    }
}