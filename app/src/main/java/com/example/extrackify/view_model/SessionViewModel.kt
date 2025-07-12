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


sealed class SessionStateFlow() {
    object Loading : SessionStateFlow()
    data class Error(val err: String) : SessionStateFlow()
    data class Success(val message: String) : SessionStateFlow()
    object Idle : SessionStateFlow()
}

@HiltViewModel
class SessionViewModel @Inject constructor(
    val userRepository: UserRepository,
    val sessionManager: SessionManager

) : ViewModel() {

    private val _sessionState = MutableLiveData<SessionStateFlow>()

    val sessionState: LiveData<SessionStateFlow>
        get() = _sessionState;


    private val _session = MutableLiveData<ActiveSession?>(null)


    fun saveSession() {
        _sessionState.value = SessionStateFlow.Loading

        viewModelScope.launch {
            try {
                _session.value = userRepository.getSession()

                sessionManager.saveSession(_session.value!!)

                Log.d("appwrite:session", "${_session.value}")

                _sessionState.value = SessionStateFlow.Success("session Loaded")

            } catch (e: AppwriteException) {
                _sessionState.value = SessionStateFlow.Success("$e.message")

                Log.d("appwrite", "${e.message}")

            } finally {
                _sessionState.value = SessionStateFlow.Idle


            }

        }


    }
}