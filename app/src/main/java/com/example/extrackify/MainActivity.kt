package com.example.extrackify

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.extrackify.databinding.ActivityMainBinding
import com.example.extrackify.factory.AuthViewModelFactory
import com.example.extrackify.models.UserRepository
import com.example.extrackify.utils.navigation.NavigationUtils
import com.example.extrackify.view_model.AuthViewModel

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private lateinit var authView: AuthViewModel;

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
        val repo = UserRepository(this)
        val factory = AuthViewModelFactory(repo)

        authView = viewModels<AuthViewModel> { factory }.value




        binding.logout.setOnClickListener {
            authView.logout()
            NavigationUtils.navigateToActivity(this@MainActivity, WelcomeScreen::class.java)

        }


    }

}