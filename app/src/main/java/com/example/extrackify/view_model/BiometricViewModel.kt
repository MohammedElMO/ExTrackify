package com.example.extrackify.view_model

import android.content.Intent
import android.provider.Settings
import android.util.Log
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


sealed class BiometricFlowState() {
    data class Success(val message: String) : BiometricFlowState()
    data class Failed(val reason: String) : BiometricFlowState()
    data class Error(val err: String) : BiometricFlowState()
    object Loading : BiometricFlowState()
    data class UnSupported(val supportReason: String) : BiometricFlowState()
}

@HiltViewModel
class BiometricsViewModel @Inject constructor() : ViewModel() {


    private val _biometricAuthState = MutableLiveData<BiometricFlowState>()

    val biometricAuthState: LiveData<BiometricFlowState>
        get() = _biometricAuthState

    fun biometricSupportChecker(ctx: FragmentActivity) {
        val biometricManager = BiometricManager.from(ctx)

        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.")
                runBiometricCheck(ctx)

            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Log.e("MY_APP_TAG", "No biometric features available on this device.")
                _biometricAuthState.value =
                    BiometricFlowState.UnSupported("No biometric features available on this device.")
            }

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.")

                _biometricAuthState.value =
                    BiometricFlowState.UnSupported("Biometric features are currently unavailable.")
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
//                _biometricAuthState.value = BiometricFlowState.UnSupported("Biometric features are currently unavailable.")

                // Prompts the user to create credentials that your app accepts.
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(
                        Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                    )
                }
                startActivityForResult(ctx, enrollIntent, 0, null)
            }

            else ->  {
                Log.d("Biometric", "dissmissed")
            }

        }
    }

    private fun runBiometricCheck(ctx: FragmentActivity) {

        val executor = ContextCompat.getMainExecutor(ctx)

        val biometricPrompt = BiometricPrompt(
            ctx, executor,

            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Log.d("BIO", "Authentication succeeded!")
                    _biometricAuthState.value = BiometricFlowState.Success("login successfully")
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Log.d("BIO", "Authentication failed.")
                    _biometricAuthState.value = BiometricFlowState.Failed("login failed")

                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    _biometricAuthState.value = BiometricFlowState.Error("$errString")

                    Log.e("BIO", "Error: $errString")
                }
            }
        )

        val biomInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Verify Your Identity")
            .setSubtitle("to continue to your Account")
            .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL )
            .build()

        biometricPrompt.authenticate(biomInfo)
    }


}