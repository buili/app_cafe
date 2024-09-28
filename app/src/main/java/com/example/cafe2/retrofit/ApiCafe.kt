package com.example.cafe2.retrofit

import com.example.cafe2.model.MessageModelShare
import com.example.cafe2.model.UserModel
import com.example.cafe2.model.admin.CategoryModel
import com.example.cafe2.model.admin.ItemModel
import com.example.cafe2.model.admin.OrdersModel
import com.example.cafe2.model.user.AddressModel
import com.example.cafe2.model.user.AdvertisementModel
import com.example.cafe2.model.user.CartModel
import com.example.cafe2.model.user.MessageModel
import com.example.cafe2.model.user.ProductModel
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiCafe {
    @POST("getProductByCategory.php")
    @FormUrlEncoded
    fun getProductByCategory(
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

    @POST("addOrder.php")
    @FormUrlEncoded
    fun addOrder(
        @Field("idUser") idUser: Int,
        @Field("quantity") quantity: Int?,
        @Field("totalAmount") totalAmount: Long?,
        @Field("detail") detail: String?
    ): Observable<MessageModel>

    @POST("updateCart.php")
    @FormUrlEncoded
    fun updateCart(
        @Field("idUser") idUser: Int,
        @Field("idProduct") idProduct: Int,
        @Field("quantity") quantity: Int
    ): Observable<CartModel>

    @POST("deleteCart.php")
    @FormUrlEncoded
    fun deleteCart(
        @Field("idUser") idUser: Int,
        @Field("idProduct") idProduct: Int,
    ): Observable<CartModel>

    @POST("signUp.php")
    @FormUrlEncoded
    fun signUp(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("userName") userName: String,
        @Field("mobile") mobile: String,
    ): Observable<UserModel>

    @POST("getOrder.php")
    @FormUrlEncoded
    fun getOrder(
        @Field("idUser") idUser: Int
    ): Observable<OrdersModel>

    @POST("getOrderByStatus.php")
    @FormUrlEncoded
    fun getOrderByStatus(
        @Field("idUser") idUser: Int,
        @Field("status") status: Int
    ): Observable<OrdersModel>

    @POST("getOrderTracking.php")
    @FormUrlEncoded
    fun getOrderTracking(
        @Field("idOrder") idOrder: Int
    ): Observable<ItemModel>

    @POST("getStatus.php")
    @FormUrlEncoded
    fun getStatus(
        @Field("idOrder") idOrder: Int
    ): Observable<OrdersModel>

    @POST("updateOrder.php")
    @FormUrlEncoded
    fun updateOrder(
        @Field("idOrder") idOrder: Int,
        @Field("status") status: Int
    ): Observable<MessageModel>

    @POST("getAddress.php")
    @FormUrlEncoded
    fun getAddress(
        @Field("idUser") idUser: Int
    ): Observable<AddressModel>

    @POST("addAddress.php")
    @FormUrlEncoded
    fun addAddress(
        @Field("idUser") idUser: Int,
        @Field("name") name: String,
        @Field("phone") phone: String,
        @Field("address") address: String
    ): Observable<AddressModel>

    @POST("getUser.php")
    @FormUrlEncoded
    fun getUser(
        @Field("email") email: String
    ): Observable<UserModel>

    @POST("getRole.php")
    @FormUrlEncoded
    fun getRole(
        @Field("idUser") idUser: Int
    ): Observable<UserModel>

    @GET("getProduct.php")
    fun getProduct(
    ): Observable<ProductModel>

    @POST("addProduct.php")
    @FormUrlEncoded
    fun addProduct(
        @Field("name") name: String,
        @Field("price") price: Int,
        @Field("image") image: String,
        @Field("id_category") id_category: Int,
    ): Observable<ProductModel>


    @Multipart
    @POST("uploadImgProduct.php")
    fun uploadImgProduct(@Part file: MultipartBody.Part): Call<MessageModelShare>

    @POST("deleteProduct.php")
    @FormUrlEncoded
    fun deleteProduct(
        @Field("id") id: Int
    ): Observable<ProductModel>

    @POST("editProduct.php")
    @FormUrlEncoded
    fun editProduct(
        @Field("id") id: Int,
        @Field("name") name: String,
        @Field("price") price: Int,
        @Field("image") image: String,
        @Field("id_category") id_category: Int,
    ): Observable<ProductModel>
}