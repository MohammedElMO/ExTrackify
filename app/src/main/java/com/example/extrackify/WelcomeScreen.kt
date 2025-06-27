package com.example.extrackify

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.extrackify.databinding.ActivityWelcomeBinding
import com.example.extrackify.utils.navigation.NavigationUtils

class WelcomeScreen : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        binding = ActivityWelcomeBinding.inflate(layoutInflater)

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.toLogin.setOnClickListener {
            NavigationUtils.navigateToActivity(
                this,
                LoginScreen::class.java
            )
        }

        binding.toSignUp.setOnClickListener {
            NavigationUtils.navigateToActivity(
                this,
                SignUpScreen::class.java
            )
        }

    }
}