package com.example.extrackify.utils

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import java.time.Instant
import java.time.OffsetDateTime
import java.time.format.DateTimeParseException
import javax.inject.Inject
import javax.inject.Singleton


data class ActiveSession(val expire: String, val sessionId: String, val userId: String)

private val Context.dataStore by preferencesDataStore("user_session")

@Singleton
class SessionManager @Inject constructor(@ApplicationContext private val context: Context) {


    companion object {


        val SESSION_ID = stringPreferencesKey("session_id")
        val SESSION_EXPIRE_DATE = stringPreferencesKey("session_expire")
        val SESSION_USER_ID = stringPreferencesKey("session_userId")


    }

    suspend fun saveSession(session: ActiveSession) {

        context.dataStore.edit { pref ->
            pref[SESSION_EXPIRE_DATE] = session.expire
            pref[SESSION_ID] = session.sessionId
            pref[SESSION_USER_ID] = session.userId
        }

    }

    suspend fun getStoredSession(): Map<String, Any> {

        val prefs = context.dataStore.data.first()
        return prefs.asMap().mapKeys { it.key.name }


    }

    suspend fun isSessionValid(): Boolean {

        val prefs = context.dataStore.data.first()

        val expireAt = prefs[SESSION_EXPIRE_DATE] ?: return false


        return try {
            val expireTime = OffsetDateTime.parse(expireAt).toInstant()

            Log.d("date:parsed", "$expireTime")

            Instant.now().isBefore(expireTime)

        } catch (e: DateTimeParseException) {
            Log.d("date:error", "${e.message}")
            false
        }


    }

    suspend fun clearStore() {
        context.dataStore.edit { preferences -> preferences.clear() }
    }


}

