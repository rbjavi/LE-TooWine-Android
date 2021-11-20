package com.jruizb.toowine.launch

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jruizb.toowine.R
import com.jruizb.toowine.databinding.ActivityHomeBinding
import com.jruizb.toowine.databinding.ActivityHomeBinding.inflate
import com.jruizb.toowine.home.HomeActivity

class LaunchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
//        Thread.sleep(1200)
        setTheme(R.style.SplashTheme)

        super.onCreate(savedInstanceState)
        binding = inflate(layoutInflater)
        setContentView(binding.root)

        showHomeActivity()


    }


    private fun showHomeActivity() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

}