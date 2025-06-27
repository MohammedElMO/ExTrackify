package com.example.extrackify.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.extrackify.models.UserRepository
import com.example.extrackify.utils.SessionManager
import io.appwrite.exceptions.AppwriteException
import io.appwrite.models.User
import kotlinx.coroutines.launch

class SplashScreenViewModel(
    val userRepository: UserRepository,
    val sessionManager: SessionManager
) : ViewModel() {

    private val _isLoadingSession = MutableLiveData<Boolean>(false)

    val isLoadingSession: LiveData<Boolean>
        get() = _isLoadingSession;


    private val _session = MutableLiveData<User<Map<String, Any>>>()

    val session: LiveData<User<Map<String, Any>>>
        get() = _session;

    init {
        getSession()
    }

    private fun getSession() {
        _isLoadingSession.value = true
        viewModelScope.launch {
            try {
                _session.value = userRepository.getSession()


                sessionManager.saveUser(
                    id = _session.value?.id,
                    email = _session.value?.email,
                    name = _session.value?.name
                )

                Log.d("appwrite", "$session")

                _isLoadingSession.value = false

            } catch (e: AppwriteException) {
                Log.d("appwrite", "${e.message}")
                _isLoadingSession.value = false

            }


        }


    }

}
