package com.example.extrackify

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.extrackify.utils.navigation.NavigationUtils
import com.example.extrackify.view_model.SplashScreenViewModel
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {
    private val splashViewModel: SplashScreenViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        splashViewModel.isLoadingAuthState.observe(this) { loaded ->
            if (!loaded) {
                val session = splashViewModel.session.value
                Log.d("session:splashscreen",session.toString())
                if (session != null) {
                    NavigationUtils.navigateToActivity(
                        this@SplashScreenActivity,
                        BiometricActivity::class.java
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