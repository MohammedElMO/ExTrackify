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
import com.example.extrackify.view_model.AuthViewModel
import com.example.extrackify.view_model.SessionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginScreen : AppCompatActivity() {

    private val authView: AuthViewModel by viewModels();
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
        binding.authView = authView

        authView.toastMessage.observe(this) { message ->
            message?.let {

                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }

        }

        authView.isLoading.observe(this) { loading ->
            val root = findViewById<ViewGroup>(android.R.id.content)

            if (overlay == null) {
                overlay = LayoutInflater.from(this).inflate(R.layout.loading_overlay, root, false)
            }
            if (loading) root.addView(overlay)
            else {
                root.removeView(overlay)

            }
        }

        authView.errorMessage.observe(this) { err_messsage ->
            err_messsage.let {

                Toast.makeText(this, err_messsage, Toast.LENGTH_SHORT).show()
            }


        }

        authView.isAuthenticated.observe(this) { isAuth ->
            if (isAuth) {
                sessionViewModel.setSession()

                NavigationUtils.navigateToActivity(
                    this@LoginScreen, MainActivity::class.java
                )
            }

        }




        binding.googleLoginBtn.setOnClickListener {
            authView.googleSignUp(this@LoginScreen)
        }

        binding.loginBtn.setOnClickListener {
            authView.onLogin()
        }


    }


}