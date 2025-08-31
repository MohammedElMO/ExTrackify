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
class SignUpViewModel @Inject constructor(
    private val userRepo: UserRepository,
    private val sessionManager: SessionManager,
) : BaseAuthViewModel(userRepo) {


    private val MIN_PASSWORD_LENGTH = 8

    val authState: LiveData<AuthUIState>
        get() = _authState


    val authType: MutableLiveData<AuthType>
        get() = _authType


    private val _username = MutableLiveData("adadgag")


    val username: MutableLiveData<String>
        get() = _username

    val email: MutableLiveData<String>
        get() = _email

    val password: MutableLiveData<String>
        get() = _password

    fun checkIfValidEmail(emailText: String): Boolean =
        (emailText.matches(Regex("[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}")))

    fun checkPasswordLength(passwordText: String): Boolean =
        passwordText.trim().length >= MIN_PASSWORD_LENGTH


    private fun validateFields(): Boolean {
        val userName: String = _username.value.toString()
        val emailText: String = _email.value.toString()
        val passwordText: String = _password.value.toString()

        val isValidPass = checkPasswordLength(passwordText)
        val isValidEmail = checkIfValidEmail(emailText)
        val areEmpty = checkEmptyInputs(
            emailText, passwordText, userName
        )

        if (areEmpty) {
            _authState.value = AuthUIState.Validating("all Fields must be filled")
            return false
        }
        if (!isValidPass) {
            _authState.value = AuthUIState.Validating("password must be 8+ combinations")

            return false
        }
        if (!isValidEmail) {
            _authState.value = AuthUIState.Validating("you should provide a valid email")

            return false
        }

        return true
    }


    fun onSignUp() {
        if (!validateFields()) return
        _authState.value = AuthUIState.Loading
        _authType.value = AuthType.CREDENTIAL

        viewModelScope.launch {
            try {
                userRepo.signUp(
                    username = _username.value.toString(),
                    email = _email.value.toString(),
                    password = _password.value.toString()
                )
                _authState.value = AuthUIState.Success("Success")

                userRepo.verifyEmail()
            } catch (e: AppwriteException) {
                Log.d("error appwrite", e.message.toString())

                _authState.value = AuthUIState.Error("${e.message}")


            } finally {
                _authState.value = AuthUIState.Idle

            }

        }
    }


}