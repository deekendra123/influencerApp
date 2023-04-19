package com.fitnytech.influencerApp.development.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.fitnytech.influencerApp.development.R
import com.fitnytech.influencerApp.development.preference.SavedPreference
import com.fitnytech.influencerApp.development.databinding.ActivityHomeBinding
import com.fitnytech.influencerApp.development.ui.fragment.PostFragment
import com.fitnytech.influencerApp.development.ui.fragment.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView



class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)

        SavedPreference.getCode(this)?.let { Log.e("USER DETAILS", it) }
        if (savedInstanceState == null) {
            val fragment = PostFragment()
            supportFragmentManager.beginTransaction().replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                .commit()
        }
        if (SavedPreference.getCode(this).equals("0")){
            binding.addPost.visibility = View.GONE
        }


        binding.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        binding.addPost.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, PostActivity::class.java))
         //   startActivity(Intent(this, MediaPickerActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);


        })

    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
        when (menuItem.itemId) {
            R.id.home -> {
                val fragment = PostFragment()
                supportFragmentManager.beginTransaction().replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                    .commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.profile -> {
                val fragment = ProfileFragment()
                supportFragmentManager.beginTransaction().replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                    .commit()
                return@OnNavigationItemSelectedListener true
            }

        }
        false
    }
    override fun onResume() {
        super.onResume()

    }

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        com.fitnytech.influencerApp.development.utils.Utils.showToast(this,"Please click BACK again to exit")
        Handler(Looper.getMainLooper()).postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }
}