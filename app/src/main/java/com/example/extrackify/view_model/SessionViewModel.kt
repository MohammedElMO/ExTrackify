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
class SessionViewModel @Inject constructor(
    val userRepository: UserRepository,
    val sessionManager: SessionManager

) : ViewModel() {

    private val _isLoadingAuthState = MutableLiveData<Boolean>(false)

    val isLoadingAuthState: LiveData<Boolean>
        get() = _isLoadingAuthState;


    private val _session = MutableLiveData<ActiveSession?>(null)
    val session: LiveData<ActiveSession?>
        get() = _session;


    fun setSession() {
        _isLoadingAuthState.value = true
        viewModelScope.launch {
            try {
                sessionManager.clearStore()
                _session.value = userRepository.getSession()

                sessionManager.saveSession(_session.value!!)

                Log.d("appwrite:session", "${_session.value}")


            } catch (e: AppwriteException) {
                Log.d("appwrite", "${e.message}")

            } finally {
                _isLoadingAuthState.value = false

            }


        }


    }
}