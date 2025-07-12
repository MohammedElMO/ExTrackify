package com.example.extrackify

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.extrackify.databinding.ActivityBiometricBinding
import com.example.extrackify.utils.navigation.NavigationUtils
import com.example.extrackify.view_model.BiometricFlowState
import com.example.extrackify.view_model.BiometricsViewModel

class BiometricActivity : AppCompatActivity() {
    val biometricViewModel: BiometricsViewModel by viewModels()
    lateinit var binding: ActivityBiometricBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBiometricBinding.inflate(layoutInflater)

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.biometricBtn.setOnClickListener { biometricViewModel.biometricSupportChecker(this@BiometricActivity) }


        biometricViewModel.biometricAuthState.observe(this) {
            when (it) {
                is BiometricFlowState.Success -> {
                    Toast.makeText(this@BiometricActivity, it.message, Toast.LENGTH_LONG).show()
                    NavigationUtils.navigateToActivity(
                        this@BiometricActivity,
                        MainActivity::class.java
                    )
                }
                is BiometricFlowState.Error -> Toast.makeText(
                    this@BiometricActivity,
                    it.err,
                    Toast.LENGTH_LONG
                ).show()

                is BiometricFlowState.Failed ->
                    Toast.makeText(this@BiometricActivity, it.reason, Toast.LENGTH_LONG).show()

                BiometricFlowState.Loading ->
                    Toast.makeText(this@BiometricActivity, "loading...", Toast.LENGTH_LONG).show()

                is BiometricFlowState.UnSupported ->
                    Toast.makeText(this@BiometricActivity, it.supportReason, Toast.LENGTH_LONG).show()


            }
        }




    }
}