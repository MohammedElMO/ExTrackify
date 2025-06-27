package com.example.extrackify

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.extrackify.factory.SplashViewModelFactory
import com.example.extrackify.models.UserRepository
import com.example.extrackify.utils.SessionManager
import com.example.extrackify.utils.navigation.NavigationUtils
import com.example.extrackify.view_model.SplashScreenViewModel

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var splashViewModel: SplashScreenViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val repo = UserRepository(this)
        val sessionManager = SessionManager(this)

        val factory = SplashViewModelFactory(repo, sessionManager)

        splashViewModel = viewModels<SplashScreenViewModel> { factory }.value



        splashViewModel.isLoadingSession.observe(this) { loaded ->
            if (!loaded) {
                val session = splashViewModel.session.value
                Log.d("appwrite:auth", "$session")
                if (session != null) {
                    NavigationUtils.navigateToActivity(
                        this@SplashScreenActivity,
                        MainActivity::class.java
                    )
                } else {
                    NavigationUtils.navigateToActivity(
                        this@SplashScreenActivity,
                        WelcomeScreen::class.java
                    )
                }
            }

        }


    }
}