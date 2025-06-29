package com.example.extrackify.models

import androidx.activity.ComponentActivity
import com.example.extrackify.appwrite.AppWriteService
import com.example.extrackify.utils.ActiveSession
import io.appwrite.ID
import io.appwrite.enums.OAuthProvider
import io.appwrite.models.Session
import io.appwrite.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepository @Inject constructor(val appWriteService: AppWriteService) {


    suspend fun login(email: String, password: String): Session {
        return appWriteService.account.createEmailPasswordSession(email, password)
    }

    suspend fun signUp(username: String, email: String, password: String): User<Map<String, Any>> {

        return appWriteService.account.create(
            name = username,
            userId = ID.unique(),
            email = email,
            password = password
        )
    }

    suspend fun logout(): Any {
        return appWriteService.account.deleteSession("current")
    }

    suspend fun getSession(): ActiveSession {

        return withContext(Dispatchers.IO) {
            val session = appWriteService.account.getSession("current")


            ActiveSession(expire = session.expire, sessionId = session.id, userId = session.userId)
        }


    }

    suspend fun googleSignUp(activity: ComponentActivity) {
        return withContext(Dispatchers.IO) {
            appWriteService.account.createOAuth2Session(
                provider = OAuthProvider.GOOGLE,
                activity = activity,
            )
        }


    }


}