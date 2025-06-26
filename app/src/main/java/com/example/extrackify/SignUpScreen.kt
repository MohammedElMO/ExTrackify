package com.example.extrackify

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.extrackify.databinding.ActivitySignUpBinding
import com.example.extrackify.factory.AuthViewModelFactory
import com.example.extrackify.models.UserRepository
import com.example.extrackify.view_model.AuthViewModel


class SignUpScreen : AppCompatActivity() {
    private lateinit var authView: AuthViewModel;
    private lateinit var binding: ActivitySignUpBinding

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
        binding.signupBtn.setOnClickListener {
            authView
                .onSignUp()
        }


    }


}