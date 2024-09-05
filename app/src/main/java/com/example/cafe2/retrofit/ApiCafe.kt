package com.example.cafe2.retrofit

import com.example.cafe2.model.Product
import com.example.cafe2.model.ProductModel
import retrofit2.http.GET
import io.reactivex.rxjava3.core.Observable

interface ApiCafe {
    @GET("getProduct.php")
     fun getProduct(): Observable<ProductModel>
}