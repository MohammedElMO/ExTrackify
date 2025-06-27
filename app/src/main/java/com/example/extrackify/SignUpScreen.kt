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
import com.example.extrackify.factory.AuthViewModelFactory
import com.example.extrackify.models.UserRepository
import com.example.extrackify.utils.navigation.NavigationUtils
import com.example.extrackify.view_model.AuthViewModel


class SignUpScreen : AppCompatActivity() {
    private lateinit var authView: AuthViewModel;
    private lateinit var binding: ActivitySignUpBinding
    private var overlay: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repo = UserRepository(this)
        val factory = AuthViewModelFactory(repo)


        authView = viewModels<AuthViewModel> { factory }.value

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
            if (loading)
                root.addView(overlay)
            else {
                root.removeView(overlay)

            }
        }

        authView.errorMessage.observe(this) { err_messsage ->
            Toast.makeText(this, err_messsage, Toast.LENGTH_SHORT).show()


        }


        binding.signupBtn.setOnClickListener {
            authView
                .onSignUp()
        }
        binding.googleSignupBtn.setOnClickListener {

        }


        authView.isAuthentificated.observe(this) { authenticated ->
            if (authenticated) {
                NavigationUtils.navigateToActivity(this, MainActivity::class.java)

                finish()

            }

        }

    }


}