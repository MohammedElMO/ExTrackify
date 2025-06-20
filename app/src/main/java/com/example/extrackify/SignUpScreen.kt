package com.example.extrackify

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.extrackify.databinding.ActivitySignUpBinding
import com.example.extrackify.utils.popups.SnackBarHelper


class SignUpScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var userNameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var signUpBtn: Button
    private lateinit var googleSignUpBtn: Button
    private lateinit var snackBarHelper: SnackBarHelper;

    //    private lateinit val toastHelper
    val MIN_PASSWORD_LENGTH = 8


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        userNameInput = binding.username
        emailInput = binding.email
        passwordInput = binding.password
        signUpBtn = binding.signupBtn
        googleSignUpBtn = binding.googleSignupBtn
        snackBarHelper = SnackBarHelper(context = applicationContext)
        signUpBtn.setOnClickListener {
            onLogin()
        }
    }

    private fun checkIfValidEmail(emailText: String): Boolean =
        (!emailText.matches(Regex("[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}")))

    private fun checkPasswordLength(passwordText: String): Boolean =
        passwordText.length == MIN_PASSWORD_LENGTH

    private fun checkEmptyInputs(emailText: String, passwordText: String): Boolean =
        (emailText.trim().isEmpty() || passwordText.trim().isEmpty())

    private fun onLogin() {

//        checkPasswordLength(passwordInput.text.toString())
//        checkIfValidEmail(emailInput.text.toString())
        if (checkEmptyInputs(
                emailText = emailInput.text.toString(), passwordText = passwordInput.text.toString()
            )
        ) {
            return snackBarHelper.makeSnackBar(
                "both email and password must be ToastTypeprovided",
                view = binding.root,
                iconRes = R.drawable.success_icon
            )


        }
    }
}