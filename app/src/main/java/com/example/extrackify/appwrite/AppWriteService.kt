package com.example.extrackify.appwrite

import android.content.Context
import com.example.extrackify.constants.Config
import io.appwrite.Client
import io.appwrite.ID
import io.appwrite.models.Session
import io.appwrite.models.User
import io.appwrite.services.Account

object AppWriteService {

    lateinit var client: Client;
    lateinit var account: Account;
    fun init(context: Context) {
        client = Client(context = context)

        client.setProject(Config.APPWRITE_PROJECT_ID)
        client.setEndpoint(Config.APPWRITE_API_ENDPOINT)

        account = Account(client)
    }

   suspend fun login(email:String,password:String): Session {
       return account.createEmailPasswordSession(email,password)
    }

    suspend fun signUp(name:String,email:String,password: String): User<Map<String,Any>> {

        return account.create(userId = ID.unique(), email = email, password = password)
    }

    suspend fun logout(): Any {
        return account.deleteSession("current")
    }


}