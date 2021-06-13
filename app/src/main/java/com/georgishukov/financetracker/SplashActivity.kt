package com.georgishukov.financetracker

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.georgishukov.financetracker.databinding.ActivitySplashBinding

private lateinit var binding: ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    override fun onBackPressed() {}

    override fun onPause() {
        super.onPause()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val timerThread: Thread = object : Thread() {
            override fun run() {
                try {
                    binding.logo.startAnimation(
                        AnimationUtils.loadAnimation(
                            applicationContext,
                            R.anim.zoom_in_fade_in
                        )
                    )
                    sleep(3000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } finally {
                    val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        timerThread.start()
    }
}