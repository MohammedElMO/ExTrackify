package com.example.extrackify.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.extrackify.models.UserRepository
import com.example.extrackify.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import io.appwrite.exceptions.AppwriteException
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val userRepo: UserRepository,
    private val sessionManager: SessionManager
) :
    ViewModel() {

    fun logout() {
        viewModelScope.launch {
            try {
                userRepo.logout()

            } catch (e: AppwriteException) {
                e.printStackTrace()
                Log.d("appwrite:auth", "${e.message}")

            } finally {
                sessionManager.clearStore()

            }
        }

    }

}