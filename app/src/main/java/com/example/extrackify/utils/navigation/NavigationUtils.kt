package com.example.extrackify.utils.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent

class NavigationUtils {
    companion object {

        fun navigateToActivity(context: Context, toActivity: Class<*>) {
            val intent = Intent(context, toActivity)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            context.startActivity(intent)

        }


    }
}