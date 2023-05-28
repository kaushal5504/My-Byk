package com.tech.my_byk.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.AnimatedImageDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.tech.my_byk.LoginActivity
import com.tech.my_byk.R

class SplashActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.P)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val alphaAnimation = AnimationUtils.loadAnimation(applicationContext,R.anim.spalsh_anim)
        var spalsh_text:TextView= findViewById(R.id.splash_text)
        spalsh_text.startAnimation(alphaAnimation)




        Handler().postDelayed({
            val intent = Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()

        }, 5000)
    }
}