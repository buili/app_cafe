package com.example.cafe2.retrofit

import com.example.cafe2.model.AdvertisementModel
import com.example.cafe2.model.ProductModel
import com.example.cafe2.model.UserModel
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import java.sql.ClientInfoStatus

interface ApiCafe {
    @POST("getProduct.php")
    @FormUrlEncoded
    fun getProduct(
        @Field("id_category") id_category: Int?,
    ): Observable<ProductModel>

    @GET("getAdvertisement.php")
    fun getAdvertisement(): Observable<AdvertisementModel>

    @POST("login.php")
    @FormUrlEncoded
    fun login(
        @Field("email") email: String?,
        @Field("pass") pass: String?,
        @Field("status") status: Int?
    ): Observable<UserModel>


}