package com.example.extrackify.view_model

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.extrackify.constants.AuthType
import com.example.extrackify.models.UserRepository
import com.example.extrackify.utils.DataStore
import com.example.extrackify.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import io.appwrite.exceptions.AppwriteException
import kotlinx.coroutines.launch
import javax.inject.Inject

private val MIN_PASSWORD_LENGTH = 8


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepo: UserRepository,
    private val sessionManager: SessionManager,
    private val dataStore: DataStore
) : ViewModel() {

    val username = MutableLiveData("adadgag")
    val email = MutableLiveData("mohammed@gmail.com")
    val password = MutableLiveData("mohammed123")
    private val _isLoading = MutableLiveData(false)
    private val _errorMessage = MutableLiveData("")
    private val _toastMessage = MutableLiveData<String>(null)
    private val _isAuthenticated = MutableLiveData<Boolean>(false)
    private val _authType = MutableLiveData<AuthType?>()

    val isLoading: LiveData<Boolean>
        get() = _isLoading


    val authType: LiveData<AuthType?>
        get() = _authType

    val isAuthenticated: LiveData<Boolean>
        get() = _isAuthenticated


    val errorMessage: LiveData<String>
        get() = _errorMessage

    val toastMessage: LiveData<String>
        get() = _toastMessage

    //            dataStore.getValue(DataStore.IS_AUTHENTICATED)
//                .onEach {
//                    _isAuthenticated.value = it
//                    _isLoading.value = false
//
//                }.launchIn(this)
    init {
        _isLoading.value = true

        viewModelScope.launch {
            Log.d("auth:state",sessionManager.isSessionValid().toString())
            _isAuthenticated.value = !sessionManager.isSessionValid()
            _isLoading.value = false

        }

    }


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
                _isAuthenticated.value = true
//                dataStore.saveKey(DataStore.IS_AUTHENTICATED, true)

            } catch (e: AppwriteException) {
                Log.d("error appwrite", e.message.toString())
                _errorMessage.value = e.message.toString()
                _isAuthenticated.value = false
//                dataStore.saveKey(DataStore.IS_AUTHENTICATED, false)

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
                _isAuthenticated.value = true
//                dataStore.saveKey(DataStore.IS_AUTHENTICATED, true)
            } catch (e: AppwriteException) {
                Log.d("error appwrite", e.message.toString())
                _errorMessage.value = e.message.toString()
                _isAuthenticated.value = false
//                dataStore.saveKey(DataStore.IS_AUTHENTICATED, true)


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
                _isAuthenticated.value = true
//                dataStore.saveKey(DataStore.IS_AUTHENTICATED, true)

            } catch (e: AppwriteException) {
                Log.d("error appwrite", e.message.toString())
                _errorMessage.value = e.message.toString()
                _isLoading.value = false
                _isAuthenticated.value = false
//                dataStore.saveKey(DataStore.IS_AUTHENTICATED, false)

            } finally {
                _isLoading.value = false

            }


        }

    }


    fun logout() {

        viewModelScope.launch {
            try {
                userRepo.logout()
                sessionManager.clearStore()
//                dataStore.saveKey(DataStore.IS_AUTHENTICATED, false)

                _isAuthenticated.value = false
            } catch (e: AppwriteException) {
                _isAuthenticated.value = true
                e.printStackTrace()
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


