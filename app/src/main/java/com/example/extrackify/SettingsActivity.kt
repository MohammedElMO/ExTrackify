package com.example.extrackify

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.extrackify.databinding.ActivitySettingsBinding
import com.example.extrackify.utils.navigation.NavigationUtils
import com.example.extrackify.view_model.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivitySettingsBinding
    private val settingsViewModel: SettingsViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        setupUI()
        observeViewModel()
    }
    
    private fun setupUI() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        
        binding.buttonLogout.setOnClickListener {
            settingsViewModel.logout()
        }
        
        binding.buttonProfile.setOnClickListener {
            NavigationUtils.navigateToActivity(this, ProfileActivity::class.java)
        }
        
        binding.buttonNotifications.setOnClickListener {
            // Navigate to notification settings
        }
        
        binding.buttonAbout.setOnClickListener {
            // Navigate to about screen
        }
    }
    
    private fun observeViewModel() {
        settingsViewModel.logoutState.observe(this) { success ->
            if (success) {
                NavigationUtils.navigateToActivity(this, WelcomeScreen::class.java)
                finishAffinity()
            }
        }
    }
}