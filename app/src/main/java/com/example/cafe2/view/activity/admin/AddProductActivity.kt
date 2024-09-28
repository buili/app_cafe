package com.example.cafe2.view.activity.admin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.DataBindingUtil
import com.example.cafe2.R
import com.example.cafe2.databinding.ActivityAddProductBinding
import com.example.cafe2.model.MessageModelShare
import com.example.cafe2.model.user.Product
import com.example.cafe2.retrofit.ApiCafe
import com.example.cafe2.retrofit.RetrofitClient
import com.example.cafe2.util.Utils
import com.github.dhaval2404.imagepicker.ImagePicker
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.util.Locale.Category

class AddProductActivity : AppCompatActivity() {
    private lateinit var addProductBinding:ActivityAddProductBinding
    private val compositeDisposable = CompositeDisposable()
    private val apiCafe by lazy {
        RetrofitClient.getInstance(Utils.BASE_URL).create(ApiCafe::class.java)
    }

    private lateinit var edtNameProduct:EditText
    private lateinit var spinnerCategory:Spinner
    private lateinit var edtDescribe:EditText
    private lateinit var edtPrice:EditText
    private lateinit var edtPromotion:EditText
    private lateinit var edtNameImg:EditText
    private lateinit var imgProduct:ImageView
    private lateinit var btnAddProduct:AppCompatButton

    private lateinit var mediaPath:String

    private var flag = false
    private var  product : Product? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_add_product)
        addProductBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_product);
        initView();
        initControl()

        product = intent.getSerializableExtra("editProduct") as? Product
        if(product == null){
            flag = false
        }else{
            flag = true
            btnAddProduct.text = "Sửa sản phẩm"
            edtNameProduct.text = Editable.Factory.getInstance().newEditable(product!!.getName())
            edtPrice.text = Editable.Factory.getInstance().newEditable(product!!.getPrice().toString())
            edtNameImg.text = Editable.Factory.getInstance().newEditable(product!!.getImage())

           // edtNameProduct.text = Editable.Factory.getInstance().newEditable(product!!.getName())

        }

    }

    private fun initControl() {
        btnAddProduct.setOnClickListener{
            if(flag == true){
                editProduct()
            }else{
                addProduct()
            }

        }

        imgProduct.setOnClickListener{
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start()


        }
    }

    private fun editProduct() {
        val strNamePrduct = edtNameProduct.text.toString().trim()
        val strDescribeProduct = edtDescribe.text.toString().trim()
        val strPriceProduct = edtPrice.text.toString().trim()
        val strPromotion = edtPromotion.text.toString().trim()
        val strImgProduct = edtNameImg.text.toString().trim()

        if(TextUtils.isEmpty(strNamePrduct) || TextUtils.isEmpty(strDescribeProduct)
            || TextUtils.isEmpty(strPriceProduct) || TextUtils.isEmpty(strPromotion)
            || TextUtils.isEmpty(strImgProduct)){
            Toast.makeText(applicationContext, "Vui lòng nhập đầy đủ các trường", Toast.LENGTH_SHORT).show()
        }else{
            compositeDisposable.add(apiCafe.editProduct(product!!.getId(), strNamePrduct, Integer.parseInt(strPriceProduct),
                strImgProduct, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        productModel->
                        if(productModel.isSuccess()){
                            Toast.makeText(applicationContext, "Sửa sản phẩm thành công", Toast.LENGTH_SHORT).show()
                        }
                    },{
                        Log.e("loiEdit" , it.message.toString());
                        Toast.makeText(applicationContext, "Sửa sản phẩm lỗi", Toast.LENGTH_SHORT).show()
                    }
                )
            )
        }
    }

    private fun initView() {
        edtNameProduct = addProductBinding.edtProduct
        spinnerCategory = addProductBinding.spinnerCategory
        edtDescribe = addProductBinding.edtDescribe
        edtPrice = addProductBinding.edtPrice
        edtPromotion = addProductBinding.edtPromotion
        edtNameImg = addProductBinding.edtNameImg
        imgProduct = addProductBinding.imgAddProduct
        btnAddProduct = addProductBinding.btnAddProduct
    }

    private fun addProduct(){
        val strNamePrduct = edtNameProduct.text.toString().trim()
        val strDescribeProduct = edtDescribe.text.toString().trim()
        val strPriceProduct = edtPrice.text.toString().trim()
        val strPromotion = edtPromotion.text.toString().trim()
        val strImgProduct = edtNameImg.text.toString().trim()

        if(TextUtils.isEmpty(strNamePrduct) || TextUtils.isEmpty(strDescribeProduct)
            || TextUtils.isEmpty(strPriceProduct) || TextUtils.isEmpty(strPromotion)
            || TextUtils.isEmpty(strImgProduct)){
            Toast.makeText(applicationContext, "Vui lòng nhập đầy đủ các trường", Toast.LENGTH_SHORT).show()
        }else{
            compositeDisposable.add(apiCafe.addProduct(strNamePrduct, Integer.parseInt(strPriceProduct),
                strImgProduct, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        productModel->
                        if(productModel.isSuccess()){
                            Toast.makeText(applicationContext, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show()
                        }
                    },{
                        Toast.makeText(applicationContext, "Thêm sản phẩm lỗi", Toast.LENGTH_SHORT).show()
                    }
                )
            )
        }
    }

    override  fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mediaPath = data?.dataString.toString()
        uploadMultipleFiles()
        Log.d("log", "onActivityResult$mediaPath")
    }

    private fun getPath(uri:Uri):String{
        var result:String = ""
        var cursor = contentResolver.query(uri, null, null, null, null)
        if(cursor == null){
            result = uri.path.toString()
        }else{
            cursor.moveToFirst()
            var index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(index)
            cursor.close()
        }
        return result
    }
    private fun uploadMultipleFiles() {
        var uri = Uri.parse(mediaPath)
        var file = File(getPath(uri))
        val  requestBody = RequestBody.create(MediaType.parse("*/*"), file)
        val fileToUpload = MultipartBody.Part.createFormData("file", file.name, requestBody)
        val call: Call<MessageModelShare> = apiCafe.uploadImgProduct(fileToUpload)
        call.enqueue(object : retrofit2.Callback<MessageModelShare?> {
            override fun onResponse(call: Call<MessageModelShare?>, response: Response<MessageModelShare?>) {
                val serverResponse: MessageModelShare? = response.body()
                if (serverResponse != null) {
                    if (serverResponse.success) {
                        edtNameImg.setText(serverResponse.name)
                    } else {
                        Toast.makeText(
                            applicationContext,
                            serverResponse.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Log.v("Response", serverResponse.toString())
                }
            }

            override fun onFailure(call: Call<MessageModelShare?>, t: Throwable) {
                Log.d("log", t.message!!)
            }
        })
    }
}