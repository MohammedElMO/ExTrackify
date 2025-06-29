package com.example.extrackify.view_model

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.extrackify.constants.AuthType
import com.example.extrackify.models.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.appwrite.exceptions.AppwriteException
import kotlinx.coroutines.launch
import javax.inject.Inject
private val MIN_PASSWORD_LENGTH = 8


@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepo: UserRepository) : ViewModel() {

    val username = MutableLiveData("adadgag")
    val email = MutableLiveData("mohammed@gmail.com")
    val password = MutableLiveData("mohammed123")
    private val _isLoading = MutableLiveData(false)
    private val _errorMessage = MutableLiveData("")
    private val _toastMessage = MutableLiveData<String>(null)
    private val _isAuthentificated = MutableLiveData<Boolean>(false)
    private val _authType = MutableLiveData<AuthType?>()

    val isLoading: LiveData<Boolean>
        get() = _isLoading


    val authType: LiveData<AuthType?>
        get() = _authType

    val isAuthentificated: LiveData<Boolean>
        get() = _isAuthentificated


    val errorMessage: LiveData<String>
        get() = _errorMessage

    val toastMessage: LiveData<String>
        get() = _toastMessage


    private fun checkIfValidEmail(emailText: String): Boolean =
        (emailText.matches(Regex("[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}")))

    private fun checkPasswordLength(passwordText: String): Boolean =
        passwordText.trim().length >= MIN_PASSWORD_LENGTH

    private fun checkEmptyInputs(
        vararg values: String

    ): Boolean {
        return values.all { it.trim().isEmpty() }
    }

    private fun validateFields(): Boolean {
        val isValidPass = checkPasswordLength(password.value.toString())
        val isValidEmail = checkIfValidEmail(email.value.toString())
        val areEmpty = checkEmptyInputs(
            email.value.toString(),
            password.value.toString(),
            username.value.toString()
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
        if (!validateFields()) return
        _isLoading.value = true
        _authType.value = AuthType.CREDENTIAL

        viewModelScope.launch {
            try {
                userRepo.signUp(
                    username = username.value.toString(),
                    email = email.value.toString(),
                    password = password.value.toString()
                )
                _toastMessage.value = "Success"
                _isAuthentificated.value = true

            } catch (e: AppwriteException) {
                Log.d("error appwrite", e.message.toString())
                _errorMessage.value = e.message.toString()
                _isLoading.value = false
                _isAuthentificated.value = false

            } finally {
                _isLoading.value = false
            }

        }
    }


    fun onLogin() {
        if (!checkEmptyInputs()) {
            _toastMessage.value = "all Fields must be filled"
            return
        }
        _isLoading.value = true
        _authType.value = AuthType.CREDENTIAL


        viewModelScope.launch {
            try {
                userRepo.login(
                    email = email.value.toString(),
                    password = password.value.toString()
                )
                _toastMessage.value = "Success"
                _isAuthentificated.value = true

            } catch (e: AppwriteException) {
                Log.d("error appwrite", e.message.toString())
                _errorMessage.value = e.message.toString()
                _isLoading.value = false
                _isAuthentificated.value = false


            } finally {
                _isLoading.value = false

            }
        }


    }


    fun googleSignUp(activity: ComponentActivity) {

        _authType.value = AuthType.GOOGLE
        _isLoading.value = true

        viewModelScope.launch {

            try {
                userRepo.googleSignUp(
                    activity = activity
                )
                _toastMessage.value = "Success"
                _isAuthentificated.value = true
            } catch (e: AppwriteException) {
                Log.d("error appwrite", e.message.toString())
                _errorMessage.value = e.message.toString()
                _isLoading.value = false
                _isAuthentificated.value = false

            } finally {
                _isLoading.value = false

            }


        }

    }


    fun logout() {

        viewModelScope.launch {
            try {
                userRepo.logout()
                _isAuthentificated.value = false

            } catch (e: AppwriteException) {
                Log.d("appwrite:auth", "${e.message}")

            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        // Clean up resources if necessary (e.g., cancel coroutines, close database connections)
        println("AuthViewModel onCleared: ViewModel is being destroyed.")
    }

}


