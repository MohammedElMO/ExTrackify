package com.example.extrackify.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take

private val Context.dataStore by preferencesDataStore("user_session")

class SessionManager(private val context: Context) {


    companion object {
        val USER_ID = stringPreferencesKey("user_id")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_EMAIL = stringPreferencesKey("user_email")


    }

    suspend fun saveUser(id: String?, email: String?, name: String?) {

        context.dataStore.edit { pref ->
            pref[USER_ID] = id as String
            pref[USER_NAME] = name as String
            pref[USER_EMAIL] = email as String


        }

    }

    fun getUserId(): Flow<String?> {
        return context.dataStore.data.map { it[USER_ID] }
    }

}

