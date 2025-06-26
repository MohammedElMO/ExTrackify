package com.example.extrackify.models

import android.content.Context

import com.example.extrackify.appwrite.AppWriteService
import io.appwrite.models.User

class UserRepository(ctx: Context) {
    private val appwrite = AppWriteService.init(ctx)

    suspend fun createUser(username: String, email: String, password: String): User<Map<String, Any>> {
        return AppWriteService.signUp(username, email, password)
    }
}