package com.example.extrackify.appwrite

import android.content.Context
import com.example.extrackify.constants.Config
import dagger.hilt.android.qualifiers.ApplicationContext
import io.appwrite.Client
import io.appwrite.services.Account
import io.appwrite.services.Databases
import io.appwrite.services.Functions
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppWriteService @Inject constructor(@ApplicationContext private val ctx: Context) {

    val client: Client = Client(ctx)

    val account: Account = Account(client)

    val db = Databases(client)

    val functions = Functions(client)


    init {
        client.setProject(Config.APPWRITE_PROJECT_ID)
        client.setEndpoint(Config.APPWRITE_API_ENDPOINT)
    }


}