package com.example.extrackify.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.extrackify.constants.AuthType
import com.example.extrackify.models.UserRepository
import com.example.extrackify.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import io.appwrite.exceptions.AppwriteException
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepo: UserRepository,
    private val sessionManager: SessionManager,
): BaseAuthViewModel(userRepo) {




    val email: MutableLiveData<String>
        get() = _email

    val password : MutableLiveData<String>
        get() = _password

    val authState : MutableLiveData<AuthUIState>
        get() = _authState


    fun onLogin() {
        if (!checkEmptyInputs()) {
            _authState.value = AuthUIState.Validating("all Fields must be filled")
            return
        }

        _authState.value = AuthUIState.Loading


        viewModelScope.launch {
            try {
                userRepo.login(
                    email = email.value.toString(),
                    password = password.value.toString()
                )
                _authState.value = AuthUIState.Success("Success")
            } catch (e: AppwriteException) {

                Log.d("error appwrite", e.message.toString())
                _authState.value = AuthUIState.Error("${e.message}")

            } finally {
                _authState.value = AuthUIState.Idle

            }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }



}