package com.example.extrackify

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.extrackify.databinding.ActivityVerificationSuccessBinding
import com.example.extrackify.utils.navigation.NavigationUtils

class VerificationSuccessActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityVerificationSuccessBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        binding = ActivityVerificationSuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        binding.buttonContinue.setOnClickListener {
            NavigationUtils.navigateToActivity(this, LoginScreen::class.java)
            finish()
        }
    }
}