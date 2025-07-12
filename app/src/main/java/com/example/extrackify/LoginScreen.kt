package com.example.extrackify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.extrackify.databinding.ActivityLoginBinding
import com.example.extrackify.utils.navigation.NavigationUtils
import com.example.extrackify.view_model.AuthUIState
import com.example.extrackify.view_model.LoginViewModel
import com.example.extrackify.view_model.SessionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginScreen : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by viewModels();
    private lateinit var binding: ActivityLoginBinding
    private var overlay: View? = null

    private val sessionViewModel: SessionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.lifecycleOwner = this
        binding.loginViewModel = loginViewModel

        val root = findViewById<ViewGroup>(android.R.id.content)

        if (overlay == null) {
            overlay = LayoutInflater.from(this).inflate(R.layout.loading_overlay, root, false)
        }

        loginViewModel.authState.observe(this) {
            when (it) {
                is AuthUIState.Success -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    NavigationUtils.navigateToActivity(
                        this@LoginScreen, MainActivity::class.java
                    )
                    sessionViewModel.saveSession()
                }

                is AuthUIState.Error -> Toast.makeText(this, it.err, Toast.LENGTH_LONG).show()
                AuthUIState.Loading -> {
                    root.addView(overlay)
                    Toast.makeText(this, "isLoading...", Toast.LENGTH_LONG)
                        .show()
                }

                is AuthUIState.Validating -> Toast.makeText(
                    this,
                    it.validationMessage,
                    Toast.LENGTH_LONG
                ).show()

                AuthUIState.Idle -> {
                    root.removeView(overlay)
                    return@observe
                }

            }
        }


//        binding.googleLoginBtn.setOnClickListener {
//            loginViewModel.googleSignUp(this@LoginScreen)
//        }

        binding.loginBtn.setOnClickListener {
            loginViewModel.onLogin()
        }


    }


}