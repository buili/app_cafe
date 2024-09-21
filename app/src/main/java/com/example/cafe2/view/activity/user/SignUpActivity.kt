package com.example.cafe2.view.activity.user

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.DataBindingUtil
import com.example.cafe2.R
import com.example.cafe2.databinding.ActivitySignUpBinding
import com.example.cafe2.retrofit.ApiCafe
import com.example.cafe2.retrofit.RetrofitClient
import com.example.cafe2.util.Utils
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class SignUpActivity : AppCompatActivity() {
    private lateinit var signUpBinding: ActivitySignUpBinding
    private lateinit var edtEmail: EditText
    private lateinit var edtUserName: EditText
    private lateinit var edtMobie: EditText
    private lateinit var edtPassword: EditText
    private lateinit var edtRePassword: EditText
    private lateinit var btnSignUp: AppCompatButton
    private lateinit var firebaseAuth:FirebaseAuth
    private val compositeDisposable = CompositeDisposable()
    private val apiCafe by lazy {
        RetrofitClient.getInstance(Utils.BASE_URL).create(ApiCafe::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_register)
        signUpBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        initView()
        initControl()

    }

    private fun initControl() {
        btnSignUp.setOnClickListener {
            val str_email: String = edtEmail.getText().toString()
            val str_username: String = edtUserName.getText().toString()
            val str_mobile: String = edtMobie.getText().toString()
            val str_pass: String = edtPassword.getText().toString()
            val str_repass: String = edtRePassword.getText().toString()

            if (TextUtils.isEmpty(str_email)) {
                Toast.makeText(applicationContext, "Bạn chưa nhập email", Toast.LENGTH_SHORT).show()
            } else if (TextUtils.isEmpty(str_username)) {
                Toast.makeText(applicationContext, "Bạn chưa nhập username", Toast.LENGTH_SHORT)
                    .show()
            } else if (TextUtils.isEmpty(str_mobile)) {
                Toast.makeText(
                    applicationContext,
                    "Bạn chưa nhập số điện thoại",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else if (TextUtils.isEmpty(str_pass)) {
                Toast.makeText(applicationContext, "Bạn chưa nhập pass", Toast.LENGTH_SHORT).show()
            } else if (TextUtils.isEmpty(str_repass)) {
                Toast.makeText(applicationContext, "Bạn chưa nhập pass", Toast.LENGTH_SHORT).show()
            } else {
                if (str_pass.equals(str_repass)) {
                    firebaseAuth = FirebaseAuth.getInstance()

                    signUp(str_email, str_pass, str_username, str_mobile)
                } else {
                    Toast.makeText(
                        applicationContext,
                        "PassWord không trùng nhau",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
    }

    private fun signUp(email: String, password: String, userName: String, mobile: String) {
        compositeDisposable.add(apiCafe.signUp(email, password, userName, mobile)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { userModel ->
                    if (userModel.isSuccess()) {
                        Utils.userCurrent.setEmail(email)
                        Utils.userCurrent.setPassword(password)
                        val intent = Intent(applicationContext, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }, {
                    Toast.makeText(
                        applicationContext,
                        "Đăng ký không thành công",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        )

    }

    private fun initView() {
        edtEmail = signUpBinding.edtEmail
        edtUserName = signUpBinding.edtUsername
        edtMobie = signUpBinding.edtMobile
        edtPassword = signUpBinding.edtPass
        edtRePassword = signUpBinding.edtRepass
        btnSignUp = signUpBinding.btnSignUp
    }
}