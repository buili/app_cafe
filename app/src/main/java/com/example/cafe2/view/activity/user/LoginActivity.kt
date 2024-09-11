package com.example.cafe2.view.activity.user

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.DataBindingUtil
import com.example.cafe2.R
import com.example.cafe2.databinding.ActivityLoginBinding
import com.example.cafe2.retrofit.ApiCafe
import com.example.cafe2.retrofit.RetrofitClient
import com.example.cafe2.util.Utils
import com.example.cafe2.view.activity.admin.AdminActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class LoginActivity : AppCompatActivity() {
    private lateinit var edtEmail: EditText
    private lateinit var edtPassWord: EditText
    private lateinit var rdGroup: RadioGroup
    private lateinit var btnLogin: AppCompatButton
    private lateinit var txtSigin: TextView
    private lateinit var loginBinding: ActivityLoginBinding
    private val compositeDisposable = CompositeDisposable()
    private val apiCafe by lazy {
        RetrofitClient.getInstance(Utils.BASE_URL).create(ApiCafe::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        initView()
        initControl()
    }


    private fun initView() {
        edtEmail = loginBinding.txtEmailLogin
        edtPassWord = loginBinding.txtPassLogin
        rdGroup = loginBinding.rdGroupLogin
        btnLogin = loginBinding.btnLogin
        txtSigin = loginBinding.txtSigin
    }


    private fun initControl() {
        btnLogin.setOnClickListener {
            val str_email: String = edtEmail.text.toString().trim()
            val str_pass: String = edtPassWord.text.toString().trim()
            val idSelected: Int = rdGroup.checkedRadioButtonId
            var role = 2
            if (idSelected != -1) {
                val rdSelected: RadioButton = findViewById<RadioButton>(idSelected)
                var status: String = rdSelected.text.toString()
                role = if (status == "Admin") 0 else 1
            }
            if (TextUtils.isEmpty(str_email)) {
                edtEmail.error = "Email không được để trống"
                Toast.makeText(applicationContext, "Email không được để trống", Toast.LENGTH_SHORT)
                    .show()
            } else if (TextUtils.isEmpty(str_pass)) {
                edtPassWord.error = "Password không được để trống"
                Toast.makeText(
                    applicationContext,
                    "Password không được để trống",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(str_email).matches()) {
                edtEmail.error = "Email không đúng định dạng"
                Toast.makeText(
                    applicationContext,
                    "Email không đúng định dạng",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (str_pass.length < 6) {
                edtPassWord.error = "Password phải lớn hơn 6 ký tự"
                Toast.makeText(
                    applicationContext,
                    "Password phải lớn hơn 6 ký tự",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (idSelected == -1) {
                Toast.makeText(
                    applicationContext,
                    "Vui lòng chọn loại người dùng",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (role == 0) {
                    login(str_email, str_pass, 0)
                }
                if (role == 1) {
                    login(str_email, str_pass, 1)
                }

            }
        }
    }

    private fun login(email: String, password: String, status: Int) {
        compositeDisposable.add(apiCafe.login(email, password, status)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { userModel ->
                    if (userModel.isSuccess()) {
                        if (status == 0) {
                            Utils.userCurrent = userModel.getResult()
                            Log.d("UserCurrent", Utils.userCurrent.getId().toString())
                            val intent = Intent(applicationContext, AdminActivity::class.java)
                            startActivity(intent)

                        }else {
                            Utils.userCurrent = userModel.getResult()
                            Log.d("UserCurrent", Utils.userCurrent.getId().toString())
                            val intent = Intent(applicationContext, MainActivity::class.java)
                            startActivity(intent)

                        }
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Email hoặc mật khẩu không đúng",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                { throwable ->
                    Log.e("LoiLogin", throwable.message.toString())
//                    Toast.makeText(
//                        applicationContext,
//                        throwable.message,
//                        Toast.LENGTH_SHORT
//                    ).show()
                    Toast.makeText(
                        applicationContext,
                        "Email hoặc mật khẩu không đúng",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            )
        )
    }

}