package com.example.extrackify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.extrackify.databinding.ActivitySignUpBinding
import com.example.extrackify.utils.SessionManager
import com.example.extrackify.utils.navigation.NavigationUtils
import com.example.extrackify.view_model.AuthViewModel
import com.example.extrackify.view_model.SessionViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignUpScreen : AppCompatActivity() {
    private val authView: AuthViewModel by viewModels();
    private lateinit var binding: ActivitySignUpBinding
    private var overlay: View? = null

    private val sessionViewModel: SessionViewModel by viewModels()

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        binding = ActivitySignUpBinding.inflate(layoutInflater)

        binding.lifecycleOwner = this
        binding.authView = authView
        setContentView(binding.root)

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

        authView.errorMessage.observe(this) { err ->
            Toast.makeText(this, err, Toast.LENGTH_SHORT).show()


        }
        binding.signupBtn.setOnClickListener {
            authView.onSignUp()
        }

        binding.googleSingUpBtn.root.setOnClickListener {

            authView.googleSignUp(this@SignUpScreen)
        }


        authView.isAuthenticated.observe(this) { isAuth ->
            if (isAuth) {

                sessionViewModel.setSession()

                NavigationUtils.navigateToActivity(
                    this@SignUpScreen, MainActivity::class.java
                )


            }

        }


    }


}







