package com.example.extrackify

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.extrackify.databinding.ActivityMainBinding
import com.example.extrackify.utils.navigation.NavigationUtils
import com.example.extrackify.view_model.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private val mainViewModel: MainActivityViewModel by viewModels();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
//
//        authView.isLoading.observe(this) { isLoading ->
//            if (!isLoading) {
//                val isAuth = authView.isAuthenticated.value
//                Log.d("auth:state", "$isAuth")
////                isAuth?.let {
////                    if (!it) {
////                        NavigationUtils.navigateToActivity(
////                            this@MainActivity,
////                            WelcomeScreen::class.java
////                        )
////
////                    }
////                }
//            }
//        }


        binding.logout.setOnClickListener {


            mainViewModel.logout()

            NavigationUtils.navigateToActivity(
                this@MainActivity,
                WelcomeScreen::class.java
            )

        }


    }

}