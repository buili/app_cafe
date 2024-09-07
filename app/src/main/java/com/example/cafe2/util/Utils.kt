package com.example.cafe2.util

object Utils {
    const  val BASE_URL:String = "http://192.168.1.5/cafe/"

        fun category(id: Int): String {
            val result = when (id) {
                0 -> "Trà sữa"
                1 -> "Cà phê"
                2 -> "Sinh tố"
                3 -> "Khác"
                else -> "..."
            }
            return result
        }


}