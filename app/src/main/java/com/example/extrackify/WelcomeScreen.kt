package com.example.extrackify

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.extrackify.databinding.ActivityWelcomeBinding
import com.example.extrackify.factory.SplashViewModelFactory
import com.example.extrackify.models.UserRepository
import com.example.extrackify.utils.SessionManager
import com.example.extrackify.utils.navigation.NavigationUtils
import com.example.extrackify.view_model.SplashScreenViewModel

class WelcomeScreen : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var splashViewModel: SplashScreenViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        binding = ActivityWelcomeBinding.inflate(layoutInflater)

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val splashScreen = installSplashScreen()

        val repo = UserRepository(this)
        val sessionManager = SessionManager(this)

        val factory = SplashViewModelFactory(repo, sessionManager)

        splashViewModel = viewModels<SplashScreenViewModel> { factory }.value


        splashScreen.setKeepOnScreenCondition {
            splashViewModel.isLoadingSession.value == true
        }

        splashViewModel.session.observe(this) {
            NavigationUtils.navigateToActivity(this, MainActivity::class.java)
        }


        super.onCreate(savedInstanceState)



        binding.toLogin.setOnClickListener {
            NavigationUtils.navigateToActivity(
                this,
                LoginScreen::class.java
            )
        }

        binding.toSignUp.setOnClickListener {
            NavigationUtils.navigateToActivity(
                this,
                SignUpScreen::class.java
            )
        }

    }
}