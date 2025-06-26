package com.example.extrackify.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.extrackify.models.UserRepository
import io.appwrite.exceptions.AppwriteException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

sealed class ValidationResult {
    object Success : ValidationResult()
    data class Error(val message: String) : ValidationResult()
}

class Event<out T>(private val content: T) {
    private var hasBeenHandled = false
    fun getContentIfNotHandled(): T? = if (hasBeenHandled) null else {
        hasBeenHandled = true
        content
    }
}

class AuthViewModel(private val userRepo: UserRepository) : ViewModel() {
    private val MIN_PASSWORD_LENGTH = 8

    val username = MutableLiveData("")
    val email = MutableLiveData("")
    val password = MutableLiveData("")
    private val _isLoading = MutableLiveData(false)
    private val _errorMessage = MutableLiveData("")
    private val _toastMessage = MutableLiveData<String>(null)

    val isLoading: LiveData<Boolean>
        get() = _isLoading

    val errorMessage: LiveData<String>
        get() = _errorMessage

    val toastMessage: LiveData<String>
        get() = _toastMessage


    private fun checkIfValidEmail(emailText: String): Boolean =
        (emailText.matches(Regex("[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}")))

    private fun checkPasswordLength(passwordText: String): Boolean =
        passwordText.trim().length >= MIN_PASSWORD_LENGTH

    private fun checkEmptyInputs(
        username: String,
        emailText: String,
        passwordText: String
    ): Boolean =
        (emailText.trim().isEmpty() || passwordText.trim().isEmpty() || username.trim().isEmpty())

    private fun validateFields(): Boolean {
        val isValidPass = checkPasswordLength(password.value.toString())
        val isValidEmail = checkIfValidEmail(email.value.toString())
        val areEmpty = checkEmptyInputs(
            emailText = email.value.toString(),
            passwordText = password.value.toString(),
            username = username.value.toString()
        )

        if (areEmpty) {
            _toastMessage.value = "all Fields must be filled"
            return false
        }
        if (!isValidPass) {
            _toastMessage.value = "password must be 8+ combinations"

            return false
        }
        if (!isValidEmail) {
            _toastMessage.value = "you should provide a valid email"

            return false
        }

        return true
    }

    fun onSignUp() {
        Log.d("values", "${username.value} ${email.value} ${password.value}")

        if (!validateFields()) return
        _isLoading.value = true


        viewModelScope.launch {
            try {
                delay(5000)

//                userRepo.createUser(
//                    username = username.value.toString(),
//                    email = email.value.toString(),
//                    password = password.value.toString()
//                )
                _toastMessage.value = "Success"
            } catch (e: AppwriteException) {
                Log.d("error appwrite", e.message.toString())
                _errorMessage.value = e.message.toString()
            } finally {
                _isLoading.value = false
            }

        }
    }

    override fun onCleared() {
        super.onCleared()
        // Clean up resources if necessary (e.g., cancel coroutines, close database connections)
        println("AuthViewModel onCleared: ViewModel is being destroyed.")
    }

}
