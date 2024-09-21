package com.example.cafe2.view.activity.user

import android.content.Intent
import android.content.SharedPreferences
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
import com.example.cafe2.util.GoogleSign
import com.example.cafe2.util.Utils
import com.example.cafe2.view.activity.admin.AdminActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.gson.Gson
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class LoginActivity : AppCompatActivity() {
    private lateinit var edtEmail: EditText
    private lateinit var edtPassWord: EditText
    private lateinit var rdGroup: RadioGroup
    private lateinit var btnLogin: AppCompatButton
    private lateinit var txtSignUp: TextView
    private lateinit var loginBinding: ActivityLoginBinding
    private val compositeDisposable = CompositeDisposable()
    private val apiCafe by lazy {
        RetrofitClient.getInstance(Utils.BASE_URL).create(ApiCafe::class.java)
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)


       googleSignInClient = GoogleSign.getGoogleSignInClient(applicationContext)

        // Initialize Firebase Auth
        auth = Firebase.auth
        loginBinding.btnSignInGoogle.setOnClickListener{
            signIn()
        }
        initView()
        initControl()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    if (user != null) {
                        val uid = user.uid
                        val name: String = user.displayName ?: "No Name"  // Nếu displayName là null, gán giá trị mặc định
                        val email: String = user.email ?: "No Email"      // Nếu email là null, gán giá trị mặc định
                        val photoUrl = user.photoUrl
                        val isEmailVerified = user.isEmailVerified
                        val creationTime = user.metadata?.creationTimestamp
                        val lastSignInTime = user.metadata?.lastSignInTimestamp
                        val phone: String = user.phoneNumber ?: "No Phone" // Nếu phoneNumber là null, gán giá trị mặc định
                        val providerData = user.providerData

                        // Hiển thị hoặc sử dụng thông tin người dùng
                        Log.d("FirebaseUser", "User ID: $uid")
                        Log.d("FirebaseUser", "Name: $name")
                        Log.d("FirebaseUser", "Email: $email")
                        Log.d("FirebaseUser", "Photo URL: $photoUrl")
                        Log.d("FirebaseUser", "Phone: $phone")
                        Log.d("FirebaseUser", "providerData: $providerData")

                        // Gọi hàm signUp với các giá trị không null

                        signUp(email, "", name, phone)
                        getUser(email)
                    }
                    updateUI(user)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun updateUI(user: FirebaseUser?) {
    }

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }

    private fun initView() {
        edtEmail = loginBinding.txtEmailLogin
        edtPassWord = loginBinding.txtPassLogin
        rdGroup = loginBinding.rdGroupLogin
        btnLogin = loginBinding.btnLogin
        txtSignUp = loginBinding.txtSignUp
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
                Toast.makeText(
                    applicationContext,
                    "Email không được để trống",
                    Toast.LENGTH_SHORT
                )
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

        txtSignUp.setOnClickListener {
            val intent = Intent(applicationContext, SignUpActivity::class.java)
            startActivity(intent)
            finish()
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
                            var userCurrent = Utils.userCurrent
                            val mypref: SharedPreferences =
                                getSharedPreferences("login", MODE_PRIVATE)
                            val editor = mypref.edit()
                            val gson = Gson()
                            val jsonUser = gson.toJson(userCurrent)
                            editor.putString("user", jsonUser)
                            editor.apply()
                            Log.d("UserCurrent", Utils.userCurrent.getId().toString())
                            val intent = Intent(applicationContext, AdminActivity::class.java)
                            startActivity(intent)

                        } else {

                            Utils.userCurrent = userModel.getResult()
                            var userCurrent = Utils.userCurrent
                            val mypref: SharedPreferences =
                                getSharedPreferences("login", MODE_PRIVATE)
                            val editor = mypref.edit()
                            val gson = Gson()
                            val jsonUser = gson.toJson(userCurrent)

// Lưu chuỗi JSON vào SharedPreferences
                            editor.putString("user", jsonUser)
                            editor.apply()
//                            editor.putString("email", userCurrent.getEmail())
//                            editor.putString("password", userCurrent.getPassword())
//                            editor.putInt("status", userCurrent.getStatus())
//                            editor.apply()


                            Log.d("UserCurrent", Utils.userCurrent.getId().toString())
                            Log.d("UserCurrent", jsonUser)
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

    private fun signUp(email: String, password: String, userName: String, mobile: String) {
        compositeDisposable.add(apiCafe.signUp(email, password, userName, mobile)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { userModel ->
                    if (userModel.isSuccess()) {
                        Utils.userCurrent.setEmail(email)
                        Utils.userCurrent.setPassword(password)
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

    private fun getUser(email: String) {
        compositeDisposable.add(apiCafe.getUser(email)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { userModel ->
                    if (userModel.isSuccess()) {
                        Utils.userCurrent = userModel.getResult()
                        var userCurrent = Utils.userCurrent
                        val mypref: SharedPreferences =
                            getSharedPreferences("login", MODE_PRIVATE)
                        val editor = mypref.edit()
                        val gson = Gson()
                        val jsonUser = gson.toJson(userCurrent)
                        editor.putString("user", jsonUser)
                        editor.apply()
                        Log.d("UserCurrent", Utils.userCurrent.getId().toString())

                    }
                }, {
                    Log.d("logingoogle", it.message.toString())
                    Toast.makeText(
                        applicationContext,
                        "Đăng ký không thành công",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        )

    }
}