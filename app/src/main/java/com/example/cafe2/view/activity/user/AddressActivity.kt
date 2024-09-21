package com.example.cafe2.view.activity.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cafe.model.User
import com.example.cafe2.R
import com.example.cafe2.adapter.user.ItemAddressAdapter
import com.example.cafe2.databinding.ActivityAddressBinding
import com.example.cafe2.databinding.LayoutBottomSheetBinding
import com.example.cafe2.model.user.Address
import com.example.cafe2.retrofit.ApiCafe
import com.example.cafe2.retrofit.RetrofitClient
import com.example.cafe2.util.Utils
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class AddressActivity : AppCompatActivity() {
    private lateinit var addressBinding: ActivityAddressBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnAddAddress: AppCompatButton
    private val compositeDisposable = CompositeDisposable()
    private val apiCafe by lazy {
        RetrofitClient.getInstance(Utils.BASE_URL).create(ApiCafe::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_address)
        addressBinding = DataBindingUtil.setContentView(this, R.layout.activity_address)

        initView()
        initControl()
        getAddress(Utils.userCurrent.getId())
    }

    private fun initControl() {
        btnAddAddress.setOnClickListener {

            val binding: LayoutBottomSheetBinding = DataBindingUtil.inflate(
                layoutInflater, R.layout.layout_bottom_sheet, null, false
            )

            val bottomSheetDialog: BottomSheetDialog = BottomSheetDialog(this)
            bottomSheetDialog.setContentView(binding.root)
            bottomSheetDialog.show()


            binding.btnCancel.setOnClickListener {
                bottomSheetDialog.dismiss()
            }

            binding.btnAddAdrress.setOnClickListener {
                val strUserName = binding.edtUsername.text.toString().trim()
                val strMobile = binding.edtMobile.text.toString().trim()
                val strAddress = binding.edtAddress.text.toString().trim()
                if(TextUtils.isEmpty(strUserName)){
                    binding.edtUsername.error = "Không được để trống"
                }else if(TextUtils.isEmpty(strMobile)){
                    binding.edtMobile.error = "Không được để trống"
                }else if(TextUtils.isEmpty(strAddress)){
                    binding.edtAddress.error = "Không được để trống"
                }else{
                    addAddress(Utils.userCurrent.getId(), strUserName, strMobile, strAddress)
                    getAddress(Utils.userCurrent.getId())
                }
            }


        }

    }

    private fun addAddress(idUser: Int, name: String, phone: String, address: String) {
        compositeDisposable.add(apiCafe.addAddress(idUser, name, phone, address)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { addressModel ->
                    if (addressModel.isSuccess()) {
                        Toast.makeText(
                            applicationContext,
                            "Thêm địa chỉ thành công",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }, {
                    Toast.makeText(applicationContext, "Lỗi ${it.message}", Toast.LENGTH_LONG)
                        .show()
                }
            )
        )
    }

    private fun getAddress(idUser: Int) {
        compositeDisposable.add(apiCafe.getAddress(idUser)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { addressModel ->
                    if (addressModel.isSuccess()) {
                        val listAddress: MutableList<Address> = addressModel.getResult()
                        val adapter = ItemAddressAdapter(listAddress)
                        recyclerView.adapter = adapter

                    }
                },
                { throwable ->
                    Toast.makeText(
                        applicationContext,
                        "Lỗi khi lấy địa chỉ",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            )
        )
    }

    private fun initView() {
        recyclerView = addressBinding.recyclerAddress
        val layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        val dividerItemDecoration =
            DividerItemDecoration(recyclerView.context, layoutManager.orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)
        btnAddAddress = addressBinding.btnAddress
    }
}