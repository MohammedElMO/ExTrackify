package com.example.extrackify.view_model

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.extrackify.models.UserRepository
import com.example.extrackify.utils.DataStore
import com.example.extrackify.utils.SessionManager
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepo: UserRepository,
    private val sessionManager: SessionManager,
    private val dataStore: DataStore
) : ViewModel() {


    private val _isAuthenticated = MutableLiveData<Boolean>(false)


    val isAuthenticated: LiveData<Boolean>
        get() = _isAuthenticated


    init {

        viewModelScope.launch {
            Log.d("auth:state", sessionManager.isSessionValid().toString())
            _isAuthenticated.value = !sessionManager.isSessionValid()
        }

    }



    fun googleIdBuilder(ctx: Context) {


    }


}


