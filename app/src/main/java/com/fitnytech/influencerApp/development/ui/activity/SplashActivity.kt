package com.fitnytech.influencerApp.development.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.fitnytech.influencerApp.development.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed(Runnable { //This method will be executed once the timer is over
           //  Start your app main activity
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)

            // close this activity
            finish()
            overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);

        }, 2000)
    }
}