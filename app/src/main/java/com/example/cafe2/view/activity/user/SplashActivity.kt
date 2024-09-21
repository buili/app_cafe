package com.example.cafe2.view.activity.user

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.cafe.model.User
import com.example.cafe2.R
import com.example.cafe2.databinding.ActivitySplashBinding
import com.example.cafe2.util.Utils
import com.example.cafe2.view.activity.admin.AdminActivity
import com.google.gson.Gson

class SplashActivity : AppCompatActivity() {
    private lateinit var splashBinding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_splash)
        splashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash)

        val thread = Thread{
            try {
                Thread.sleep(1500)
            }catch (ex:Exception){
                ex.printStackTrace()
            }finally {
                val mypref:SharedPreferences = getSharedPreferences("login", MODE_PRIVATE)
                val jsonUser = mypref.getString("user", null)
                var user: User? = null
                if (jsonUser != null) {
                    // Chuyển chuỗi JSON thành đối tượng User
                    val gson = Gson()
                    user = gson.fromJson(jsonUser, User::class.java)
                }

                if(user == null){
                    Log.d("Splash", "Không user")
                    val intent = Intent(applicationContext, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    Log.d("Splash", "có user: ${user.getEmail()} , id: ${user.getId()}")
                    Utils.userCurrent = user
                    if(user.getStatus() == 1){
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        val intent = Intent(applicationContext, AdminActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                }
            }
        }
        thread.start()

    }
}