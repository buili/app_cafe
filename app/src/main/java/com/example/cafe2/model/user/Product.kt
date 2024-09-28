package com.example.cafe2.model.user

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Product(private var id:Int,
              private var name:String,
              private var image:String,
              private var id_category:Int,
              private var price:Int) : Serializable {
    fun getId(): Int {
        return id
    }

    fun setId(value: Int){
        id = value
    }

    fun getName(): String {
        return name
    }
    fun setName(value: String){
        name = value
    }

    fun getImage(): String {
        return image
    }
    fun setImage(value: String){
        image = value
    }

    fun getPrice(): Int {
        return price
    }
    fun setPrice(value: Int){
        price = value
    }
    fun getIdCategory(): Int {
        return id_category
    }
    fun setIdCategory(value: Int){
        id_category = value
    }
}