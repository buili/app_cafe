package com.example.cafe2.view.activity.user

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.cafe2.R
import com.example.cafe2.databinding.ActivityDetailsProductBinding
import com.example.cafe2.model.user.Product
import com.example.cafe2.retrofit.ApiCafe
import com.example.cafe2.retrofit.RetrofitClient
import com.example.cafe2.util.Utils
import com.example.cafe2.view.fragment.user.CartFragment
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class DetailsProductActivity : AppCompatActivity() {
    private lateinit var detailsProductBinding: ActivityDetailsProductBinding
    private lateinit var imgProduct: ImageView
    private lateinit var txtNameProduct: TextView
    private lateinit var txtPriceProduct: TextView
    private lateinit var imgReduceProduct: ImageView
    private lateinit var imgAddProduct: ImageView
    private lateinit var txtQuantityProduct: TextView
    private lateinit var txtSumPrice: TextView
    private lateinit var btnAddCart: AppCompatButton
    private var product: Product? = null
    var quantity = 1
    private val compositeDisposable = CompositeDisposable()
    private val apiCafe by lazy {
        RetrofitClient.getInstance(Utils.BASE_URL).create(ApiCafe::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_details_product)
        detailsProductBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_details_product)

        initView()
        getData()
        initControl()

    }

    private fun sumPrice() {
        val sum = product?.getPrice()?.times(quantity)
        txtSumPrice.text = sum.toString()
    }

    private fun initControl() {
        btnAddCart.setOnClickListener {
//            val intent = Intent(this, MainActivity::class.java)
//            val bundle = Bundle()
//            bundle.putSerializable("productDetail", product)
//            bundle.putInt("quantity", quantity)
//            intent.putExtra("addProduct", bundle)
//            startActivity(intent)
            val idProduct = product?.getId()
            if (idProduct != null) {
                addCart(Utils.userCurrent.getId(), idProduct, quantity)
                Log.d("detailsProduct1", "id1: ${Utils.userCurrent.getId()}" )
            }
        }

        imgReduceProduct.setOnClickListener {
            if (quantity > 1) {
                quantity -= 1
                txtQuantityProduct.text = quantity.toString()
                sumPrice()
            }
        }


        imgAddProduct.setOnClickListener {
            quantity += 1
            txtQuantityProduct.text = quantity.toString()
            sumPrice()
        }

    }

    private fun addCart(idUser: Int, idProduct: Int, quantity: Int) {
        compositeDisposable.add(apiCafe.addCart(idUser, idProduct, quantity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { categoryModel ->
                    if (categoryModel.isSuccess()) {
                        Toast.makeText(
                            applicationContext,
                            "Thêm vào giỏ hàng thành công",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("Cart Info", "UserID: $idUser, ProductID: $idProduct, Quantity: $quantity")
                        val intent = Intent(this, MainActivity::class.java)
                        val bundle = Bundle()
                        bundle.putSerializable("productDetail", product)
                        bundle.putInt("quantity", quantity)
                        intent.putExtra("addProduct", bundle)
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Thêm không thành công",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                { throwable ->
                    Toast.makeText(
                        applicationContext,
                        "Thêm vào giỏ hàng lỗi",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            )

        )
    }

    @SuppressLint("CheckResult")
    private fun getData() {
        if (product != null) {
            Glide.with(applicationContext)
                .load(product?.getImage())
                .placeholder(R.drawable.noanh)
                .error(R.drawable.error)
                .into(imgProduct)
            txtNameProduct.text = product?.getName()
            txtPriceProduct.text = product?.getPrice().toString()
            txtSumPrice.text = product?.getPrice().toString()

        }
    }

    private fun initView() {
        imgProduct = detailsProductBinding.imgDetailProduct
        txtNameProduct = detailsProductBinding.txtNameDetailProduct
        txtPriceProduct = detailsProductBinding.txtPriceDetailProduct
        imgReduceProduct = detailsProductBinding.imgReduceNumber
        imgAddProduct = detailsProductBinding.imgAddNumber
        txtSumPrice = detailsProductBinding.txtSumPrice
        txtQuantityProduct = detailsProductBinding.txtQuantityProduct
        btnAddCart = detailsProductBinding.btnAddCart
        txtQuantityProduct.text = quantity.toString();
        product = intent.getSerializableExtra("product") as Product?
    }
}