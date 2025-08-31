package com.example.extrackify.utils

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.time.Instant
import java.time.OffsetDateTime
import java.time.format.DateTimeParseException
import javax.inject.Inject
import javax.inject.Singleton


data class ActiveSession(val expire: String, val sessionId: String, val userId: String)

private val Context.sessionStorage by preferencesDataStore("user_session")

@Singleton
class SessionManager @Inject constructor(@ApplicationContext private val context: Context) {


    companion object {

        val SESSION_ID = stringPreferencesKey("session_id")
        val SESSION_EXPIRE_DATE = stringPreferencesKey("session_expire")
        val SESSION_USER_ID = stringPreferencesKey("session_userId")
    }

    suspend fun saveSession(session: ActiveSession) {

        context.sessionStorage.edit { pref ->
            pref[SESSION_EXPIRE_DATE] = session.expire
            pref[SESSION_ID] = session.sessionId
            pref[SESSION_USER_ID] = session.userId
        }

    }
    suspend fun getUserId(): String? {
        val prefs = context.sessionStorage.data.first()

        return prefs[SESSION_USER_ID]
    }

    suspend fun getStoredSession(): Map<String, Any> {

        val prefs = context.sessionStorage.data.first()
        return prefs.asMap().mapKeys { it.key.name }


    }

    suspend fun isSessionValid(): Boolean {

        val prefs = context.sessionStorage.data.first()

        val expireAt = prefs[SESSION_EXPIRE_DATE]

        if (expireAt == null) return false

        Log.d("date:got", expireAt)
        try {
            val expireTime = OffsetDateTime.parse(expireAt).toInstant()

            Log.d("date:parsed", "$expireTime")

            return Instant.now().isBefore(expireTime)

        } catch (e: DateTimeParseException) {
            Log.d("date:error", "${e.message}")
            return false
        }


    }


    suspend fun clearStore() {

            context.sessionStorage.edit { preferences -> preferences.clear()
            Log.d("store:log","store cleared all")}


    }


}

