package com.example.cafe2.util

import com.example.cafe.model.User
import com.example.cafe2.model.admin.Category

object Utils {
    const  val BASE_URL:String = "http://192.168.1.5/cafe/"
    var listCategory: MutableList<Category> = ArrayList()
    var userCurrent:User = User()
    //var listCategory: MutableList<Category>? = null

//        fun category(id: Int): String {
//            val result = when (id) {
//                0 -> "Cà phê"
//                1 -> "Trà sữa"
//                2 -> "Sinh tố"
//                3 -> "Khác"
//                else -> "..."
//            }
//            return result
//        }


}