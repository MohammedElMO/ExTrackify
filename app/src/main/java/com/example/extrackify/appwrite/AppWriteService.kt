package com.example.extrackify.appwrite

import android.content.Context
import androidx.activity.ComponentActivity
import com.example.extrackify.constants.Config
import io.appwrite.Client
import io.appwrite.ID
import io.appwrite.enums.OAuthProvider
import io.appwrite.models.Session
import io.appwrite.models.User
import io.appwrite.services.Account
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object AppWriteService {

    lateinit var client: Client;
    lateinit var account: Account;
    fun init(context: Context) {

        if (!::client.isInitialized) {
            client = Client(context)
            client.setProject(Config.APPWRITE_PROJECT_ID)
            client.setEndpoint(Config.APPWRITE_API_ENDPOINT)

        }
        if (!::account.isInitialized) {
            account = Account(client)
        }


    }


    suspend fun login(email: String, password: String): Session {
        return account.createEmailPasswordSession(email, password)
    }

    suspend fun signUp(name: String, email: String, password: String): User<Map<String, Any>> {

        return account.create(userId = ID.unique(), email = email, password = password)
    }

    suspend fun logout(): Any {
        return account.deleteSession("current")
    }

    suspend fun getSession(): Session {

        return withContext(Dispatchers.IO) {

            account.getSession("current")
        }


    }


    suspend fun googleSignUp(activity: ComponentActivity) {

        return account.createOAuth2Session(
            provider = OAuthProvider.GOOGLE,
            activity = activity,
//            success = "appwrite-callback-68545c4f0035d81094e4://success"
        )


    }


}