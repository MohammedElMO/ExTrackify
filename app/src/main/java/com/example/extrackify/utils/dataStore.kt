package com.example.extrackify.utils

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject


val Context.dataStore by preferencesDataStore("extrakify-store")

class DataStore @Inject constructor(@ApplicationContext val ctx: Context) {

    companion object {
        val IS_AUTHENTICATED = booleanPreferencesKey("is_authenticated")
    }

//    init {
//        CoroutineScope(Dispatchers.IO).launch {
//            saveKey(IS_AUTHENTICATED, false)
//        }
//    }

    suspend fun <V> saveKey(key: Preferences.Key<V>, value: V) {

        ctx.dataStore.edit { pref ->
            pref[key] = value
        }
    }

    suspend fun <T>getValue(key: Preferences.Key<T>): Flow<T?> {
        return ctx.dataStore.data.map { pref -> pref[key] }
    }


}