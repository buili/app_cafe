package com.example.cafe2.model.user

import com.example.cafe2.model.admin.Category

class AddressModel (private var success:Boolean,
                    private var message:String,
                    private var result:MutableList<Address>) {

    fun isSuccess() = success
    fun setSuccess(success: Boolean){
        this.success = success
    }

    fun getMesage() = message
    fun setMessage(message: String){
        this.message = message
    }

    fun getResult() = result
    fun setResult(result: MutableList<Address>){
        this.result = result
    }
}