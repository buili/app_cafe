package com.example.cafe2.model.user

class AdvertisementModel( private var success:Boolean,
private var message:String,
private var result:MutableList<Advertisement>) {

    fun isSuccess() = success
    fun setSuccess(success: Boolean){
        this.success = success
    }

    fun getMesage() = message
    fun setMessage(message: String){
        this.message = message
    }

    fun getResult() = result
    fun setResult(result: MutableList<Advertisement>){
        this.result = result
    }
}