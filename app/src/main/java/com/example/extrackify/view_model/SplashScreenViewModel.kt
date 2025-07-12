package com.example.extrackify.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.extrackify.models.UserRepository
import com.example.extrackify.utils.ActiveSession
import com.example.extrackify.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import io.appwrite.exceptions.AppwriteException
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    val userRepository: UserRepository, val sessionManager: SessionManager
) : ViewModel() {

    private val _isLoadingAuthState = MutableLiveData<Boolean>(true)

    val isLoadingAuthState: LiveData<Boolean>
        get() = _isLoadingAuthState;


    private val _session = MutableLiveData<ActiveSession?>(null)
    val session: LiveData<ActiveSession?>
        get() = _session;

    init {
        checkSession()
    }

    private fun checkSession() {
        viewModelScope.launch {
            try {
                if (sessionManager.isSessionValid()) {

                    val storedSession = sessionManager.getStoredSession()

                    _session.value = ActiveSession(
                        userId = storedSession["session_userId"] as String,
                        sessionId = storedSession["session_id"] as String,
                        expire = storedSession["session_expire"] as String
                    )
                    Log.d("stored:session", "${_session.value}")


                } else {
                    _session.value = userRepository.getSession()

                    sessionManager.saveSession(_session.value!!)


                    Log.d("appwrite:session", "${_session.value}")

                }


            } catch (e: AppwriteException) {
                Log.d("appwrite:sessionCheck", "${e.message}")
                _isLoadingAuthState.value = false
//                _session.value = null


            } finally {
                _isLoadingAuthState.value = false

            }


        }


    }

}
