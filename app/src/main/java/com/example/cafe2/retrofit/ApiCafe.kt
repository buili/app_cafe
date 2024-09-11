package com.example.cafe2.retrofit

import com.example.cafe2.model.UserModel
import com.example.cafe2.model.admin.CategoryModel
import com.example.cafe2.model.user.AdvertisementModel
import com.example.cafe2.model.user.CartModel
import com.example.cafe2.model.user.ProductModel
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

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

    @GET("getCategory.php")
    fun getCategory(): Observable<CategoryModel>

    @POST("addCategory.php")
    @FormUrlEncoded
    fun addCategory(
        @Field("nameCategory") nameCategory: String?,
    ): Observable<CategoryModel>

    @POST("editCategory.php")
    @FormUrlEncoded
    fun editCategory(
        @Field("idCategory") idCategory: Int?,
        @Field("nameCategory") nameCategory: String?

    ): Observable<CategoryModel>

    @POST("deleteCategory.php")
    @FormUrlEncoded
    fun deleteCategory(
        @Field("idCategory") idCategory: Int?,
    ): Observable<CategoryModel>

    @POST("addCart.php")
    @FormUrlEncoded
    fun addCart(
        @Field("idUser") idUser: Int?,
        @Field("idProduct") idProduct: Int?,
        @Field("quantity") quantity: Int?,
    ): Observable<CategoryModel>

    @POST("getCart.php")
    @FormUrlEncoded
    fun getCart(
        @Field("idUser") idUser: Int?
    ): Observable<CartModel>
}