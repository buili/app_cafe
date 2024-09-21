package com.example.cafe2.model.admin

class Orders(val id:Int,
    val idUser:Int,
    val quantity:Int,
    val totalAmount:Int,
    val status:Int,
    val listItem:MutableList<Item>?
)