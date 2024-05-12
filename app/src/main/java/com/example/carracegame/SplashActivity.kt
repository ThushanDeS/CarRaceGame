package com.example.carracegame

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.carracegame.R.id.splashLayout

class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT: Long = 2000
    private var splashHandler: Handler? = null
    private var splashRunnable: Runnable? = null
    private var shouldNavigate: Boolean = true

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splashHandler = Handler()
        splashRunnable = Runnable {
            if (shouldNavigate) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        splashHandler?.postDelayed(splashRunnable!!, SPLASH_TIME_OUT)

        findViewById<View>(splashLayout).setOnClickListener {
            shouldNavigate = true
            splashHandler?.removeCallbacks(splashRunnable!!)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        splashHandler?.removeCallbacks(splashRunnable!!)
    }
}
