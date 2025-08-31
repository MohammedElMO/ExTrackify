package com.example.extrackify

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.extrackify.constants.AuthType
import com.example.extrackify.databinding.ActivitySignUpBinding
import com.example.extrackify.utils.SessionManager
import com.example.extrackify.utils.navigation.NavigationUtils
import com.example.extrackify.view_model.AuthUIState
import com.example.extrackify.view_model.SessionViewModel
import com.example.extrackify.view_model.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SignUpScreen : AppCompatActivity() {
    private val signUpViewModel: SignUpViewModel by viewModels();
    private lateinit var binding: ActivitySignUpBinding
    private var overlay: View? = null

    private val sessionViewModel: SessionViewModel by viewModels()

    @Inject
    lateinit var sessionManager: SessionManager

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        binding = ActivitySignUpBinding.inflate(layoutInflater)

        binding.lifecycleOwner = this
        binding.signUpViewModel = signUpViewModel
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val root = findViewById<ViewGroup>(android.R.id.content)

        if (overlay == null) {
            overlay = LayoutInflater.from(this).inflate(R.layout.loading_overlay, root, false)
        }

        signUpViewModel.authState.observe(this) {
            when (it) {
                is AuthUIState.Success -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()


                    if (signUpViewModel.authType.value == AuthType.CREDENTIAL) {
                        NavigationUtils.navigateToActivity(
                            this@SignUpScreen, LoginScreen::class.java
                        )
                    } else {
                        NavigationUtils.navigateToActivity(
                            this@SignUpScreen, MainActivity::class.java
                        )
                        sessionViewModel.saveSession()

                    }

                }


                is AuthUIState.Error -> Toast.makeText(this, it.err, Toast.LENGTH_SHORT).show()


                AuthUIState.Loading -> {
                    root.addView(overlay)
                    Toast.makeText(this, "isLoading...", Toast.LENGTH_SHORT)
                        .show()
                }

                is AuthUIState.Validating -> Toast.makeText(
                    this,
                    it.validationMessage,
                    Toast.LENGTH_SHORT
                ).show()

                AuthUIState.Idle -> {
                    root.removeView(overlay)
                    return@observe
                }

            }
        }



        binding.signupBtn.setOnClickListener {
            signUpViewModel.onSignUp()
        }

        binding.googleSingUpBtn.root.setOnClickListener {

            signUpViewModel.googleSingUp(this@SignUpScreen)
        }

        binding.discordSingUpBtn.root.setOnClickListener {
            signUpViewModel.discordSingUp(this@SignUpScreen)
        }

        binding.githubSingUpBtn.root.setOnClickListener {
            signUpViewModel.githubSingUp(this@SignUpScreen)
        }

    }


}







