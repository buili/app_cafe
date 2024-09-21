package com.example.cafe2._interface

import android.view.View
import com.example.cafe2.model.user.Cart
import java.text.FieldPosition

interface ImageClickInterface {
    fun onImageClick(cart:Cart, position: Int, value:Int)
}