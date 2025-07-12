package com.example.extrackify.view_model

import android.content.Context
import android.credentials.GetCredentialException
import android.os.Build
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.extrackify.models.UserRepository
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import io.appwrite.enums.OAuthProvider
import io.appwrite.exceptions.AppwriteException
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

sealed class AuthUIState {
    object Idle : AuthUIState()
    object Loading : AuthUIState()
    data class Success(val message: String) : AuthUIState()
    data class Error(val err: String) : AuthUIState()
    data class Validating(val validationMessage: String) : AuthUIState()

}

@HiltViewModel
open class BaseAuthViewModel @Inject constructor(val userRepository: UserRepository) : ViewModel() {

    open val _email = MutableLiveData("mohammed@gmail.com")
    val _password = MutableLiveData("mohammed123")
    val _authState = MutableLiveData<AuthUIState>(AuthUIState.Idle)

    val _authType = MutableLiveData<AuthType>()

    val isLoading = _authState.map { it is AuthUIState.Loading }

    fun checkEmptyInputs(
        vararg values: String
    ): Boolean {
        return values.all { it.trim().isEmpty() }
    }

    fun githubSingUp(activity: ComponentActivity) {
        _authType.value = AuthType.OAUTH
        _authState.value = AuthUIState.Loading
        viewModelScope.launch {
            try {
                userRepository.OAuth2Singup(activity = activity, OAuthProvider.GITHUB)
                _authState.value = AuthUIState.Success("Singup Successfully")

            } catch (e: AppwriteException) {
                Log.e("discord:auth", "${e.message}")
                _authState.value = AuthUIState.Error(e.message.toString())

            } catch (e: IllegalStateException) {
                Log.e("discord:auth", "${e.message}")
                _authState.value = AuthUIState.Error(e.message.toString())

            } finally {
                _authState.value = AuthUIState.Idle
            }

        }

    }

    val GOOGLE_WEB_CLIENT_ID =
        "528371290738-i1epfhugha4489cesuvhrfvfpjf56b0j.apps.googleusercontent.com"


    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun googleSingUp(ctx: Context) {
        _authType.value = AuthType.OAUTH
        val googleIdOption: GetGoogleIdOption =
            GetGoogleIdOption.Builder().setFilterByAuthorizedAccounts(true)
                .setServerClientId(GOOGLE_WEB_CLIENT_ID).setAutoSelectEnabled(false).build()

        val credentialManager = androidx.credentials.CredentialManager.create(ctx)
        val request =
            androidx.credentials.GetCredentialRequest.Builder().addCredentialOption(googleIdOption)
                .build()

        viewModelScope.launch {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = ctx,
                )
                handleGoogleSingUp(result)

            } catch (e: GetCredentialException) {
                Log.e("google:singup:err", "${e.message}")
            } catch (e: GetCredentialCancellationException) {
                Log.e("google:singup:err", "${e.message}")
                _authState.value = AuthUIState.Error("${e.message}")

            }
        }

    }

    fun handleGoogleSingUp(result: GetCredentialResponse) {


        viewModelScope.launch {

            when (val credentials = result.credential) {

                is GoogleIdTokenCredential -> {

                    try {
                        val payload = JSONObject()


                        payload.put("idToken", credentials.idToken)
                        val execFunc = userRepository.triggerFunction(
                            com.example.extrackify.constants.Config.GOOGLE_AUTH_FUCNTION_ID,
                            payload.toString()
                        )
                        val response = execFunc.responseBody
                        Log.d("fun:result", "$execFunc")

                        val json = JSONObject(response)
                        val tokenSecret = json.optString("tokenSecret", "")
                        val userId = json.optString("appwriteUserId", "")


                        if (tokenSecret != "") {

                            userRepository.createSession(userId = userId, secret = tokenSecret)

                            Log.d("google:auth", "success")
                            _authState.value = AuthUIState.Success("sing-up successfully")

                        } else {
                            Log.d("google:auth", "error")

                            _authState.value = AuthUIState.Error("error singup")

                        }


                    } catch (e: AppwriteException) {

                        Log.e("Appwrite", "Function call failed: ${e.message}")

                        _authState.value = AuthUIState.Error("error singup")

                    }

                }
            }
        }


    }


    fun discordSingUp(activity: ComponentActivity) {
        _authType.value = AuthType.OAUTH
        _authState.value = AuthUIState.Loading
        viewModelScope.launch {
            try {
                try {
                    userRepository.OAuth2Singup(activity = activity, OAuthProvider.DISCORD)
                    _authState.value = AuthUIState.Success("Singup Successfully")

                } catch (e: IllegalStateException) {
                    Log.e("discord:auth", "${e.message}")
                    _authState.value = AuthUIState.Error(e.message.toString())

                }


            } catch (e: AppwriteException) {
                Log.e("discord:auth", "${e.message}")
                _authState.value = AuthUIState.Error(e.message.toString())

            } finally {
                _authState.value = AuthUIState.Idle
            }

        }

    }


}