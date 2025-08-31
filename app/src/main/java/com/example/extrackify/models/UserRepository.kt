package com.example.extrackify.models

import androidx.activity.ComponentActivity
import com.example.extrackify.appwrite.AppWriteService
import com.example.extrackify.utils.ActiveSession
import io.appwrite.ID
import io.appwrite.enums.OAuthProvider
import io.appwrite.models.Execution
import io.appwrite.models.Session
import io.appwrite.models.Token
import io.appwrite.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepository @Inject constructor(val appWriteService: AppWriteService) {


    suspend fun isAuthenticated() = appWriteService.account.getSession("current")

    suspend fun login(email: String, password: String): Session {
        return withContext(Dispatchers.IO) {
            appWriteService.account.createEmailPasswordSession(email, password)

        }
    }

    suspend fun signUp(username: String, email: String, password: String): User<Map<String, Any>> {
        return withContext(Dispatchers.IO) {
            appWriteService.account.create(
                name = username,
                userId = ID.unique(),
                email = email,
                password = password
            )
        }

    }

    suspend fun logout() {
        withContext(Dispatchers.IO) {

            appWriteService.account.deleteSession("current")
        }
    }

    suspend fun createSession(userId: String, secret: String) {
        withContext(Dispatchers.IO) {

            appWriteService.account.createSession(userId, secret)
        }

    }


    suspend fun verifyEmail(): Token {
        return appWriteService.account.createVerification("https://extrackify.software/email-verified")
    }


    suspend fun getSession(): ActiveSession {

        return withContext(Dispatchers.IO) {
            val session = appWriteService.account.getSession("current")


            ActiveSession(expire = session.expire, sessionId = session.id, userId = session.userId)
        }


    }

    suspend fun OAuth2Singup(activity: ComponentActivity, provider: OAuthProvider) {

        return withContext(Dispatchers.IO) {
            appWriteService.account.createOAuth2Session(
                activity = activity,
                provider = provider,

                )
        }

    }


    suspend fun triggerFunction(functionId: String, payload: String): Execution {
        return withContext(Dispatchers.IO) {

            appWriteService.functions.createExecution(
                functionId = functionId,
                body = payload

            )
        }

    }


}


