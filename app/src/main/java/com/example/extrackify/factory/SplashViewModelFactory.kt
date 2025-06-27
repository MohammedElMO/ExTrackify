package com.example.extrackify.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.extrackify.models.UserRepository
import com.example.extrackify.utils.SessionManager
import com.example.extrackify.view_model.SplashScreenViewModel

class SplashViewModelFactory(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(SplashScreenViewModel::class.java)) {
            return SplashScreenViewModel(userRepository, sessionManager) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }

}