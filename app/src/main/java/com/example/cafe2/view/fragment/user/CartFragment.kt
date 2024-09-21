package com.example.cafe2.view.fragment.user

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafe2.R
import com.example.cafe2._interface.ImageClickInterface
import com.example.cafe2.adapter.user.ItemCartAdapter
import com.example.cafe2.databinding.FragmentCartBinding
import com.example.cafe2.model.user.Cart
import com.example.cafe2.model.user.Product
import com.example.cafe2.retrofit.ApiCafe
import com.example.cafe2.retrofit.RetrofitClient
import com.example.cafe2.util.Utils
import com.example.cafe2.view.activity.user.AddressActivity
import com.google.gson.Gson
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class CartFragment : Fragment(), ImageClickInterface {

    private lateinit var fragmentCartBinding: FragmentCartBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnAddOrder: AppCompatButton
    private lateinit var txtTotalAmount:TextView
    private lateinit var listCart: MutableList<Cart>
    private lateinit var relativeAddress:RelativeLayout
    private var product: Product? = null
    private val compositeDisposable = CompositeDisposable()
    private lateinit var itemCartAdapter: ItemCartAdapter
    private val apiCafe: ApiCafe by lazy {
        RetrofitClient.getInstance(Utils.BASE_URL).create(ApiCafe::class.java)
    }
    var total = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentCartBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_cart,
            container,
            false
        )
        initView()
        initControl()
        //getCart(Utils.userCurrent.getId())
        getCart(Utils.userCurrent.getId())
        totalAmount()
        return fragmentCartBinding.root
    }

    private fun initControl() {
        btnAddOrder.setOnClickListener {
            Log.d("test", Gson().toJson(Utils.listPurchase))
            addOrder(Utils.userCurrent.getId(), 2, 3, Gson().toJson(Utils.listPurchase))
        }
        moveAddressActivity()
    }

    private fun moveAddressActivity() {
        relativeAddress.setOnClickListener{
            val intent = Intent(context, AddressActivity::class.java)
            startActivity(intent)
        }
    }

    private fun addOrder(idUser: Int, quantity: Int, totalAmount: Long, detail: String) {
        compositeDisposable.add(apiCafe.addOrder(idUser, quantity, totalAmount, detail)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { messageModel ->
                    if (messageModel.success) {
                        Toast.makeText(
                            context,
                            "Dặt hàng thành công",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                {
                    Log.e("CartFragment", it.message.toString())
                    Toast.makeText(context, "dat hang loi", Toast.LENGTH_SHORT)
                        .show()
                }
            )
        )
    }

    private fun getCart(idUser: Int) {
        compositeDisposable.add(
            apiCafe.getCart(idUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { cartModel ->
                        if (cartModel.isSuccess()) {
                            listCart = cartModel.getResult()
                            Utils.listPurchase = listCart
//                            for (item in Utils.listPurchase){
//                                total += item.product.getPrice() * item.quantity
//                                Log.d("listPurchase", item.product.toString())
//                                Log.d("listPurchase", item.quantity.toString())
//                                Log.d("listPurchase", item.product.getPrice().toString())
//                                Log.d("listPurchase", total.toString())
//                            }
                            totalAmount()
                            //  itemProductAdapter = ItemProductAdapter(listProduct, this)
                            itemCartAdapter = ItemCartAdapter(listCart, this)
                            recyclerView.adapter = itemCartAdapter
                            Log.e("idUser", "id ${Utils.userCurrent.getId()}")
                        } else {
                            Log.e("CartFragment", "Lỗi")
                        }
                    },
                    { throwable ->
                        Log.e("CartFragment", throwable.message.toString())
                        Toast.makeText(context, "Lỗi ${throwable.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
                )
        )
    }

    private fun initView() {
//         product = arguments?.getSerializable("productDetail") as Product?
//         product?.let { Log.d("productDetail", it.getName()) }
        val bundle = arguments
        if (bundle != null) {
            // Lấy product và quantity từ bundle
            val product = bundle.getSerializable("productDetail") as? Product
            val quantity = bundle.getInt("quantity")

            // Kiểm tra product không null trước khi sử dụng
            product?.let {
                Log.d("CartFragment", "Received product: ${it.getName()}, Quantity: $quantity")
                // Xử lý tiếp với product và quantity
            }
        }
        btnAddOrder = fragmentCartBinding.btnAddOrder
        recyclerView = fragmentCartBinding.recyclerCart
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        val dividerItemDecoration =
            DividerItemDecoration(recyclerView.context, layoutManager.orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)
        txtTotalAmount = fragmentCartBinding.txtSumPrice
        listCart = ArrayList()
        relativeAddress = fragmentCartBinding.relaAddressShipping
    }

    override fun onImageClick(cart: Cart, position: Int, value: Int) {
        var newQuantity = cart.quantity
        if (value == 1) {
            //Toast.makeText(context, "Xóa", Toast.LENGTH_SHORT).show()
            if (newQuantity > 1) {  // Đảm bảo số lượng không giảm về 0
                newQuantity -= 1
                cart.quantity = newQuantity
              //  Utils.listPurchase[position].quantity = newQuantity
                updateCart(Utils.userCurrent.getId(), cart.idProduct, newQuantity)
                itemCartAdapter.notifyItemChanged(position)
            } else {
                var builder: AlertDialog.Builder = AlertDialog.Builder(context)
                builder.setTitle("Thông báo")
                    .setIcon(R.drawable.baseline_announcement_24)
                    .setMessage("Bạn có muốn xóa sản phẩm khỏi giỏ hàng không")
                    .setPositiveButton("Đồng ý") { dialog, which ->
                        deleteCart(Utils.userCurrent.getId(), listCart[position].idProduct)
                        Utils.listPurchase.removeAt(position)
                        itemCartAdapter.notifyDataSetChanged()
                    }
                    .setNegativeButton("Hủy", {dialog, which->
                        dialog.dismiss()
                    })
                builder.show()
            }
        } else if (value == 2) {
            newQuantity += 1
            cart.quantity = newQuantity
           // Utils.listPurchase[position].quantity = newQuantity
            updateCart(Utils.userCurrent.getId(), cart.idProduct, newQuantity)
            itemCartAdapter.notifyItemChanged(position)
        }else if(value == 0){
            var builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle("Thông báo")
                .setIcon(R.drawable.baseline_announcement_24)
                .setMessage("Bạn có muốn xóa sản phẩm khỏi giỏ hàng không")
                .setPositiveButton("Đồng ý") { dialog, which ->
                    deleteCart(Utils.userCurrent.getId(), listCart[position].idProduct)
                    Utils.listPurchase.removeAt(position)
                    itemCartAdapter.notifyDataSetChanged()
                }
                .setNegativeButton("Hủy", {dialog, which->
                    dialog.dismiss()
                })
            builder.show()
        }
    }

    private fun deleteCart(idUser: Int, idProduct: Int) {
        compositeDisposable.add(apiCafe.deleteCart(idUser, idProduct)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { cartModel ->
                    if (cartModel.isSuccess()) {
                        Toast.makeText(
                            context,
                            "Đã xóa sản phẩm khỏi giỏ hàng",
                            Toast.LENGTH_SHORT
                        ).show()
                        totalAmount()
                    }
                }, {
                    Toast.makeText(context, it.message.toString(), Toast.LENGTH_SHORT).show()
                }
            )
        )
    }

    private fun updateCart(idUser: Int, idProduct: Int, quantity: Int) {
        compositeDisposable.add(apiCafe.updateCart(idUser, idProduct, quantity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                // Hiển thị một trạng thái đang tải hoặc disable nút tạm thời
            }
            .doFinally {
                // Kích hoạt lại nút hoặc cập nhật trạng thái sau khi hoàn tất
            }
            .subscribe(
                { cartModel ->
                    if (cartModel.isSuccess()) {
                        Toast.makeText(
                            context,
                            "Cập nhật giỏ hàng thành công",
                            Toast.LENGTH_SHORT
                        ).show()
                        totalAmount()
                        Log.d(
                            "CartFragment",
                            "Response: ${cartModel.getResult()}"
                        ) // Kiểm tra response
                    } else {
                        Log.e("CartFragment", "Cập nhật thất bại: ${cartModel.getMesage()}")
                    }
                },
                {
                    Toast.makeText(context, "lỗi ${it.message.toString()}", Toast.LENGTH_SHORT)
                        .show()
                }
            )
        )
    }
    private fun totalAmount(){
        total = 0
        for (item in Utils.listPurchase){
            Log.d("listPurchase", item.toString())
            total += item.product.getPrice() * item.quantity
        }

        txtTotalAmount.text = total.toString()
    }
}